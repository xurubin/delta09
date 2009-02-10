/*
  This file is part of JOP, the Java Optimized Processor
    see <http://www.jopdesign.com/>

  Copyright (C) 2001-2008, Martin Schoeberl (martin@jopdesign.com)

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/*
 * Created on 28.02.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package test;

import com.jopdesign.io.DE2Peripheral;
import com.jopdesign.io.Packet;
import com.jopdesign.io.SerialPort;
import com.jopdesign.io.IOFactory;
import com.jopdesign.io.JoPDatagramLayer;
import com.jopdesign.sys.Native;
import com.jopdesign.sys.Const;
import joprt.RtThread;
/**
 * @author Rubin
 *
 * DE2 can also say 'Hello World'
 */
public class HelloDE2 {

	private static void sort(int[] a, int len){
		int t;
		for(int i=0;i<len;i++)
			for(int j=i-1;j>=0;j--)
				if (a[j]>a[j+1]) {
					t = a[j];
					a[j] = a[j+1];
					a[j+1] = t;
		}
	}
	public static void main(String[] args) {

		System.out.println("Hello, DE2!");

		DE2Peripheral.setLEDGState(0, true);
		DE2Peripheral.setLEDGState(1, true);
		DE2Peripheral.setLEDGState(2, true);
		DE2Peripheral.setLEDGState(3, true);

		JoPDatagramLayer d = new JoPDatagramLayer(0, 0);
		RtThread.startMission();
		byte datagram[] = new byte[16*1024];
		while (true) {
			DE2Peripheral.directLEDState(1);
			d.readDatagram(datagram);
			DE2Peripheral.directLEDState(2);
			int array[] = d.deserialiseIntArray(datagram);
			DE2Peripheral.directLEDState(4);
			sort(array, array.length);
			DE2Peripheral.directLEDState(8);
			
			DE2Peripheral.setHEX1Display(array[0],true);
			DE2Peripheral.setHEX2Display(array[array.length-1],true);

			DE2Peripheral.setLEDRState(17, !DE2Peripheral.getLEDRState(17));
			
		}
		
		/*
		byte data[] =new byte[56];
		while (true) {
			d.readPacket(data);
			DE2Peripheral.setLEDRState(17, !DE2Peripheral.getLEDRState(17));
		*/
			/*
			DE2Peripheral.directLEDState(d.readLEDStates());
		
			int sk = DE2Peripheral.directSwitchState();
			d.sendSwitchStates(sk);
			DE2Peripheral.setLEDRState(17, !DE2Peripheral.getLEDRState(17));
		}
			*/

	}
}
