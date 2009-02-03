package org.delta.transport;
import org.delta.simulation.Simulator;
import org.jop.common.jftd2xx.JFTD2XX;

class SerialListener extends Thread {
	private volatile boolean[] switches;
	private JFTD2XX usbInterface;
	private Simulator simulator;
	
	public SerialListener(JFTD2XX usb, Simulator sm) {
		usbInterface = usb;
		simulator = sm;
	}
	
	public run() {
		while(true) {
			read(/* arguments */);
			//on receipt of switch packet
			if(/* packet received is switch update packet */) {
				//compare to switches array
				if(/* change */) {
					StateChangeEvent event = new StateChangeEvent(/* */);
					simulator.addSwitchEvent(event);
				}
			}
			
			
		}
	}
}