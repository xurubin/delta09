package org.delta.circuit.gate;

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
}
