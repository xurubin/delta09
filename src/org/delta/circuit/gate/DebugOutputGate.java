package org.delta.circuit.gate;

import org.delta.logic.DebugConstant;
import org.delta.logic.Formula;

public class DebugOutputGate extends AbstractOutputGate {

    public DebugOutputGate() {
        super(1);
    }

    @Override
    public Formula getFormula() {
        return new DebugConstant(getWire(0));
    }

}
