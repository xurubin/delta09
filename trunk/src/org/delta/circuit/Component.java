package org.delta.circuit;

import java.util.ArrayList;

import org.delta.util.BidirectionalIntegerMap;
import org.delta.verilog.VerilogConverter;

abstract public class Component {
    protected BidirectionalIntegerMap<ComponentWire> inputMap;
    private BidirectionalIntegerMap<ComponentWire> outputMap;

    private ArrayList<GateInput> internalInputList;
    private ArrayList<Gate> internalOutputList;

    protected Circuit circuit = new Circuit();

    public Component(int inputCount, int outputCount) {
        inputMap = new BidirectionalIntegerMap<ComponentWire>(inputCount);
        outputMap = new BidirectionalIntegerMap<ComponentWire>(outputCount);
        
        internalInputList = new ArrayList<GateInput>(inputCount);
        internalOutputList = new ArrayList<Gate>(outputCount);
        
        // Initialise ArrayLists
        for (int i = 0; i < inputCount; ++i) {
            internalInputList.add(null);
        }
        for (int i = 0; i < outputCount; ++i) {
            internalOutputList.add(null);
        }
    }

    public void setInputWire(int inputNumber, ComponentWire wire) {
        inputMap.set(inputNumber, wire);
    }
    
    public void setOutputWire(int outputNumber, ComponentWire wire) {
        outputMap.set(outputNumber, wire);
    }

    public ComponentWire getInputWire(int inputNumber) {
        return inputMap.getEntry(inputNumber);
    }
    
    public ComponentWire getOutputWire(int outputNumber) {
        return outputMap.getEntry(outputNumber);
    }

    public final Circuit getCircuit() {
        return circuit;
    }

    public int getInputCount() {
        return inputMap.getSize();
    }

    public int getOutputCount() {
        return outputMap.getSize();
    }

    protected Gate getOutputGate(int outputNumber) {
        return internalOutputList.get(outputNumber);
    }
    
    protected void setOutputGate(int outputNumber, Gate gate) {
        if (outputNumber >= getOutputCount())
            throw new IllegalArgumentException("Output number out of bounds.");
        if (!circuit.containsVertex(gate)) {
            throw new IllegalArgumentException(
                "Gate is not part of the circuit"
            );
        }
        
        internalOutputList.set(outputNumber, gate);
    }
    
    protected Gate getInputGate(int inputNumber) {
        if (inputNumber >= getInputCount())
            throw new IllegalArgumentException("Input number out of bounds.");
        return internalInputList.get(inputNumber).getGate();
    }
    
    protected GateInput getGateInput(int inputNumber) {
        if (inputNumber >= getInputCount())
            throw new IllegalArgumentException("Input number out of bounds.");
        
        return internalInputList.get(inputNumber);
    }
    
    protected GateInput getGateInputSelector(ComponentWire wire) {
        Integer inputNumber = inputMap.getIndex(wire);

        return internalInputList.get(inputNumber);
    }
    
    protected void setInputGate(int inputNumber, Gate gate,
            int gateInputNumber) {
        if (inputNumber >= getInputCount()) {
            throw new IllegalArgumentException("Input number out of bounds.");
        }
        if (!circuit.containsVertex(gate)) {
            throw new IllegalArgumentException("Gate is part of the circuit.");
        }
        if (gateInputNumber < 0 || gateInputNumber >= gate.getInputCount()) {
            throw new IllegalArgumentException(
                "Input number of gate is out of bounds."
            );
        }
        
        GateInput gateInput = new GateInput(gate, gateInputNumber);
        internalInputList.set(inputNumber, gateInput);
    }
    
    protected class GateInput {

        private final Gate gate;
        private final int inputNumber;
        
        public GateInput(final Gate gate, final int inputNumber) {
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
    
    public String getVerilogMethod(String name, ArrayList<String> inputWires, ArrayList<String> outputWires) {
    	return VerilogConverter.convertToVerilog(name, getCircuit(), inputWires, outputWires);
    }

}