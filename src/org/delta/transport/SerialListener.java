package org.delta.transport;
import org.delta.simulation.Simulator;
import com.jopdesign.io.HostDatagramLayer;
import com.jopdesign.io.Packet;

/**
 * Interface to USB serial link to DE2 board. 
 * Provides thread to listen for changes to switches from board. 
 * @author Group Delta 2009
 *
 */

class SerialListener extends Thread {
   private volatile int switches = 0;
   private HostDatagramLayer hostLayer;
   private Simulator simulator;

   /**
    * Constructs a new SerialListener.
    * @param hl a reference to the HostDatagramLayer that deals with the low level implementation of the USB interface.
    * @param sm a reference to the simulator for call-backs. 
    */
   public SerialListener(HostDatagramLayer hl, Simulator sm) {
      hostLayer = hl;
      simulator = sm;
   }

   /**
    * runs the SerialListener thread. 
    * Continuously listens for switch state packets, on receipt compares new switch states to previous to then send change events to the simulator. 
    * @return void
    */
   public void run() {
	   while(true) {
		   int status = hostLayer.readSwitchStates();
		   int diff = status ^ switches;
		   int i = 31;
		   do {
			   if((diff & 0x1) == 1) {
				   simulator.sendSwitchEvent(i, (status << i) == 1);
			   }
			   i--;
		   } while ((diff >>= 1) == 0);
	   }
      }
}