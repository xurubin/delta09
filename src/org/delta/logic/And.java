package org.delta.logic;

public class And extends BinaryFunction {
    
    @Override
    public State evaluate() {
        switch (evaluateArg0()) {
        case S1: return evaluateArg1();
        case SX: return (evaluateArg1() == State.S0)? State.S0 : State.SX ;
        case S0:
        default: return State.S0;
        }
    }

}
