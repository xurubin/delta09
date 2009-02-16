package org.delta.transport;


public class TestTransport {
	public static void main(String args[]) throws InterruptedException {
		BoardInterface b = BoardInterface.getInstance();
		boolean bool = true;
		int i = 0;
		long t, t0 = System.currentTimeMillis();
		while(true) {
			b.sendLEDEvent((i+18) % 26, bool);
			
			t = (t0+100000 - System.currentTimeMillis()) / 10;
			b.sendHEXDigit(0, (int)(t % 10)); t /= 10;
			//if (t%10 == 0) i++;
			b.sendHEXDigit(1, (int)(t % 10)); t /= 10;
			b.sendHEXDigit(2, (int)(t % 10)); t /= 10;
			b.sendHEXDigit(3, (int)(t % 6)); t /= 6;
			b.sendHEXDigit(4, (int)(t % 10)); t /= 10;
			b.sendHEXDigit(5, (int)(t % 6)); t /= 6;
			b.sendHEXDigit(6, (int)(t % 10)); t /= 10;
			b.sendHEXDigit(7, (int)(t % 6)); t /= 6;
			if(i++ > 24) {
				i = 0;
				bool = !bool;
			}
			Thread.sleep(10L);
		}
	}
}
