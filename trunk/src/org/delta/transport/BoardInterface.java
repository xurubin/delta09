package org.delta.transport;
import org.delta.simulation.Simulator;
//import com.jopdesign.io.HostDatagramLayer;
//import com.jopdesign.io.Packet;


/**
 * Interface to USB serial link to DE2 board. 
 * Provides method to send LED changes to board. 
 * @author Group Delta 2009
 *
 */
class BoardInterface {
	
	private volatile int lights = 0;
	private HostDatagramLayer hostLayer;
	
	/**
	 * Constructs a new BoardInterface.
	 * @param simulator	a reference to the simulator object, so call-backs can be used.
	 */
	public BoardInterface(Simulator simulator) {
		//start listener
		HostDatagramLayer hostLayer  = new HostDatagramLayer(1);
		SerialListener s = new SerialListener(hostLayer, simulator);
		s.start();
		
	}
	/**
	 * Sends a packet to the DE2 board with the new status of the lights.
	 * @param i	the number of the LED on the board.
	 * @param b	the value to set that LED to.
	 * @return 1 if successful. 
	 */
	public int sendLEDEvent(int i, boolean b) {
		//compare state change event to lights packed int.
		int status = (b ? 1 : 0) << (31 - i);
		status |= lights;
		//send LED state packet. 
		return hostLayer.sendLEDStates(status);
	}
}