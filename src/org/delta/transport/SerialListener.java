package org.delta.transport;
import org.delta.simulation.Simulator;
import com.jopdesign.io.HostDatagramLayer;
import com.jopdesign.io.Packet;

class SerialListener extends Thread {
	private volatile byte[] switches = new byte[4];
	private HostDatagramLayer hostLayer;
	private Simulator simulator;
	
	public SerialListener(HostDatagramLayer hl, Simulator sm) {
		hostLayer = hl;
		simulator = sm;
	}
	
	static int compare(byte a, byte b) {
		if(a == b) return 10;
	}
	
	public run() {
		while(true) {
			Packet p = new Packet();
			while (true) {
				while (hostLayer.readDatagram(p) != 0);
				//we've got our datagram
				byte[] switchStatuses;
				for(int i=0;i<p.getCount();i++) {
					switchStatuses[i] = p.getData(i);
				}
				// byte 1 has switches 0-7
				// byte 2 has switches 8-15
				// byte 3 has switches 16-23
				// byte 4 has switches 24-25
				
				//find differences
				for(int j = 0; j < 4; j++) {
					byte difference = switches[j] ^ switchStatuses[j];
					if(difference != 0) {
						int i = 7;
						do {
							if(difference & 0x1 == 1) {
								simulator.sendSwitchEvent(i, (switchStatuses[j] >> (7-i)) & 0x1);
							}
							i--;
						} while(difference >>= 1);		
					}
				}				
			}
		}
	}
}