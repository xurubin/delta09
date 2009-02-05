package org.delta.logic;

public class Nor extends Formula {
    private Formula arg0 = new Constant(State.SX);
    private Formula arg1 = new Constant(State.SX);

    public Nor(Formula arg0, Formula arg1) {
        if (arg0 != null)
            this.arg0 = arg0;
        if (arg1 != null)
            this.arg1 = arg1;
    }

    @Override
    public State evaluate() {
        return (new Not(new Or(arg0, arg1))).evaluate();
    }

}
