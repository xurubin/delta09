package org.delta.circuit;

import java.util.ArrayList;

import org.delta.logic.Formula;

abstract public class Gate {
    private int inputCount;
    private ArrayList<Wire> inputList;

    public Gate(int inputCount) {
        this.inputCount = inputCount;
        inputList = new ArrayList<Wire>(inputCount); 
    }
    
    abstract public Formula getFormula();
    
    public boolean setWire(Wire wire, int inputNumber) {
        return false;
    }
    
    public Wire getWire(int inputNumber) {
        return null;
    }
    
    public int getInputCount() {
        return inputCount;
    }
}
