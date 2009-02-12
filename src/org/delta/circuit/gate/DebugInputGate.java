package org.delta.circuit.gate;

import java.util.ArrayList;

import org.delta.logic.State;

public class DebugInputGate extends AbstractInputGate {
    private boolean hasChanged = true;
    
    public void setState(State state) {
        if (state != getState()) {
            super.setState(state);
            hasChanged = true;
        }
    }

    @Override
    public boolean update() {
        if (hasChanged) {
            hasChanged = false;
            return true;
        }
        return false;
    }
    
    public String getVerilogMethod(String name, String out, ArrayList<String> in) {
    	return name + ": assign " + out + " = PLACEHOLDER;";
    }

}
