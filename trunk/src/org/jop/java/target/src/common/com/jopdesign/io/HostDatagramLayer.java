package com.jopdesign.io;


import java.lang.Thread;
import jftd2xx.JFTD2XX;

public class HostDatagramLayer extends BaseDatagramLayer{
	private final int MAX_USBBUF = 64;
	private byte usbBuf[] = new byte[MAX_USBBUF];
	private JFTD2XX usb;

	public HostDatagramLayer(final int ms){
		try {
			usb  = new JFTD2XX(0);
			usb.closeConnection();
			usb.initConnection();
		} catch (Exception e){
			System.exit(1);
		}
		Thread pollingThread = new Thread(){
			public void run(){
				while(true){
					loop();
					try {
						Thread.sleep(ms);
					}catch (Exception e) {
						return;
					}
				}
			}
		};
		pollingThread.start();
	}
	
	/* copy from send buffer to serial buffer. called by handler */
	protected void send(){
		int byteCount = 1;
		
		synchronized(monitor){
//		write serial data
			int i = rdptTx;
			int j = wrptTx;
			while(i != j && byteCount<MAX_USBBUF){
				usbBuf[byteCount++] = (byte)txBuf[i];
				i = (i + 1)&BUF_MSK;
			}
			usbBuf[0] = (byte) (0x80 | (byteCount-1));
			try {
				if (byteCount > 1)
					if (usb.write(usbBuf, 0, byteCount)==byteCount)
						rdptTx = i;
			} catch(Exception e){}
		}
	}
	
	private byte readByteFromUSB(){
		usbBuf[0] = (byte) 0xC1;
		usbBuf[1] = 0;
		try {
			usb.write(usbBuf, 0, 2);
			if (1 == usb.read(usbBuf, 0, 1))
				return usbBuf[0];
			else 
				return END;
		}catch(Exception e){
			return END;
		}
	}
	/**
	 Reads from the serial buffer into the receive buffer. If more bytes are received, than the receive buffer can hold, the
	 packet will be truncated.
	 */
	protected void recv(){
		//synchronized(monitor){
//		read serial data
			if ( ((wrptRx + 1)&BUF_MSK) == rdptRx) //Full rxBuf
				return;
			int i = wrptRx;
			int j = rdptRx;
			while(((i + 1)&BUF_MSK) != j){
				rxBuf[i] = readByteFromUSB();
				//if (rxBuf[i] != 0)	System.out.print((char)rxBuf[i]);
				if ( (i != rdptRx)&&(rxBuf[(i-1)&BUF_MSK] == END)&&(rxBuf[i]==END) ) //Consecutive ENDs are not stored
					break; 
				else
					i = (i + 1)&BUF_MSK;
			}
			wrptRx = i;
			//System.out.println(wrptRx-rdptRx);
		}
	//}
	
}


