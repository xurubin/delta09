package org.delta.circuit.gate;

import java.util.ArrayList;

import org.delta.logic.DebugConstant;
import org.delta.logic.Formula;

public class DebugOutputGate extends AbstractOutputGate {

    public DebugOutputGate() {
        super(1);
    }

    @Override
    public Formula getFormula() {
        return new DebugConstant(getWire(0));
    }
    
    public String getVerilogMethod(String name, String out, ArrayList<String> in) {
    	return name + ": assign PLACEHOLDER = " + in.get(0) + ";";
    }

}
