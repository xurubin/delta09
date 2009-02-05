package org.delta.circuit.gate;

import org.delta.circuit.Gate;
import org.delta.logic.Formula;

public abstract class AbstractOutputGate extends Gate {

    public AbstractOutputGate(int inputCount) {
        super(inputCount);
    }

}
