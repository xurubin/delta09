package org.delta.circuit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.delta.util.BidirectionalIntegerMap;
import org.delta.util.Pair;
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
        outputMap =
            new BidirectionalIntegerMap<Set<ComponentWire>>(outputCount);
        
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
    
    protected void fromComponentGraph(ComponentGraph graph,
            Set<ComponentPort> inputs,
            Set<ComponentPort> outputs,
            Set<ComponentPort> clockInputs) {
    }
    
    protected void fromComponentGraph(ComponentGraph graph,
            Set<ComponentPort> inputs,
            Set<ComponentPort> outputs) {
        Set<ComponentPort> clockedInputs = new HashSet<ComponentPort>();
        for (Component component: graph.vertexSet()) {
            if (component instanceof ClockedComponent) {
                clockedInputs.add(new ComponentPort(component, 0));
            }
        }
        fromComponentGraph(graph, inputs, outputs, clockedInputs);
    }

    public void setInputWire(int inputNumber, ComponentWire wire) {
        inputMap.set(inputNumber, wire);
    }
    
    public void removeInputWire(ComponentWire wire) {
        if (inputMap.containsEntry(wire)) {
            inputMap.remove(wire);
        }
    }
    
    public void addOutputWire(int outputNumber, ComponentWire wire) {
        outputMap.getEntry(outputNumber).add(wire);
    }
    
    public void removeOutputWire(ComponentWire wire) {
        for (int i = 0; i < outputMap.getSize(); ++i) {
            Set<ComponentWire> set = outputMap.getEntry(i);
            if (set.contains(wire)) {
                set.remove(wire);
                return;
            }
        }
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
            inputGateList.add(gateInputPort.gate);
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
    
    public class GateInputPort extends Pair<Gate, Integer> {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        public final Gate gate;
        public final Integer inputNumber;
        
        public GateInputPort(final Gate gate, final int inputNumber) {
            super(gate, inputNumber);
            this.gate = first;
            this.inputNumber = second;
        }
    }
    
    public class ComponentPort extends Pair<Component, Integer> {
        public final Integer portNumber;
        public final Component component;

        public ComponentPort(final Component first, final Integer second) {
            super(first, second);
            component = this.first;
            portNumber = this.second;
        }
    }
    
    public String getVerilogMethod(String name,
            HashMap<ComponentWire,String> inputWires,
            HashMap<ComponentWire,String> outputWires) {
        return VerilogConverter.convertToVerilog(name, this, inputWires,
                outputWires);
    }

}