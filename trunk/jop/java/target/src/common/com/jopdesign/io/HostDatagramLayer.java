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
			close();
			open();
			usb.setLatencyTimer(2);
		} catch (Exception e){
			System.exit(1);
		}
		if (ms > 0){
			Thread pollingThread = new Thread(){
				public void run(){
					while(true){
						//recv();
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
	/* copy from send buffer to serial buffer. called by handler */
	public void send(){
		synchronized(monitor){
//		write serial data
			int byteCount = 1;
			int i = rdptTx;
			int j = wrptTx;
			while(i != j && byteCount<MAX_USBBUF){
				usbBuf[byteCount++] = (byte)txBuf[i];
				i = (i + 1)&BUF_MSK;
			}
			usbBuf[0] = (byte) (0x80 | (byteCount-1));
			try {
				if (byteCount > 1)
					if (usb.write(usbBuf, 0, byteCount)==byteCount){
						rdptTx = i;
						//System.out.printf("%d - %d\n", rdptTx, wrptTx);
						//System.out.printf("(%d)%02x %02x %02x\n",byteCount, usbBuf[0], usbBuf[1], usbBuf[2]);
					}else
						;//System.out.println("send() error.");
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
	public void recv(){
		synchronized(monitor){
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
	}

//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
	public int readSwitchStates(){
		synchronized(monitor){
			//System.out.printf("%02x",data);
			int count = 0;
			int states = -1;
			int data;
			while (count < 4) { //Invalid Packet
				states = 0;
				while(  (data = readByteFromUSB()) == END);
				while(data != END){
					states = (states>>7)| ((data&0x7F)<<21);
					//System.out.printf("%02x",(byte)data);
					data = readByteFromUSB();
					count++;
				}
				//System.out.printf("%02x %08x\n",(byte)data, states);
			}
			return states;
		}
	}
	
	public int sendLEDStates(int States){
		synchronized(monitor){
			int byteCount = 1;

			usbBuf[byteCount++]= ((byte)0xAA); //LED Packet Signature. in sc_uart_jtag.vhdl
			usbBuf[byteCount++]= ((byte)((States&0x7F)+1)); States >>= 7;
			usbBuf[byteCount++]= ((byte)((States&0x7F)+1)); States >>= 7;
			usbBuf[byteCount++]= ((byte)((States&0x7F)+1)); States >>= 7;
			usbBuf[byteCount++]= ((byte)((States&0x7F)+1)); 
			usbBuf[byteCount++]= 0x21; //Paddings
			usbBuf[byteCount++]= 0x43; //Paddings
			usbBuf[byteCount++]= 0x65; //Paddings
			usbBuf[byteCount++]= 0x77; //Paddings
			usbBuf[byteCount++]= 0x00; //Termination




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
	
	
}


