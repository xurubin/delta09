package org.delta.circuit.gate;

import org.delta.circuit.Gate;
import org.delta.logic.Constant;
import org.delta.logic.Formula;
import org.delta.logic.State;

public abstract class AbstractInputGate extends Gate {
    private State value = State.SX;

    public AbstractInputGate() {
        super(0);
    }

    public State getValue() {
        return value;
    }

    public void setValue(State value) {
        this.value = value;
    }

    @Override
    public Formula getFormula() {
        return new Constant(value);
    }

}
