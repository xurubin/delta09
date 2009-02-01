/*
 * Copyright (c) 2007,2008, Stefan Hepp
 *
 * This file is part of JOPtimizer.
 *
 * JOPtimizer is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * JOPtimizer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jopdesign.libgraph.cfg.statements.stack;

import com.jopdesign.libgraph.cfg.statements.common.ArraylengthStmt;
import com.jopdesign.libgraph.cfg.statements.quad.QuadArrayLength;
import com.jopdesign.libgraph.cfg.statements.quad.QuadStatement;
import com.jopdesign.libgraph.cfg.variable.Variable;
import com.jopdesign.libgraph.cfg.variable.VariableTable;
import com.jopdesign.libgraph.struct.ConstantValue;
import com.jopdesign.libgraph.struct.type.TypeInfo;

/**
 * @author Stefan Hepp, e0026640@student.tuwien.ac.at
 */
public class StackArrayLength extends ArraylengthStmt implements StackStatement,StackAssign {

    public StackArrayLength() {
    }

    public TypeInfo[] getPopTypes() {
        return new TypeInfo[] { TypeInfo.CONST_OBJECTREF };
    }

    public TypeInfo[] getPushTypes() {
        return new TypeInfo[] { TypeInfo.CONST_INT };
    }

    public QuadStatement[] getQuadCode(TypeInfo[] stack, VariableTable varTable) {
        Variable var = varTable.getDefaultStackVariable(stack.length - 1);
        return new QuadStatement[] { new QuadArrayLength(var, var) };
    }

    public int getOpcode() {
        return 0xbe;
    }

    public int getBytecodeSize() {
        return 1;
    }

    public String getCodeLine() {
        return "arraylength";
    }

    public ConstantValue[] getConstantValues(ConstantValue[] input) {
        return null;
    }
}
