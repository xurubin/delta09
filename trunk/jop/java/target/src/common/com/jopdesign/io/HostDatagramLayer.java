package com.jopdesign.io;


import java.lang.Thread;
import jftd2xx.JFTD2XX;

public class HostDatagramLayer extends BaseDatagramLayer{
	private final int TIME_OUT = 100;
	private final int MAX_USBBUF = 64;
	private byte usbBuf[] = new byte[MAX_USBBUF];
	private JFTD2XX usb;
	
	public HostDatagramLayer(){
		try {
			usb  = new JFTD2XX(0);
			close();
			open();
			//usb.setLatencyTimer(2);
		} catch (Exception e){
			System.exit(1);
		}
	}
	
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
	
	public int sendLEDStates(int States){
			int byteCount = 1;

			usbBuf[byteCount++]= (DATAGRAM_HEADER); 
			usbBuf[byteCount++]= ((byte)((States&0x7F)+4)); States >>>= 7;
			usbBuf[byteCount++]= ((byte)((States&0x7F)+4)); States >>>= 7;
			usbBuf[byteCount++]= ((byte)((States&0x7F)+4)); States >>>= 7;
			usbBuf[byteCount++]= ((byte)((States&0x7F)+4)); 
			usbBuf[byteCount++]= 0x21; //LED Packet Signature.
			usbBuf[byteCount++]= 0x43; 
			usbBuf[byteCount++]= 0x65; 
			usbBuf[byteCount++]= 0x78; 
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


