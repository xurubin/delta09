package org.delta.circuit.gate;

import org.delta.circuit.Gate;
import org.delta.logic.Constant;
import org.delta.logic.Formula;
import org.delta.logic.And;

public class AndGate extends Gate {

    public AndGate(int inputCount) {
        super(inputCount);
        if (inputCount < 2)
            throw new IllegalArgumentException("Need at least two inputs");
    }

    @Override
    public Formula getFormula() {
        Formula and = new And(new Constant(getWire(0)),
                new Constant(getWire(1)));
        for (int i = 2; i < getInputCount(); i++) {
            Formula tmp = and;
            and = new And(tmp, new Constant(getWire(i)));
        }
        return and;
    }

}
