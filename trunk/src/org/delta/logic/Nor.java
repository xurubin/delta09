package org.delta.logic;

public class Nor extends BinaryFunction {

    @Override
    public State evaluate() {
        switch (evaluateArgument(0)) {
        case S1: return State.S0;
        case SX: return (evaluateArgument(1) == State.S1)? State.S0 : State.SX;
        case S0:
        default: return (new Not(getArgument(1))).evaluate();
        }
    }

}
