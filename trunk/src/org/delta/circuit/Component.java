package org.delta.circuit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
    
    public Component(ComponentGraph graph, List<Set<ComponentPort>> inputs,
            List<ComponentPort> outputs) {
        this(inputs.size(), outputs.size());
        
        fromComponentGraph(graph, inputs, outputs);
    }
    
    protected void fromComponentGraph(ComponentGraph graph,
            List<Set<ComponentPort>> inputs,
            List<ComponentPort> outputs) {
        // Get circuit.
        circuit = graph.circuit;

        // Set inputs.
        for (int i = 0; i< inputs.size(); ++i) {
            Set<ComponentPort> componentPorts = inputs.get(i);
            Set<GateInputPort> inputPorts = new HashSet<GateInputPort>();
            
            for (ComponentPort port: componentPorts) {
                Component c = port.component;
                int inputNumber = port.portNumber;
                
                Set<GateInputPort> gatePorts= c.getGateInputPorts(inputNumber);
                inputPorts.addAll(gatePorts);
            }
            addAllInputGates(i, inputPorts);
        }

        // Set outputs.
        for (int i = 0; i< outputs.size(); ++i) {
            ComponentPort componentPort = outputs.get(i);

            Component c = componentPort.component;
            int outputNumber = componentPort.portNumber;
            
            Gate gate = c.getOutputGate(outputNumber);
            setOutputGate(i, gate);
        }
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
                "Gate is not part of the circuit");
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

    private void addInputGate(int inputNumber, GateInputPort inputPort) {
        if (inputNumber >= getInputCount()) {
            throw new IllegalArgumentException("Input number out of bounds.");
        }
        if (inputPort.inputNumber < 0 ||
                inputPort.inputNumber >= inputPort.gate.getInputCount()) {
            throw new IllegalArgumentException(
                "Input number of gate is out of bounds."
            );
        }
        if (!circuit.containsVertex(inputPort.gate)) {
            throw new IllegalArgumentException(
                "Gate is not part of the circuit");
        }
        internalInputList.get(inputNumber).add(inputPort);        
    }
    
    private void addAllInputGates(int inputNumber,
            Collection<GateInputPort> collection) {
        for (GateInputPort inputPort: collection) {
            addInputGate(inputNumber, inputPort);
        }
    }

    protected void addInputGate(int inputNumber, Gate gate,
            int gateInputNumber) {
        GateInputPort gateInputPort = new GateInputPort(gate, gateInputNumber);
        addInputGate(inputNumber, gateInputPort);
    }
    
    public class GateInputPort extends Pair<Gate, Integer> {
        /**
         * UID for serialisation.
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
        /**
         * UID for serialisation.
         */
        private static final long serialVersionUID = 1L;
        public final Integer portNumber;
        public final Component component;

        public ComponentPort(Component component, Integer portNumber) {
            super(component, portNumber);
            this.component = first;
            this.portNumber = second;
        }
    }
    
    public String getVerilogMethod(String name,
            HashMap<ComponentWire,String> inputWires,
            HashMap<ComponentWire,String> outputWires) {
        return VerilogConverter.convertToVerilog(name, this, inputWires,
                outputWires);
    }

}