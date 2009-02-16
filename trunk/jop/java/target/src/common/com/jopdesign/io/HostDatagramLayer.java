package com.jopdesign.io;


import java.lang.Thread;
import jftd2xx.JFTD2XX;

public class HostDatagramLayer extends BaseDatagramLayer{
	private final int TIME_OUT = 100;
	private final int MAX_USBBUF = 64;
	private byte usbBuf[] = new byte[MAX_USBBUF];
	private JFTD2XX usb;
	private boolean linkOK;
	public HostDatagramLayer(){
		try {
			usb  = new JFTD2XX(0);
			close();
			open();
			linkOK = true;
			//usb.setLatencyTimer(2);
		} catch (Exception e){
			linkOK = false;
		}
	}
	
	public boolean isLinkOK(){return linkOK;}
	public void close() {
		try {
			usb.closeConnection();
			usb.close();
		}catch (Exception e){}
	}
	public void open() {
		try {
			usb.open(0);
			usb.initConnection();
		}catch (Exception e){}
	}
	private byte readByteFromUSB(){
		usbBuf[0] = (byte) 0xC1;
		usbBuf[1] = 0;
		try {
			usb.write(usbBuf, 0, 2);
			if (1 == usb.read(usbBuf, 0, 1)) {
				if (usbBuf[0] > DATAGRAM_END ) //A readable char, not Data
						System.out.print((char)usbBuf[0]);
				return usbBuf[0];
			}else 
				return 0x00;
		}catch(Exception e){
			return 0x00;
		}
	}
	
	public boolean sendDatagram(byte[] data, int len) {
		assert(PACKET_LEN==56);
		byte b[] = new byte[PACKET_LEN];
		b[0]= (byte) (((len&0x7F)<<1) + 1);
		b[1]= (byte) ((((len>>>7)&0x7F)<<1) + 1);
		b[2]= (byte) ((((len>>>14)&0x7F)<<1)+ 1);
		b[3]= (byte) ((((len>>>21)&0x7F)<<1) + 1);
		b[4]=b[5]=b[6]=b[7]=b[8]=b[9]=b[10]=b[11]=DATAGRAM_LEN_HEADER;
		while(!sendPacket(b,12));// return false; 
		
		//One packet is able to carry 56 bytes. 
		//Each byte contains only 7 bits of data
		//So each packe maps to 56*7/8=49 bytes information
		for(int i=0;i<(len+48)/49;i++){// i*49..i*49+48
			for(int j=0;j<7;j++) // i*49+(j*7..j*7+6)
				BytesExpand(data, i*49+j*7, b, j*8);
			if (!sendPacket(b, 56)) return false;
		}
		return true;
	}
	

	/**
	 *  data[] requirement: No 0x00, 0xAA, maximum length PACKET_LEN=56
	 */
	public boolean sendPacket(byte[] data,int len) {
		int i;
		int timeout = 0;
		byte d,d1;
		d=d1=-1;
		usbBuf[0] = (byte)(0x80|(PACKET_LEN+2));
		usbBuf[1] = (byte)DATAGRAM_HEADER;
		for (i=0;i<len;i++)
			usbBuf[2+i] = data[i];
		for (i=len;i<PACKET_LEN;i++)
			usbBuf[2+i] = 1;
		usbBuf[2+PACKET_LEN]= DATAGRAM_END;
		try {
			if (usb.write(usbBuf, 0, PACKET_LEN+3) != PACKET_LEN+3)
				return false;
			d = readByteFromUSB();
			while (!( (d1==(byte)DATAGRAM_ACK)&&(d==0) )) {
				timeout++;
				if (timeout>TIME_OUT) return false;
				d1 = d;
				d = readByteFromUSB();
			}
			readByteFromUSB();
			usbBuf[0] = (byte)0x81; //Block JoP.readPacket
			usbBuf[1] = DATAGRAM_END-1;
			usb.write(usbBuf, 0, 2);
			return true;
		}catch(Exception e){
			return false;
		}
 	}
	
	public boolean readPacket(byte[] data) {
		byte d;
		int c = 0;
		while ((d=readByteFromUSB())== 0 );
		while (d != 0){
			data[c++] = d;
			d= readByteFromUSB();
		}
		return true;
			
	}
//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
	public int readSwitchStates(){
		final int burst_len = 25;
		int i, j;
		int c = 0;
		byte d;
		byte data[] = new byte[burst_len];
		usbBuf[0] = (byte) (0xC0 | burst_len);
		for(i=0;i<burst_len;i++) usbBuf[i+1] = 0;
		try {
			usb.write(usbBuf, 0, burst_len+1);
			if (burst_len != usb.read(usbBuf, 0, burst_len)) return -1;
			
			i = burst_len;
			while (i > 0) {
				i--;
				d = usbBuf[i];
				if (d == 0) continue;
				j = 0;
				while ((d&0x80) == 0) {j++; d <<=1;}
				if (j == 0) //Data in a single byte
					data[c++] = (byte) (d & 0x7F);
				else {//Data go across byte boundary
					data[c++] = (byte) ((  d | (((usbBuf[i-1]&0xFF)>>>(8-j))&0xFF)  ) & 0x7F);
					i--;
				}
			}
				//System.out.println();
			if (c == 5 && (data[0] == ((data[1]+data[2]+data[3]+data[4])&0x7F))) //Checksum & length
				return   data[4] | 
					   ( data[3]<<7 ) |
					   ( data[2]<<14 ) |
					   ( data[1]<<21 );
			else {
				if (c != 5) return -1;
/*
					for(i=0;i<burst_len;i++)
						System.out.printf("%02x ", (byte)usbBuf[i]);
					System.out.println();
					for(i=0;i<c;i++)
						System.out.printf("%02x", (byte)data[c-i-1]);
					System.out.println();
*/
				return -1;
			}
		}catch(Exception e){
			return -1;
		}
	}
/*
	public int readSwitchStates(){
			//System.out.printf("%02x",data);
			int count = 0;
			int states = -1;
			int data;
			int timeout;
			//while (count < 4) { //Invalid Packet
				states = 0;
				count = 0;
				timeout = 0;
				while((data = readByteFromUSB()) == 0x00) {
					timeout++;
					if (timeout == TIME_OUT) return -1;
				}
				while(data != 0x00){
						states = (states>>>7)| ((data&0x7F)<<21);
					//System.out.printf("%02x",(byte)data);
					data = readByteFromUSB();
					count++;
				}
				//System.out.printf("%02x\n",(byte)data);
				//System.out.printf("%02x %08x\n",(byte)data, states);
			//}
			if (count != 4)
				return -1;
			else
				return states;
	}
*/

	public int sendLEDHEXStates(int LEDStates, long HEXStates){
			int byteCount = 1;
			int States;

			usbBuf[byteCount++]= (DATAGRAM_HEADER); 
			usbBuf[byteCount++]= 0x21; //LED Packet Signature.
			usbBuf[byteCount++]= 0x43; 
			usbBuf[byteCount++]= 0x65; 
			usbBuf[byteCount++]= 0x78; 

			States = LEDStates; //LED states
			usbBuf[byteCount++]= ((byte)((States&0x7F)+4)); States >>>= 7;
			usbBuf[byteCount++]= ((byte)((States&0x7F)+4)); States >>>= 7;
			usbBuf[byteCount++]= ((byte)((States&0x7F)+4)); States >>>= 7;
			usbBuf[byteCount++]= ((byte)((States&0x7F)+4)); 

			States = (int)(HEXStates & 0xFFFFFFF); //Lowest 28 bits for HEX7654
			usbBuf[byteCount++]= ((byte)((States&0x7F)+4)); States >>>= 7;
			usbBuf[byteCount++]= ((byte)((States&0x7F)+4)); States >>>= 7;
			usbBuf[byteCount++]= ((byte)((States&0x7F)+4)); States >>>= 7;
			usbBuf[byteCount++]= ((byte)((States&0x7F)+4)); 

			States = (int)((HEXStates >>> 28) & 0xFFFFFFF); //[55..28]bits for HEX3210
			usbBuf[byteCount++]= ((byte)((States&0x7F)+4)); States >>>= 7;
			usbBuf[byteCount++]= ((byte)((States&0x7F)+4)); States >>>= 7;
			usbBuf[byteCount++]= ((byte)((States&0x7F)+4)); States >>>= 7;
			usbBuf[byteCount++]= ((byte)((States&0x7F)+4)); 
			while (byteCount < PACKET_LEN+2) usbBuf[byteCount++] = 1; //Paddings
			usbBuf[byteCount++]= DATAGRAM_END; //Termination



			usbBuf[0] = (byte) (0x80 | (byteCount-1));
			try {
				if (byteCount > 1)
					if (usb.write(usbBuf, 0, byteCount)==byteCount){
						return 0;
					}else
						return 1;//System.out.println("send() error.");
			} catch(Exception e){}
			return 1;
		}
	
	
}


