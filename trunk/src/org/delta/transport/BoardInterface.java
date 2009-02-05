package org.delta.transport;
import org.delta.simulation.StateChangeEvent;
import org.delta.simulation.Simulator;

class BoardInterface {
	
	
	public BoardInterface(Simulator simulator) {
		//start listener
		SerialListener s = new SerialListener(, hostLayer, simulator);
		HostDatagramLayer u = new HostDatagramLayer(1);
		s.start();
		
	}
	
	public sendEvent(StateChangeEvent event) {
		//translate StateChangeEvent into data
		
		//send state change packet. 
		int r = usbInterface.write(/* arguments */);
	}
}