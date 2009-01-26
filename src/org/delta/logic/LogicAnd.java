package org.delta.logic;

public class LogicAnd extends Formula {
    private Formula arg0 = new Constant(State.SX);
    private Formula arg1 = new Constant(State.SX);

    public LogicAnd(Formula arg0, Formula arg1) {
        if (arg0 != null)
            this.arg0 = arg0;
        if (arg1 != null)
            this.arg1 = arg1;
    }
    
    @Override
    public State evaluate() {
        State stateArg0 = arg0.evaluate();
             if (stateArg0 == State.SX) return State.SX;
        else if (stateArg0 == State.S0) return State.S0;
        else                            return arg1.evaluate();
    }

}
