/*
  This file is part of JOP, the Java Optimized Processor
    see <http://www.jopdesign.com/>

  Copyright (C) 2007, Alberto Andreotti

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

package gcinc;
public class SimpGC2 {

	
	public void createObjects() {
		int i;
		for(i=1;i<3*9730;i++) //make GC trigger 3 times (3*9730)
		{		
		allocateObject();
		}
		System.out.println(i);

	}

	public void allocateObject() {
		myObject mo;
		mo=new myObject();
	}

	public static void main(String[] args) {
		SimpGC2 sgc= new SimpGC2();
		sgc.createObjects();
	}

}
