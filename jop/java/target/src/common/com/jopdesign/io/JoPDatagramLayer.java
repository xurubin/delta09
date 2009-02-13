/*

Adapted from Serial.java in ejip123 in the JoP Distribution


*/
package com.jopdesign.io;

import com.jopdesign.sys.Native;
import com.jopdesign.sys.Const;


public class JoPDatagramLayer extends BaseDatagramLayer {
	protected SerialPort sp;
	private int LEDStates = 0;
	private int HEX1States = 0;
	private int HEX2States = 0;

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
	public void readLEDHEXStates(){
//DE2Peripheral.setHEX1Display(1,true);
		while( sp.read()!= DATAGRAM_END);
//DE2Peripheral.setHEX1Display(2,true);
		int H = Native.rdMem(Const.IO_UART_SR);
		if (H != 0x21436578)
			return;

//DE2Peripheral.setHEX2Display(H>>16,true);
	
		int states = 0;
		H = Native.rdMem(Const.IO_UART_SR+1);
		states =  ((byte)(((H>>>24) & 0xFF) - 4));
		states |= ((byte)(((H>>>16) & 0xFF) - 4))<<7;
		states |= ((byte)(((H>>>8 ) & 0xFF) - 4))<<14;
		states |= ((byte)(((H    ) & 0xFF) - 4))<<21;
		LEDStates = states;

		H = Native.rdMem(Const.IO_UART_SR+2);
		states =  ((byte)(((H>>>24) & 0xFF) - 4));
		states |= ((byte)(((H>>>16) & 0xFF) - 4))<<7;
		states |= ((byte)(((H>>>8 ) & 0xFF) - 4))<<14;
		states |= ((byte)(((H    ) & 0xFF) - 4))<<21;
		HEX1States = states;

		H = Native.rdMem(Const.IO_UART_SR+3);
		states =  ((byte)(((H>>>24) & 0xFF) - 4));
		states |= ((byte)(((H>>>16) & 0xFF) - 4))<<7;
		states |= ((byte)(((H>>>8 ) & 0xFF) - 4))<<14;
		states |= ((byte)(((H    ) & 0xFF) - 4))<<21;
		HEX2States = states;

//DE2Peripheral.setHEX1Display(3,true);
	}
	
	public int getLEDStates(){
		return LEDStates;
	}

	public int getHEX1States(){
		return HEX1States;
	}

	public int getHEX2States(){
		return HEX2States;
	}
	
	public int sendSwitchStates(int states){ 
		int i = 0;
		byte d = 0;
//DE2Peripheral.setHEX1Display(4,true);
		while(!sp.txEmpty());
//DE2Peripheral.setHEX1Display(5,true);
		sp.write(0x00);
		for(i=0;i<4;i++){
//DE2Peripheral.setHEX1Display(6,true);
			while(!sp.txEmpty());
//DE2Peripheral.setHEX1Display(7,true);
				sp.write((byte)(0x80|(states&0x7F))); 
				states >>= 7;
		}
//DE2Peripheral.setHEX1Display(8,true);
		while(!sp.txEmpty());
//DE2Peripheral.setHEX1Display(9,true);
		sp.write(0x00);
//DE2Peripheral.setHEX1Display(10,true);
		while(!sp.txEmpty());
//DE2Peripheral.setHEX1Display(11,true);
		return 0;
	}
	
}

