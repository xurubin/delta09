package org.delta.logic;

public class And extends BinaryFunction {
    
    @Override
    public State evaluate() {
        switch (evaluateArgument(0)) {
        case S1: return evaluateArgument(1);
        case SX: return (evaluateArgument(1) == State.S0)? State.S0 : State.SX;
        case S0:
        default: return State.S0;
        }
    }

}
