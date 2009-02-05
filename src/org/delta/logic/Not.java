package org.delta.logic;

public class Not extends Formula {
    private Formula arg0 = new Constant(State.SX);

    public Not(Formula arg0) {
        if (arg0 != null)
            this.arg0 = arg0;
    }

    @Override
    public State evaluate() {
       switch (arg0.evaluate()) {
       case S0: return State.S1;
       case S1: return State.S0;
       default: return State.SX;
       }
    }

}
