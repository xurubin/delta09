package org.delta.logic;

public class Nand extends BinaryFunction {

    @Override
    public State evaluate() {
        switch(evaluateArg0()) {
        case S0: return State.S1;
        case S1: return (new Not(getArg1())).evaluate();
        case SX:
        default: return (evaluateArg1() == State.S0)? State.S1 : State.SX;
        }
    }

}
