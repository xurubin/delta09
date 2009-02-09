package org.delta.circuit.gate;

import org.delta.logic.State;

public class HighGate extends AbstractInputGate {
    private boolean firstEvaluation = true;

    public HighGate() {
        setState(State.S1);
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
