package org.delta.logic;

public class Nor extends BinaryFunction {

    @Override
    public State evaluate() {
        switch (evaluateArg0()) {
        case S1: return State.S0;
        case SX: return (evaluateArg1() == State.S1)? State.S0 : State.SX;
        case S0:
        default: return (new Not(getArg1())).evaluate();
        }
    }

}
