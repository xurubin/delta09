/*

Adapted from Serial.java in ejip123 in the JoP Distribution


*/
package com.jopdesign.io;

import com.jopdesign.io.Packet;
import com.jopdesign.sys.Native;
import com.jopdesign.sys.Const;
import joprt.RtThread;


public class JoPDatagramLayer extends BaseDatagramLayer{
	protected SerialPort sp;

	public JoPDatagramLayer(int prio, int us){
		
		sp = IOFactory.getFactory().getSerialPort();

		if (us > 0) {
			new RtThread(prio, us){
				public void run(){
					for(; ;){
						waitForNextPeriod();
						loop();
					}
				}
			};
		}
	}

	/* copy from send buffer to serial buffer. called by handler */
	public void send(){
		synchronized(monitor){
//		write serial data
			int i = rdptTx;
			int j = wrptTx;
			while(sp.txEmpty() && i != j){
				// FIXME deadlock in simulator under windows if there is no slip connection established
				sp.write(txBuf[i]);
				i = (i + 1)&BUF_MSK;
			}
			rdptTx = i;
			//DE2Peripheral.setHEX1Display((wrptTx+BUF_MSK-rdptTx)&BUF_MSK);
		}
	}

	
	/**
	 Reads from the serial buffer into the receive buffer. If more bytes are received, than the receive buffer can hold, the
	 packet will be truncated.
	 */
	public void recv(){
		synchronized(monitor){
//		read serial data
			int i = wrptRx;
			int j = rdptRx;
			while(sp.rxFull() && ((i + 1)&BUF_MSK) != j){
				rxBuf[i] = sp.read();
				if (i >0)
				DE2Peripheral.setHEX2Display(rxBuf[i-1]*100+rxBuf[i], false);
				if ( (i != rdptRx)&&(rxBuf[(i-1)&BUF_MSK] == END)&&(rxBuf[i]==END) ) //Consecutive ENDs are not stored
				;
				else
					i = (i + 1)&BUF_MSK;
			}
			wrptRx = i;
			//DE2Peripheral.setHEX1Display(wrptRx, false);
			//DE2Peripheral.setHEX2Display(rdptRx);
			//System.out.println(wrptRx-rdptRx);
		}
	}
	
	
/////////////////////////////////////////////////////////////////////
	public int readLEDStates(){
		//while(!sp.rxFull());
		//sp.read();
		while( sp.read()!= 0);
		int L = Native.rdMem(Const.IO_UART_SR_L);
		int H = Native.rdMem(Const.IO_UART_SR_H);
		DE2Peripheral.setHEX1Display(H,true);
		DE2Peripheral.setHEX2Display(H>>16,true);
	
		int states = 0;
		states =  ((byte)((H>>24) & 0xFF) - 1);
		states |= ((byte)((H>>16) & 0xFF) - 1)<<7;
		states |= ((byte)((H>>8 ) & 0xFF) - 1)<<14;
		states |= ((byte)((H    ) & 0xFF) - 1)<<21;
		return states;
	}
	
	public int sendSwitchStates(int states){
		synchronized(monitor){
		int i = 0;
		byte d = 0;
		while(!sp.txEmpty());
		sp.write(END);
		for(i=0;i<4;i++){
			while(!sp.txEmpty());
			sp.write((byte)(0x80|(states&0x7F)));
			states >>= 7;
		}
		while(!sp.txEmpty());
		sp.write(END);
		while(!sp.txEmpty());
		return 0;
		}
	}
	
}

