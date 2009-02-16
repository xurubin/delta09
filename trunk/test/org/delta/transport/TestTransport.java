package org.delta.transport;


public class TestTransport {
	public static void main(String args[]) throws InterruptedException {
		BoardInterface b = BoardInterface.getInstance();
		boolean bool = true;
		int i = 0;
		while(true) {
			b.sendLEDEvent(i, bool);
			bool = !bool;
			if(i++ > 16) {
				i = 0;
				bool = !bool;
			}
			//Thread.sleep(2L);
		}
	}
}
