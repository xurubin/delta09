package org.delta.transport;
import com.jopdesign.io.HostDatagramLayer;

/**
 * Interface to USB serial link to DE2 board. 
 * Provides method to send LED changes to board. 
 * @author Group Delta 2009
 *
 */
public class BoardInterface {
	
	private HostDatagramLayer hostLayer;
	private SerialListener serialListener;
	private static BoardInterface boardInterface;
	
	/**
	 * Constructs a new BoardInterface. The method is protected.
	 * @param simulator	a reference to the simulator object, so call-backs can be used.
	 * @see #getInstance()
	 */
	protected BoardInterface() {
		//start listener
		hostLayer  = new HostDatagramLayer();
		serialListener = new SerialListener(hostLayer);
		serialListener.start();
		
	}
	
	/**
	 * Uses singleton pattern to return a single boardInterface.
	 * @return BoardInterface instance. 
	 * @see #BoardInterface()
	 */
	public static BoardInterface getInstance() {
		if(boardInterface == null) {
			boardInterface =  new BoardInterface();
		}
		return boardInterface;
	}
	
	/**
	 * Sends a packet to the DE2 board with the new status of the 7-segment led.
	 * @param hexID		the number of the HEX on the board.
	 * @param segmentID	the position of the target segment. See below.
	 *  -0--
	 * |    |1
	 * |5   |
	 *  --6-
	 * |    |2
	 * |4   |
	 *  --3-
	 *  @param b	true for on and false for off. 
	 */
	public void sendHEXEvent(int hexID, int segmentID,  boolean b) {
		serialListener.setHEXs(hexID,segmentID, b);
	}
	
	/**
	 * Convert	a digit(0..15) to the 7-segment led pattern as shown above. 
	 * @param 	Digit digit to be converted
	 * @return	hex pattern as a packed int
	 */
	private int digitToHexPattern(int Digit){
		switch(Digit) {
			case  0: return 0x3F;//011 1111
			case  1: return 0x06;//000 0110
			case  2: return 0x5B;//101 1011
			case  3: return 0x4F;//100 1111
			case  4: return 0x66;//110 0110
			case  5: return 0x6D;//110 1101
			case  6: return 0x7D;//111 1101
			case  7: return 0x07;//000 0111
			case  8: return 0x7F;//111 1111
			case  9: return 0x6F;//110 1111
			case 10: return 0x77;//111 0111
			case 11: return 0x7C;//111 1100
			case 12: return 0x39;//011 1001
			case 13: return 0x5E;//101 1110
			case 14: return 0x79;//111 1001
			case 15: return 0x71;//111 0001
			default: return 0x7F;//111 1111
		}
	}
	
	/**
	 * Let the specified 7-segment led display a digit pattern
	 * @param hexID ID of the target led
	 * @param digit Digit to be displayed
	 */
	public void sendHEXDigit(int hexID, int digit) {
		int b = digitToHexPattern(digit);
		for(int i=0;i<7;i++)
			sendHEXEvent(hexID, i, (b & (1<<i)) != 0);
	}
	
	/**
	 * Sends a packet to the DE2 board with the new status of the lights.
	 * @param i	the number of the LED on the board.
	 * @param b	the value to set that LED to.
	 */
	public void sendLEDEvent(int i, boolean b) {
		//compare state change event to lights packed int.
		serialListener.setLights(i,b);
	}
	
	public void reset() {
		serialListener.reset();
	}
	
	/**
	 * Gets the status of the ith switch.
	 * @param i the switch number.
	 * @return the status of the switch.
	 */
	public boolean getSwitchStatus(int i) {
		int switches = serialListener.getSwitches();
		return (((switches >> i) & 0x1) == 1);
	}
}