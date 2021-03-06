package org.delta.usb;

import java.util.Random;

import jftd2xx.JFTD2XX;

import com.jopdesign.io.HostDatagramLayer;

public class usb_test {

	public static void upload_test() {
		byte[] data = {0x26, 0x27, 0x26, 
					   (byte) 0x84, 0x00, 0x01, 0x00, 0x01,
					   0x1F};
		try {
			JFTD2XX usb = new JFTD2XX(0);
			usb.setLatencyTimer(5);

			usb.write(data, 0, 3);
			
			for (int i=0;i<100;i++)
				usb.write(data, 3, 5);
			Thread.sleep(200);
			usb.write(data, 8, 1);
			usb.close();

			System.out.println("Done");
		}catch (Exception e) {
			System.out.println(e);
		}
			
	}
	private static void sort(int[] a, int len){
		for(int i=0;i<len;i++)
			for(int j=i-1;j>0;j--)
				if (a[j]>a[j+1]) {
					int t = a[j];
					a[j] = a[j+1];
					a[j+1] = t;
		}
	}
	public static void send_serializedArray() {
		
		HostDatagramLayer u = new HostDatagramLayer();
		
		byte[] buf = new byte[1024*1024];
		Random r = new Random(System.currentTimeMillis());
		final int DATA_LEN =98;
		int[] data = new int[DATA_LEN];
while(true){
		int min = 0x7FFFFFFF;
		int max = 0;
		for(int i=0;i<DATA_LEN;i++) {
			data[i] = r.nextInt() &0xFFFF;
			if (data[i]>max) max = data[i];
			if (data[i]<min) min = data[i];
		}
		
		int serializedLen = HostDatagramLayer.serialiseIntArray(data, data.length, buf);
		int a[] = HostDatagramLayer.deserialiseIntArray(buf);
		sort(a, DATA_LEN);
		u.sendDatagram(buf,serializedLen);

		min = a[0]; max=a[a.length-1];
		System.out.printf("Max:%08x Min:%08x\n",max,min);
}
	}


	public static void main(String[] args) {
		int i = 0, j = 0;
		int LED =0, L;
		HostDatagramLayer u = new HostDatagramLayer();
		long t3 = System.currentTimeMillis();
		while (true) {
			i = (int)(System.currentTimeMillis());
			u.sendLEDHEXStates((i&0xF)<<22 | LED, j+((long)j<<28));
			L = u.readSwitchStates();
			if (L != -1) { LED = L;j++;}
			System.out.printf("FPS: %4.2f ",(double)j*1000.0/(double)(System.currentTimeMillis()-t3));
			System.out.print('\r');
		}
	}
}
 
