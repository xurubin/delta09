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
   
   public void setLights(int p, boolean b) {
	   if(b) {
		   lights |= (1 << p);
	   }
	   else {
		   lights &= (lights - (1 << p));
	   }
   }
   

   /**
    * runs the SerialListener thread. 
    * Continuously listens for switch state packets.
    * @return void
    */
   public void run() {
	   while(true) {
		   //set lights
		   hostLayer.sendLEDStates(lights);
		   
		   //read switches
		   int status = hostLayer.readSwitchStates();
		   if(status != -1) switches = status; 
	   }
      }
}