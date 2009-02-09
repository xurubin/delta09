package org.delta.transport;
import com.jopdesign.io.HostDatagramLayer;

/**
 * Interface to USB serial link to DE2 board. 
 * Provides thread to listen for changes to switches from board. 
 * @author Group Delta 2009
 *
 */

class SerialListener extends Thread {
   private volatile int switches = 0;
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
   

   /**
    * runs the SerialListener thread. 
    * Continuously listens for switch state packets.
    * @return void
    */
   public void run() {
	   while(true) {
		   int status = hostLayer.readSwitchStates();
		   if((status ^ switches) != 0) switches = status;
	   }
      }
}