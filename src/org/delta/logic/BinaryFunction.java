package org.delta.logic;

public abstract class BinaryFunction extends Formula {
    private Formula arg0 = new Constant(State.SX);
    private Formula arg1 = new Constant(State.SX);

    public void setArg0(Formula arg0) {
        if (arg0 != null)
            this.arg0 = arg0;
    }
    public void setArg1(Formula arg1) {
        if (arg1 != null)
            this.arg1 = arg1;
    }
    
    State evaluateArg0() {
        return arg0.evaluate();
    }
    
    State evaluateArg1() {
        return arg1.evaluate();
    }
    
    Formula getArg0() {
        return arg0;
    }
    
    Formula getArg1() {
        return arg1;
    }
    
}
