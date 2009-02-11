package org.delta.circuit;

import java.util.ArrayList;

import org.delta.logic.Formula;
import org.delta.utils.BidirectionalIntegerMap;

abstract public class Gate {
    private BidirectionalIntegerMap<Wire> inputMap;

    public Gate(int inputCount) {
        inputMap = new BidirectionalIntegerMap<Wire>(inputCount);
    }

    abstract public Formula getFormula();

    public void setWire(Wire wire, int inputNumber) {
        inputMap.set(inputNumber, wire);
    }
    
    public int removeWire(Wire wire) {
        return inputMap.remove(wire);
    }

    public Wire getWire(int inputNumber) {
        return inputMap.getEntry(inputNumber);
    }

    public int getInputCount() {
        return inputMap.getSize();
    }
    
}
