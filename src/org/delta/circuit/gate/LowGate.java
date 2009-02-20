package org.delta.circuit.gate;

import java.util.ArrayList;

import org.delta.logic.State;

public class LowGate extends AbstractInputGate {
    private boolean firstEvaluation = true;

    public LowGate() {
        setState(State.S0);
    }

    @Override
    public boolean update() {
        if (firstEvaluation) {
            firstEvaluation = false;
            return true;
        }
        return false;
    }
    
    public String getVerilogMethod(String name, ArrayList<String> out,
            ArrayList<String> in) {
    	String result = "";
    	for(String s : out) {
    		result += "assign " + s + " = 0;\n"; 
    	}
    	return result.substring(0, result.length() - 1);
    }
}
