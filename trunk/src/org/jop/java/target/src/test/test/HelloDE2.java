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
import com.jopdesign.sys.Native;
/**
 * @author Rubin
 *
 * DE2 can also say 'Hello World'
 */
public class HelloDE2 {

	private static void displayMem(int addr) {
		int val = Native.rd(addr);
		System.out.println("Addr: " + addr + " = "+val);
	}
	public static void main(String[] args) {

		System.out.println("Hello, DE2!");

		DE2Peripheral.setLEDGState(0, true);
		DE2Peripheral.setLEDGState(1, true);
		DE2Peripheral.setLEDGState(2, true);
		DE2Peripheral.setLEDGState(3, true);

		displayMem(0);
		displayMem(1);
		displayMem(64*1024);
		displayMem(64*1024+1);
		displayMem(128*1024);
		displayMem(128*1024+1);
		displayMem(256*1024);
		displayMem(256*1024+1);
		displayMem(512*1024);
		displayMem(512*1024+1);
	}
}
