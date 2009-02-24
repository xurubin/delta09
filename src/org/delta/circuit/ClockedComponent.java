package org.delta.circuit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Marker class for clocked components. The 0th input to the component is
 * reserved for the clock: When a clocked component is added to a component
 * graph, a wire from the main circuit clock to the 0th component input is
 * automatically inserted. 
 */
public class ClockedComponent extends Component {
    /**
     * UID for serialisation.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Holds all gate inputs which are connected to the 0th component input.
     * (The 0th input of a ClockedConponent is reserved for the main clock of
     * the circuit (i.e. the ComponentGraph).)
     */
    private List<GateInputPort> clockInputList;

    /**
     * Standard constructor for creating a new component.
     * @param inputCount - number of inputs.
     * @param outputCount - number of outputs.
     * @see Component#Component(int, int)
     */
    public ClockedComponent(final int inputCount, final int outputCount) {
        super(inputCount, outputCount);
        
        clockInputList = new ArrayList<GateInputPort>();
    }
    
    /**
     * Creates a new (clocked) component from a ComponentGraph which is useful
     * for grouping components or when writing components which build upon
     * several other components.
     * @param graph - 
     * @param inputs - a list of all component inputs that will be inputs in the
     * new component. All of the components in this list must be vertices in the
     * component graph.
     * @param outputs - a list of all component outputs that will be inputs in
     * the new component. All of the components in this list must be vertices in
     * the component graph.
     * @param clockedInputs
     */
    public ClockedComponent(ComponentGraph graph,
            List<Set<ComponentPort>> inputs,
            List<ComponentPort> outputs,
            Set<ComponentPort> clockedInputs) {
        super(inputs.size(), outputs.size());
        
        fromComponentGraph(graph, inputs, outputs, clockedInputs);
    }
    
    public ClockedComponent(ComponentGraph graph,
            List<Set<ComponentPort>> inputs,
            List<ComponentPort> outputs) {
        super(inputs.size(), outputs.size());
        
        fromComponentGraph(graph, inputs, outputs);
    }
    
    protected void fromComponentGraph(ComponentGraph graph,
            List<Set<ComponentPort>> inputs,
            List<ComponentPort> outputs,
            Set<ComponentPort> clockInputs) {
        
        // Disconnect from old clock component, remove old clock gate.
        Set<ComponentWire> oldClockWires =
            graph.outgoingEdgesOf(graph.mainClockComponent);
        graph.circuit.removeVertex(graph.mainClockGate);
        
        super.fromComponentGraph(graph, inputs, outputs);

        for (ComponentWire wire: oldClockWires) {
            Component clockedComponent = graph.getEdgeTarget(wire);
            Set<GateInputPort> ports = clockedComponent.getGateInputPorts(wire);
            
            for (GateInputPort port: ports) {
                addClockInput(port);
            }
        }
        graph.removeAllEdges(oldClockWires);
        
        // Connect clock inputs.
        for (ComponentPort componentPort: clockInputs) {
            Component component = componentPort.component;
            int inputNumber = componentPort.portNumber;
            
            Set<GateInputPort> inputPorts =
                component.getGateInputPorts(inputNumber);
            addAllClockInputs(inputPorts);
        }
    }
    
    private void addAllClockInputs(Collection<GateInputPort> gateInputPorts) {
        clockInputList.addAll(gateInputPorts);
    }

    private void addClockInput(GateInputPort gateInputPort) {
        clockInputList.add(gateInputPort);
    }

    protected void fromComponentGraph(ComponentGraph graph,
            List<Set<ComponentPort>> inputs,
            List<ComponentPort> outputs) {
        Set<ComponentPort> clockedInputs = new HashSet<ComponentPort>();
        for (Component component: graph.vertexSet()) {
            if (component instanceof ClockedComponent) {
                clockedInputs.add(new ComponentPort(component, 0));
            }
        }
        fromComponentGraph(graph, inputs, outputs, clockedInputs);
    }
    
    protected void addClockInput(Gate gate, int inputNumber) {
        addClockInput(new GateInputPort(gate, inputNumber));
    }
    
    public List<GateInputPort> getClockInputList() {
        return clockInputList;
    }

}
