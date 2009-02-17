package org.delta.logic;

public class Xor extends BinaryFunction {

    @Override
    public State evaluate() {
        switch (evaluateArg0()) {
        case S0: return evaluateArg1();
        case SX: return State.SX;
        case S1:
        default: return (new Not(getArg1())).evaluate();
        }
    }

}
