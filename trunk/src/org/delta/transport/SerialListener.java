package org.delta.transport;
import org.delta.simulation.Simulator;
import org.jop.java.jop_usb.src.HostDatagramLayer;
import org.jop.java.jop_usb.src.Packet;

class SerialListener extends Thread {
	private volatile boolean[] switches;
	private HostDatagramLayer hostLayer;
	private Simulator simulator;
	
	public SerialListener(HostDatagramLayer hl, Simulator sm) {
		hostLayer = hl;
		simulator = sm;
	}
	
	public run() {
		while(true) {
			Packet p = new Packet();
			while (true) {
				while (hostLayer.readDatagram(p) != 0);
				for(int i=0;i<p.getCount();i++)
					System.out.print((char)p.getData(i));
				System.out.println();
				
			}
		}
	}
}