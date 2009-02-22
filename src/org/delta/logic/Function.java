package org.delta.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Function implements Serializable, Formula {
    private List<Formula> argumentList;
    private int argumentCount;
    
    public Function(int argumentCount) {
        this.argumentCount = argumentCount;
        argumentList = new ArrayList<Formula>(argumentCount);
        for (int i = 0; i < argumentCount; ++i) {
            argumentList.add(new Constant(State.SX));
        }
    }
    
    public Formula getArgument(int argumentNumber) {
        checkIndex(argumentNumber);
        return argumentList.get(argumentNumber);
    }
    
    public void setArgument(int argumentNumber, Formula argument) {
        checkIndex(argumentNumber);
        if (argument != null) {
            argumentList.set(argumentNumber, argument);
        }
    }
    
    public int getArgumentCount() {
        return argumentCount;
    }
    
    public State evaluateArgument(int argumentNumber) {
        checkIndex(argumentNumber);
        return getArgument(argumentNumber).evaluate();
    }
    
    private void checkIndex(int index) {
        if (index < 0 || index>= argumentCount) {
            throw new IllegalArgumentException("Argument number out of range.");
        }
    }
}
