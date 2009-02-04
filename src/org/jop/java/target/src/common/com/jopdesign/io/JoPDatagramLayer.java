/*

Adapted from Serial.java in ejip123 in the JoP Distribution


*/
package com.jopdesign.io;

import com.jopdesign.io.Packet;
import com.jopdesign.sys.Native;
import joprt.RtThread;


public class JoPDatagramLayer extends BaseDatagramLayer{
	protected SerialPort sp;

	public JoPDatagramLayer(int prio, int us){
		
		sp = IOFactory.getFactory().getSerialPort();

		new RtThread(prio, us){
			public void run(){
				for(; ;){
					waitForNextPeriod();
					loop();
				}
			}
		};

	}

	/* copy from send buffer to serial buffer. called by handler */
	protected void send(){
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
		}
	}
	/**
	 Reads from the serial buffer into the receive buffer. If more bytes are received, than the receive buffer can hold, the
	 packet will be truncated.
	 */
	protected void recv(){
		synchronized(monitor){
//		read serial data
			int i = wrptRx;
			int j = rdptRx;
			while(sp.rxFull() && ((i + 1)&BUF_MSK) != j){
				rxBuf[i] = sp.read();
				if ( (i != rdptRx)&&(rxBuf[(i-1)&BUF_MSK] == END)&&(rxBuf[i]==END) ) //Consecutive ENDs are not stored
				;
				else
					i = (i + 1)&BUF_MSK;
			}
			wrptRx = i;
			if (rdptRx != wrptRx) {
				DE2Peripheral.directHEX1State(rdptRx);
				DE2Peripheral.directHEX2State(wrptRx);
			}
			//System.out.println(wrptRx-rdptRx);
		}
	}
}

