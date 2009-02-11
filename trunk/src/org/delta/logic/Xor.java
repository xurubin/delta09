package org.delta.logic;

public class Xor extends Formula {
    private Formula arg0 = new Constant(State.SX);
    private Formula arg1 = new Constant(State.SX);

    public Xor(Formula arg0, Formula arg1) {
        if (arg0 != null)
            this.arg0 = arg0;
        if (arg1 != null)
            this.arg1 = arg1;
    }

    @Override
    public State evaluate() {
        switch (arg0.evaluate()) {
        case S0: return arg1.evaluate();
        case SX: return State.SX;
        case S1:
        default: return (new Not(arg1)).evaluate();
        }
    }

}
