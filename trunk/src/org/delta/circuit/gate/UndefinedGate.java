package org.delta.circuit.gate;

import org.delta.logic.State;

public class UndefinedGate extends AbstractInputGate {
    private boolean firstEvaluation = true;

    public UndefinedGate() {
        setState(State.SX);
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
