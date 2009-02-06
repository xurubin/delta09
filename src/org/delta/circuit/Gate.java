package org.delta.circuit;

import java.util.ArrayList;

import org.delta.logic.Formula;

abstract public class Gate {
    /**
     * Number of inputs (i.e. wires) to the gate.
     */
    private int inputCount;
    private ArrayList<Wire> inputList;

    public Gate(int inputCount) {
        this.inputCount = inputCount;
        inputList = new ArrayList<Wire>(inputCount);

        // Initialise array.
        for (int i = 0; i < inputCount; i++)
            inputList.add(null);
    }

    abstract public Formula getFormula();

    public boolean setWire(Wire wire, int inputNumber) {
        if (inputNumber > inputCount -1) return false;

        inputList.set(inputNumber, wire);
        return true;
    }

    public Wire getWire(int inputNumber) {
        return inputList.get(inputNumber);
    }

    public int getInputCount() {
        return inputCount;
    }
}