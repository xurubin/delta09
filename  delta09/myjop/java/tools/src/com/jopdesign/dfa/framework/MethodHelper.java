/*
  This file is part of JOP, the Java Optimized Processor
    see <http://www.jopdesign.com/>

  Copyright (C) 2008, Wolfgang Puffitsch

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

package com.jopdesign.dfa.framework;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;

public class MethodHelper {

	public static int getArgSize(InvokeInstruction m, ConstantPoolGen cp) {
		int retval = 0;
		if (!(m instanceof INVOKESTATIC)) {
			retval += 1;
		}
        Type at[] = m.getArgumentTypes(cp);
        for (int i = 0; i < at.length; ++i) {
        	retval += at[i].getSize();
        }
        return retval;
	}

	public static int getArgSize(MethodGen m) {
		int retval = 0;
		if (!m.isStatic()) {
			retval += 1;
		}	
        Type at[] = m.getArgumentTypes();
        for (int i = 0; i < at.length; ++i) {
        	retval += at[i].getSize();
        }
        return retval;
	}

}
