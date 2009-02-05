package org.delta.transport;
import org.delta.simulation.SimulationEvent;
import org.delta.simulation.Simulator;
import com.jopdesign.io.HostDatagramLayer;
import com.jopdesign.io.Packet;

class BoardInterface {
	
	private volatile byte[] lights;
	private HostDatagramLayer hostLayer;
	
	public BoardInterface(Simulator simulator) {
		//start listener
		HostDatagramLayer hostLayer  = new HostDatagramLayer(1);
		SerialListener s = new SerialListener(hostLayer, simulator);
		s.start();
		
	}
	
	public sendEvent(SimulationEvent event) {
		//compare state change event to lights boolean array.
		//send state change packet. 
		Packet p = new Packet();
		hostLayer.sendDatagram(p);
	}
}