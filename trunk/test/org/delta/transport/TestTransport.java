package org.delta.transport;

public class TestTransport {
	public static void main(String args[]) {
		BoardInterface b = BoardInterface.getInstance();
		while(true) {
			for(int i = 0; i <= 17; i++) {
				b.sendLEDEvent(i, b.getSwitchStatus(i));
			}
			try {
				Thread.sleep(1L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
}
