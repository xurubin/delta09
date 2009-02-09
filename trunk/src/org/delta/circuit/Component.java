package org.delta.circuit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

abstract public class Component {
    private int inputCount;
    private int outputCount;

    private ArrayList<ComponentWire> inputList;

    // FIXME: Get rid of protected fields.
    // FIXME: inputMap should be a map from Integer to GateInputSelector.
    protected Map<Integer, Gate> inputMap;
    protected ArrayList<Gate> outputList;
    protected Circuit circuit = new Circuit();

    public Component(int inputCount, int outputCount) {
        this.inputCount = inputCount;
        this.outputCount = outputCount;
        inputList = new ArrayList<ComponentWire>(inputCount);
        inputMap = new HashMap<Integer, Gate>(inputCount);
        outputList = new ArrayList<Gate>(outputCount);

        // Initialise arrays.
        for (int i = 0; i < inputCount; ++i) {
            inputList.add(null);
        }
        for (int i = 0; i < outputCount; ++i) {
            outputList.add(null);
        }
    }

    public boolean setInputWire(ComponentWire wire, int inputNumber) {
        if (inputNumber >= inputCount)
            throw new IllegalArgumentException("Input number out of bounds.");

        inputList.set(inputNumber, wire);
        return true;
    }

    public ComponentWire getInputWire(int inputNumber) {
        if (inputNumber >= inputCount)
            throw new IllegalArgumentException("Input number out of bounds.");

        return inputList.get(inputNumber);
    }

    final Circuit getCircuit() {
        return circuit;
    }

    public int getInputCount() {
        return inputCount;
    }

    public int getOutputCount() {
        return outputCount;
    }

    public Gate getOutputGate(int outputNumber) {
        if (outputNumber >= outputCount)
            throw new IllegalArgumentException("Input number out of bounds.");
        
        return outputList.get(outputNumber);
    }
    
    public Gate getInputGate(int inputNumber) {
        if (inputNumber >= inputCount)
            throw new IllegalArgumentException("Input number out of bounds.");
        
        return inputMap.get(inputNumber);
    }
    
    class GateInputSelector {

        private final Gate gate;
        private final int inputNumber;
        
        public GateInputSelector(final Gate gate, final int inputNumber) {
            this.gate = gate;
            this.inputNumber = inputNumber;
        }
        
        public Gate getGate() {
            return gate;
        }
        
        public int getInputNumber() {
            return inputNumber;
        }
    }
    
    abstract public String getVerilogMethod();
    
    abstract public ComponentWire getOutputWire(int outputNumber);

}