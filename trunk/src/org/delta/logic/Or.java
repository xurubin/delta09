package org.delta.logic;

public class Or extends BinaryFunction {

    @Override
    public State evaluate() {
        switch (evaluateArg0()) {
        case S1: return State.S1;
        case SX: return (evaluateArg1() == State.S1)? State.S1 : State.SX;
        case S0:
        default: return evaluateArg1();
        }
    }

}
