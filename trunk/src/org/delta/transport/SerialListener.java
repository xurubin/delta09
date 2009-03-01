package org.delta.transport;
import com.jopdesign.io.HostDatagramLayer;

/**
 * Interface to USB serial link to DE2 board. 
 * Provides thread to listen for changes to switches from board. 
 * @author Group Delta 2009
 *
 */

class SerialListener extends Thread {
   private int switches = 0;
   private int lights = 0;
   private long hexs = 0L;
   private HostDatagramLayer hostLayer;

   /**
    * Constructs a new SerialListener.
    * @param hl a reference to the HostDatagramLayer that deals with the low level implementation of the USB interface.
    */
   protected SerialListener(HostDatagramLayer hl) {
      hostLayer = hl;
   }
   
   /**
    * returns status of switches as packed int.
    * @return integer. 
    */
   public int getSwitches() {
	   return switches;
   }
   
   public void reset() {
	   lights = 0;
	   hexs= 0l;
   }
   
   public void setLights(int p, boolean b) {
	   if(b) {
		   lights |= (1 << p);
	   }
	   else {
		   lights &= ~(1 << p);
	   }
   }
   
	public void setHEXs(int hexID, int segmentID,  boolean b) {
		if (b)
			hexs |= (1L << (7*hexID+segmentID));
		else
			hexs &= ~(1L << (7*hexID+segmentID));
	}

   /**
    * runs the SerialListener thread. 
    * Continuously listens for switch state packets.
    * @return void
    */
   public void run() {
	   while(true) {
		   //set lights

		   hostLayer.sendLEDHEXStates(lights, hexs);
		   
		   //read switches
		   int status = hostLayer.readSwitchStates();
		   if(status != -1) switches = status; 
	   }
      }
}