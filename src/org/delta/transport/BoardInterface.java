package org.delta.transport;
import com.jopdesign.io.HostDatagramLayer;

/**
 * Interface to USB serial link to DE2 board. 
 * Provides method to send LED changes to board. 
 * @author Group Delta 2009
 *
 */
class BoardInterface {
	
	private volatile int lights = 0;
	private HostDatagramLayer hostLayer;
	private SerialListener serialListener;
	private BoardInterface boardInterface;
	
	/**
	 * Constructs a new BoardInterface. The method is protected.
	 * @param simulator	a reference to the simulator object, so call-backs can be used.
	 * @see #getInstance()
	 */
	protected BoardInterface() {
		//start listener
		HostDatagramLayer hostLayer  = new HostDatagramLayer(1);
		serialListener = new SerialListener(hostLayer);
		serialListener.start();
		
	}
	
	/**
	 * Uses singleton pattern to return a single boardInterface.
	 * @return BoardInterface instance. 
	 * @see #BoardInterface()
	 */
	public BoardInterface getInstance() {
		if(boardInterface == null) boardInterface =  new BoardInterface();
		return boardInterface;
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
	
	/**
	 * Gets the status of the ith switch.
	 * @param i the switch number.
	 * @return the status of the switch.
	 */
	public boolean getSwitchStatus(int i) {
		int switches = serialListener.getSwitches();
		return (switches >> (31 - i)) == 1;
	}
}