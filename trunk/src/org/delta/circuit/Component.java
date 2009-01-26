package org.delta.circuit;

import java.util.ArrayList;

abstract public class Component extends Circuit {
    protected ArrayList<ComponentWire> inputList;
    protected ArrayList<GateInputSelector> inputMap;
    protected ArrayList<Gate> outputList;

    public Component(int inputCount, int outputCount) {
        inputList = new ArrayList<ComponentWire>(inputCount);
        inputMap = new ArrayList<GateInputSelector>(inputCount);
        outputList = new ArrayList<Gate>(outputCount);
    }
    
    public boolean setInput(ComponentWire w, int inputNumber) {
        return false;
    }
    
    public boolean getOutput(int outputNumber) {
        return false;
    }
}