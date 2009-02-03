package org.delta.transport;
import org.delta.simulation.StateChangeEvent;
import org.delta.simulation.Simulator;
import org.jop.common.jftd2xx.JFTD2XX;

class BoardInterface {
	
	private JFTD2XX usbInterface = new JFTD2XX();
	
	public BoardInterface(Simulator simulator) {
		//start listener
		SerialListener s = new SerialListener(usbInterface, simulator);
		s.run();
		
	}
	
	public sendEvent(StateChangeEvent event) {
		//translate StateChangeEvent into data
		
		//send state change packet. 
		int r = usbInterface.write(/* arguments */);
	}
}