package org.delta.circuit;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.graph.DirectedMultigraph;

/**
 * Graph data structure that represents the high-level/user view of the digital
 * circuit.
 * @author Group Delta 2009
 * @see Circuit
 */
public class ComponentGraph extends
        DirectedMultigraph<Component, ComponentWire> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * Internal gate-level representation of the circuit which can be passed to
     * the simulator.
     * @see Circuit
     * @see Simulator
     */
    private Circuit circuit = new Circuit();
    /**
     * 
     */
    private Map<ComponentWire, Wire> wireMap;

    /**
     * 
     */
    public ComponentGraph() {
        super(ComponentWire.class);

        wireMap = new HashMap<ComponentWire, Wire>();
    }

    /**
     * Adds a component to the graph and updates the internal circuit
     * representation: Each component internally stores a gate-level
     * interpretation of its logic. When added to a ComponentGraph object, the
     * gates and wires of this gate-level representation are added to the
     * internal gate-level representation of the circuit.
     * @param component - component to be added.
     * @return true if the circuit did not already contain the component, false
     * otherwise.
     * @see Component
     * @see ComponentGraph#circuit
     * @see DirectedMultigraph#addVertex(Object)
     */
    @Override
    public final boolean addVertex(final Component component) {
        if (!super.addVertex(component)) return false;

        final Circuit c = component.getCircuit();
        for (Gate g: c.vertexSet()) {
            circuit.addVertex(g);
        }
        for (Wire w: c.edgeSet()) {
            circuit.addEdge(c.getEdgeSource(w), c.getEdgeTarget(w), w);
        }
        return true;
    }

    /**
     * Removes several wires at once.
     * @param wireCollection - wires to be removed.
     * @return true if the operation altered the data structure,
     * false otherwise.
     * @see ComponentGraph#removeEdge(ComponentWire)
     * @see DirectedMultigraph#removeAllEdges(Collection)
     */
    @Override
    public final boolean removeAllEdges(final Collection
            <? extends ComponentWire> wireCollection) {
        if (super.removeAllEdges(wireCollection)) {
            for (ComponentWire wire: wireCollection) {
                circuit.removeEdge(wireMap.get(wire));
                wireMap.remove(wire);
            }
            return true;
        }
        return false;
    }

    /**
     * Removes all wires between two components.
     * @see ComponentGraph#removeEdge(ComponentWire)
     * @see DirectedMultigraph#removeAllEdges(Object, Object)
     */
    @Override
    public Set<ComponentWire> removeAllEdges(final Component source,
            final Component target) {
        final Set<ComponentWire> wireSet = getAllEdges(source, target);
        
        removeAllEdges(wireSet);
        return wireSet;
    }

    /**
     * Removes several components at once.
     * @see ComponentGraph#removeVertex(Component)
     * @see DirectedMultigraph#removeAllVertices(Collection)
     * @param vertexCollection
     * @return
     */
    @Override
    public boolean removeAllVertices(final Collection<? extends Component>
            componentCollection) {
        final List<ComponentWire> wireSet = new LinkedList<ComponentWire>();
        for (Component component: componentCollection) {
            wireSet.addAll(edgesOf(component));
        }
        
        if (super.removeAllVertices(componentCollection)) {
            for (ComponentWire wire: wireSet) {
                circuit.removeEdge(wireMap.get(wire));
                wireMap.remove(wire);
            }
            return true;
        }
        return false;
    }

    @Override
    public ComponentWire removeEdge(final Component source,
            final Component target) {
        final ComponentWire wire = super.getEdge(source, target);
        removeEdge(wire);
        return wire;
    }

    @Override
    public boolean removeEdge(final ComponentWire wire) {
        if (super.removeEdge(wire)) {
            circuit.removeEdge(wireMap.get(wire));
            wireMap.remove(wire);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeVertex(final Component component) {
        final Set<ComponentWire> connectedWires = edgesOf(component);
        if (super.removeVertex(component)) {
            final Set<Gate> gateSet = component.getCircuit().vertexSet();
            circuit.removeAllVertices(gateSet);
            
            for (ComponentWire wire: connectedWires) {
                wireMap.remove(wire);
            }
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean removeAllEdges(final ComponentWire[] wireArray) {
        return removeAllEdges(Arrays.asList(wireArray));
    }

    // FIXME: Unregister already registered wires.
    public boolean registerEdge(final ComponentWire wire,
            final int sourceOutputNumber, final int targetInputNumber) {
        if (!edgeSet().contains(wire) || getEdgeSource(wire) == null
                                      || getEdgeTarget(wire) == null) {
            return false;
        }

        final Component sourceComponent = getEdgeSource(wire);
        final Component targetComponent = getEdgeTarget(wire);

//        if (targetComponent.getInputWire(targetInputNumber) != null) {
//            return false;
//        }
        
        sourceComponent.setOutputWire(sourceOutputNumber, wire);
        targetComponent.setInputWire(targetInputNumber, wire);

        Component.GateInput targetGateInput =
            targetComponent.getGateInput(targetInputNumber);
        
        int targetGateInputNumber = targetGateInput.getInputNumber();
        
        Gate sourceGate = sourceComponent.getOutputGate(sourceOutputNumber);
        Gate targetGate = targetGateInput.getGate();
        
        // Remove old wire.
        final Wire oldWire = wireMap.get(wire);
        if (oldWire != null) {
            if (!circuit.removeEdge(oldWire))
                throw new IllegalStateException("Inconsistent state.");
        }
        
        
        final Wire newWire = circuit.addEdge(sourceGate, targetGate);
        
        if (newWire == null) return false;
        
        // Register gate...
        targetGate.setWire(newWire, targetGateInputNumber);
        
        wireMap.put(wire, newWire);
        return true;
    }

    /**
     * Method copies the data structure that is used internally to represent
     * digital logic circuits. In this representation, the circuit can be
     * simulated.
     * @see ComponentGraph#circuit
     * @see Circuit
     * @see Simulator
     * @see ComponentGraph#isValid()
     * @return Copy of the internal circuit representation. The return value is
     * null if the circuit data structure is inconsistent.
     */
    public final Circuit getCircuit() {
        if (!isValid()) {
            throw new IllegalStateException("Circuit is not valid.");
        }

        return (Circuit) circuit.clone();
    }

    /**
     * Method to check whether the graph contains any illegal edges.
     * @return True if the component graph can be simulated, false otherwise.
     * @see ComponentGraph#registerEdge(ComponentWire, int, int)
     */
    public final boolean isValid() {
        int countEdges = 0;
        
        for (Component component: vertexSet()) {
            for (int i = 0; i < component.getInputCount(); ++i) {
                /* Get the wire registered with the input number i of the
                 * component.
                 */
                final ComponentWire w = component.getInputWire(i);
                
                /* If the result is null, the input is unconnected and can be
                 * ignored.
                 */
                if (w != null) {
                    // Is the wire part of the component graph? If not, reject.
                    if (!edgeSet().contains(w)) return false;
                    /* Is the head of the wire the same component as the one the
                     * wire is registered with? If not, reject.
                     */
                    if (component != getEdgeTarget(w)) return false;
                    /* Is the tail vertex of the wire in the vertex set of the
                     * component graph.
                     */
                    if (!vertexSet().contains(getEdgeSource(w))) return false;
                    // Wire is valid, increase edge counter.
                    ++countEdges;
                }
            }
        }
        /* If the number of registered vertices does not agree with the number
         * edges in the graph, reject the graph.
         */
        if (countEdges != edgeSet().size()) {
            return false;
        }
        return true;
    }
}
