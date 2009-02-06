package org.delta.circuit.gate;

import org.delta.circuit.Gate;
import org.delta.logic.Constant;
import org.delta.logic.Formula;
import org.delta.logic.Not;

public class InverterGate extends Gate {

    public InverterGate() {
        super(1);
    }

    @Override
    public Formula getFormula() {
        return new Not(new Constant(getWire(0)));
    }

}
