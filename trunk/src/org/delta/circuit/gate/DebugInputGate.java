package org.delta.circuit.gate;

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

}
