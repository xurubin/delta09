package org.delta.circuit.gate;

import java.util.ArrayList;

import org.delta.circuit.Gate;
import org.delta.logic.Constant;
import org.delta.logic.Formula;
import org.delta.logic.Nor;

public class NorGate extends Gate {

    public NorGate() {
        super(2);
    }

    @Override
    public Formula getFormula() {
        return new Nor(new Constant(getWire(0)), new Constant(getWire(1)));
    }
    
    public String getVerilogMethod(String name, String out, ArrayList<String> in) {
    	return Gate.constructDefaultVerilogMethod("nor", name, out, in);
    }


}
