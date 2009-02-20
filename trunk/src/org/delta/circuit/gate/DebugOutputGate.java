package org.delta.circuit.gate;

import java.util.ArrayList;

import org.delta.logic.DebugConstant;
import org.delta.logic.Formula;

public class DebugOutputGate extends AbstractOutputGate {
    private String name;

    public DebugOutputGate() {
        this("");
    }
    
    public DebugOutputGate(String name) {
        super(1);
        
        this.name = name;
    }

    @Override
    public Formula getFormula() {
        return new DebugConstant(getWire(0), name);
    }
    
    public String getVerilogMethod(String name, String out, ArrayList<String> in) {
    	return name + ": assign PLACEHOLDER = " + in.get(0) + ";";
    }

}
