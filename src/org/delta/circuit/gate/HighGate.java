package org.delta.circuit.gate;

import org.delta.logic.Constant;
import org.delta.logic.Formula;
import org.delta.logic.State;

public class HighGate extends AbstractInputGate {

    @Override
    public Formula getFormula() {
        return new Constant(State.S1);
    }
}
