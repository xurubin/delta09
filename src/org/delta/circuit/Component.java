package org.delta.circuit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.delta.util.BidirectionalIntegerMap;
import org.delta.verilog.VerilogConverter;

abstract public class Component implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BidirectionalIntegerMap<ComponentWire> inputMap;
    private BidirectionalIntegerMap<Set<ComponentWire>> outputMap;

    private ArrayList<Set<GateInputPort>> internalInputList;
    private ArrayList<Gate> internalOutputList;

    private Circuit circuit = new Circuit();

    public Component(int inputCount, int outputCount) {
        inputMap = new BidirectionalIntegerMap<ComponentWire>(inputCount);
        outputMap = new BidirectionalIntegerMap<Set<ComponentWire>>(outputCount);
        
        internalInputList = new ArrayList<Set<GateInputPort>>(inputCount);
        internalOutputList = new ArrayList<Gate>(outputCount);
        
        // Initialise ArrayLists
        for (int i = 0; i < inputCount; ++i) {
            internalInputList.add(new HashSet<GateInputPort>());
        }
        for (int i = 0; i < outputCount; ++i) {
            internalOutputList.add(null);
            outputMap.set(i, new HashSet<ComponentWire>());
        }
    }

    public void setInputWire(int inputNumber, ComponentWire wire) {
        inputMap.set(inputNumber, wire);
    }
    
    public void addOutputWire(int outputNumber, ComponentWire wire) {
        outputMap.getEntry(outputNumber).add(wire);
    }

    public ComponentWire getInputWire(int inputNumber) {
        return inputMap.getEntry(inputNumber);
    }
    
    public Set<ComponentWire> getOutputWires(int outputNumber) {
        return outputMap.getEntry(outputNumber);
    }
    

    public final Circuit getCircuit() {
        return circuit;
    }
    
    protected void setCircuit(Circuit circuit) {
        this.circuit = circuit;
    }

    public int getInputCount() {
        return inputMap.getSize();
    }

    public int getOutputCount() {
        return outputMap.getSize();
    }

    public Gate getOutputGate(int outputNumber) {
        return internalOutputList.get(outputNumber);
    }
   
    
    public ArrayList<Gate> getOutputGates() {
    	return internalOutputList;
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
    
    public List<Gate> getInputGates(int inputNumber) {
        if (inputNumber >= getInputCount())
            throw new IllegalArgumentException("Input number out of bounds.");
        Set<GateInputPort> gateInputPorts = internalInputList.get(inputNumber);
        
        List<Gate> inputGateList = new ArrayList<Gate>(gateInputPorts.size());
        for (GateInputPort gateInputPort: gateInputPorts) {
            inputGateList.add(gateInputPort.getGate());
        }
        
        return inputGateList;
    }
    
    public Set<GateInputPort> getGateInputPorts(int inputNumber) {
        if (inputNumber >= getInputCount())
            throw new IllegalArgumentException("Input number out of bounds.");
        
        return internalInputList.get(inputNumber);
    }
    
    public Set<GateInputPort> getGateInputPorts(ComponentWire wire) {
        Integer inputNumber = inputMap.getIndex(wire);

        return internalInputList.get(inputNumber);
    }
    
    protected void addInputGate(int inputNumber, Gate gate,
            int gateInputNumber) {
        if (inputNumber >= getInputCount()) {
            throw new IllegalArgumentException("Input number out of bounds.");
        }
        if (!circuit.containsVertex(gate)) {
            throw new IllegalArgumentException(
                    "Gate is not part of the circuit.");
        }
        if (gateInputNumber < 0 || gateInputNumber >= gate.getInputCount()) {
            throw new IllegalArgumentException(
                "Input number of gate is out of bounds."
            );
        }
        
        GateInputPort gateInputPort = new GateInputPort(gate, gateInputNumber);
        internalInputList.get(inputNumber).add(gateInputPort);
    }
    
    public class GateInputPort implements Serializable {

        private final Gate gate;
        private final int inputNumber;
        
        public GateInputPort(final Gate gate, final int inputNumber) {
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
    
    public String getVerilogMethod(String name,
            HashMap<ComponentWire,String> inputWires,
    		HashMap<ComponentWire,String> outputWires) {
    	return VerilogConverter.convertToVerilog(name, this, inputWires,
    	        outputWires);
    }

}