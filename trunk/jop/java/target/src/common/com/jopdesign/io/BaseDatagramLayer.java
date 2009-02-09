/*

Adapted from Serial.java in ejip123 in the JoP Distribution


*/
package com.jopdesign.io;

import com.jopdesign.io.Packet;


public abstract class BaseDatagramLayer{
//Packet Separator
protected static final int END = 0x00;
protected static final int ESC = 0xdb;
protected static final int ESC_END = 0xdc;
protected static final int ESC_ESC = 0xdd;
protected int translateEscape(int d) {
	return (d == ESC_END) ? END :
		   (d == ESC_ESC) ? ESC :
		   d;
}

protected static final int BUF_LEN = 128;
protected static final int BUF_MSK = 0x7f;


/** receive FIFO buffer. */
protected int[] rxBuf = null;
protected int rdptRx, wrptRx;

/** send FIFO buffer. */
protected int[] txBuf = null;
protected int rdptTx, wrptTx;


protected final Object monitor;

/** internal Packets */
protected Packet curRxPacket;
protected Packet curTxPacket;
protected int curTxPacketIndex;

public BaseDatagramLayer(){
	txBuf = new int[BUF_LEN];		// should be byte
	rdptTx = wrptTx = 0;
	rxBuf = new int[BUF_LEN];
	rdptRx = wrptRx = 0;
	monitor = new Object();

	curRxPacket = new Packet();
	curTxPacket = null;
	curTxPacketIndex = 0;
	
}

/** 
	main loop. Should be called periodically to retrieve data from
    serial port to receive buffer and send data from send buffer
    to remote side
*/
public void loop(){
	recv();
	send();
}


/** Retrieve a datagram from receive buffer.
return 0 - succeed
return 1 - bad packet dropped
return 2 - No complete packet yet
 */

public int readDatagram(Packet p){
synchronized(monitor){
	int i = rdptRx;
	int j = wrptRx;
	int data;
	while( i != j){
		data = rxBuf[i];
		//DE2Peripheral.directLEDState(data);
		if (data == END) {//End of Packet
			if (curRxPacket.isValid()) { //Good Packet
				p.copyFrom(curRxPacket);
				curRxPacket.clear();
				rdptRx = (i + 1)&BUF_MSK;
				return 0;
			} else { // Bad Packet
				curRxPacket.clear();
				rdptRx = (i + 1)&BUF_MSK;
				return 1;
			}
		}else if (data == ESC) { //Encounter a Escape character
			if (((i + 1)&BUF_MSK) != j) {// Next character is also available
				i = (i + 1)&BUF_MSK;
				if (!curRxPacket.appendByte_raw((byte)translateEscape(rxBuf[i]))){ //Append it to packet, fail means packet overflow
					curRxPacket.clear();
					rdptRx = i&BUF_MSK;
					return 1;
				}
			} else {//Character after escape is not available yet
				rdptRx = (i)&BUF_MSK;
 				return 2;//Have to break to leave the previous escape untouched for next call.
 			}
		}else { // Just a normal character, put it into the buffer
			curRxPacket.appendByte_raw((byte)data);
//DE2Peripheral.directLEDState(curRxPacket.buf[0]);
		}
				
		i = (i + 1)&BUF_MSK;
	}
	rdptRx = i;
	return 2;
}
}

/* copy datagram to send buffer. The real send will be handled be handler()
return 0 - successful
return 1 - buffer too small. Try resending later.
return -1 - Internal error.
*/
public int sendDatagram(Packet p){
	if (curTxPacket == null) {
			curTxPacket = p;
			curTxPacketIndex = 0;
	}
synchronized(monitor){
	
	int i = wrptTx;
	int j = rdptTx;
	int returnVal = 1;
	if (p.getCount()+1 >= ((j+BUF_MSK-i)&BUF_MSK))
		return 1;
	while((((i + 1)&BUF_MSK) != j) && (((i + 2)&BUF_MSK) != j)){ //Need 2 slots 
		// Put curTxPacket[index] into txBuf
		byte d = curTxPacket.getData(curTxPacketIndex);
		if (d == END || d == ESC) {
			if ( ((i + 2)&BUF_MSK) == j) {// Buffer too small
				returnVal = 1;
				break;
			} else {// Enough buffer. write escape sequences 
				txBuf[i] = ESC;
				i = (i + 1)&BUF_MSK;
				txBuf[i] = (d == ESC)? ESC_ESC : ESC_END;
				curTxPacketIndex++;
			}
		} else { // Just normal character
			txBuf[i] = d;
			curTxPacketIndex++;
		}
		
		i = (i + 1)&BUF_MSK;

		if (curTxPacketIndex == curTxPacket.getCount()+1) { //This packet is done
			curTxPacket = null;
			curTxPacketIndex = 0;
			returnVal = 0;
			txBuf[i] = END;
			i = (i + 1)&BUF_MSK;
			break;
		}
	}
	wrptTx = i;
	return returnVal;
}
}

/**
 copy from send buffer to serial buffer. called by handler 
 */
abstract protected void send();
/**
 Reads from the serial buffer into the receive buffer. If more bytes are received, than the receive buffer can hold, the
 packet will be truncated.
 */
abstract protected void recv();


}
