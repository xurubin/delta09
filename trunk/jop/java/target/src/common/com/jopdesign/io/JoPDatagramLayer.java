/*

Adapted from Serial.java in ejip123 in the JoP Distribution


*/
package com.jopdesign.io;

import com.jopdesign.sys.Native;
import com.jopdesign.sys.Const;


public class JoPDatagramLayer extends BaseDatagramLayer {
	protected SerialPort sp;
	private int LEDStates = 0;
	private final int PACKET_LEN = 56;
	public JoPDatagramLayer(){
		sp = IOFactory.getFactory().getSerialPort();
	}


	public boolean readPacket(byte[] data) {
		while( sp.read()!= DATAGRAM_END);
		for(int i=0;i<PACKET_LEN/4;i++){
			int H = Native.rdMem(Const.IO_UART_SR+i);
			//DE2Peripheral.setHEX1Display(H,true);
			//DE2Peripheral.setHEX2Display(H>>16,true);
		
			data[i*4+3] = (byte)(H & 0xFF); H>>=8;
			data[i*4+2] = (byte)(H & 0xFF); H>>=8;
			data[i*4+1] = (byte)(H & 0xFF); H>>=8;
			data[i*4+0] = (byte)(H & 0xFF); 
		}
		
		while(!sp.txEmpty());
		sp.write(DATAGRAM_ACK);
		while(!sp.txEmpty());
		sp.write(0x00);
		while(!sp.txEmpty());
		return true;
	}
	
	/**
	 * 
	 * @param data no 0x00, ideally Top bit in each byte is 1
	 *  to distinguish between normal console outputs and packets
	 * @param len
	 * @return
	 */
	public boolean sendPacket(byte[]data, int len) {
		while(!sp.txEmpty());
		for(int i=0;i<len;i++){
			sp.write(data[i]);
			while(!sp.txEmpty());
		}
		return true;
	}

	public int readDatagram(byte[] data) {
 		assert(PACKET_LEN==56);
		byte b[] = new byte[PACKET_LEN];
		readPacket(b);
		int data_len = (((b[3]&0xFF)>>>1))<<21 |
					   (((b[2]&0xFF)>>>1))<<14 |
					   (((b[1]&0xFF)>>>1))<<7  | ((b[0]&0xFF)>>>1);
		DE2Peripheral.setHEX1Display(data_len,true);
		DE2Peripheral.setHEX2Display(data_len>>16,true);
		
		if ((b[4]!=DATAGRAM_LEN_HEADER)||(b[5]!=DATAGRAM_LEN_HEADER)||
				(b[6]!=DATAGRAM_LEN_HEADER)||(b[7]!=DATAGRAM_LEN_HEADER)) 
				return -1;
			
		for(int i=0;i<(data_len+48)/49;i++){
			readPacket(b);
			for(int j=0;j<7;j++)
				BytesShrink(b, 8*j, data, i*49+j*7);
		}
		return data_len;
	}
/////////////////////////////////////////////////////////////////////
	public int readLEDStates(){
		while( sp.read()!= DATAGRAM_END);
		int H = Native.rdMem(Const.IO_UART_SR);
		int L = Native.rdMem(Const.IO_UART_SR+1);
		DE2Peripheral.setHEX1Display(H,true);
		DE2Peripheral.setHEX2Display(H>>16,true);
	
		int states = 0;
		states =  ((byte)((H>>24) & 0xFF) - 1);
		states |= ((byte)((H>>16) & 0xFF) - 1)<<7;
		states |= ((byte)((H>>8 ) & 0xFF) - 1)<<14;
		states |= ((byte)((H    ) & 0xFF) - 1)<<21;
		if (L == 0x21436578)
			LEDStates = states;
		return LEDStates;
	}
	
	public int sendSwitchStates(int states){
		synchronized(monitor){
		int i = 0;
		byte d = 0;
		while(!sp.txEmpty());
		sp.write(0x00);
		for(i=0;i<4;i++){
			while(!sp.txEmpty());
			sp.write((byte)(0x80|(states&0x7F)));
			states >>= 7;
		}
		while(!sp.txEmpty());
		sp.write(0x00);
		while(!sp.txEmpty());
		return 0;
		}
	}
	
}

