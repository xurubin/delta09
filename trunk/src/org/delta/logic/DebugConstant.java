package org.delta.logic;

import org.delta.circuit.Wire;

public class DebugConstant extends Constant {

    public DebugConstant(State state) {
        super(state);
    }

    public DebugConstant(Wire wire) {
        super(wire);
    }

    @Override
    public State evaluate() {
        State result = super.evaluate();
        System.out.println("Debug: " + result + ".");
        return result;
    }

}
