package test;

import com.jopdesign.io.DE2Peripheral;
import com.jopdesign.io.JoPDatagramLayer;
import joprt.RtThread;
/**
 * @author Rubin
 *
 * DE2 Daemon envorinment
 */
public class DE2_Daemon {

	public static void main(String[] args) {

		JoPDatagramLayer d = new JoPDatagramLayer(); //No polling thread
		RtThread.startMission();
		while (true) {
			DE2Peripheral.directLEDState(d.readLEDStates());
			d.sendSwitchStates(DE2Peripheral.directSwitchState());

			DE2Peripheral.setLEDRState(17, !DE2Peripheral.getLEDRState(17)); //Flash LED17 as an indicator.
		}

	}
}
