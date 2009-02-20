package org.delta.circuit.gate;

import java.util.ArrayList;

import org.delta.circuit.Gate;
import org.delta.logic.Constant;
import org.delta.logic.Formula;
import org.delta.logic.Not;

public class InverterGate extends Gate {

    public InverterGate() {
        super(1);
    }

    @Override
    public Formula getFormula() {
        return new Not(new Constant(getWire(0)));
    }
    
    public String getVerilogMethod(String name, ArrayList<String> out,
            ArrayList<String> in) {
    	return Gate.constructDefaultVerilogMethod("not", name, out, in);
    }

}
