package org.delta.circuit;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.delta.circuit.Component.GateInputPort;
import org.delta.circuit.component.ComponentFactory;
import org.delta.circuit.gate.ClockGate;
import org.jgrapht.graph.DirectedMultigraph;

/**
 * Graph data structure that represents the high-level/user view of the digital
 * circuit.
 * @see Circuit
 */
public class ComponentGraph extends
        DirectedMultigraph<Component, ComponentWire> {
    /**
     * UID for serialisation.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Internal gate-level representation of the circuit which can be passed to
     * the simulator.
     * @see Circuit
     * @see Simulator
     */
    Circuit circuit = new Circuit();
    /**
     * A map from wires as represented in {@link ComponentGraph}} and their
     * corresponding wires in the low-level {@link Circuit} data structure (this
     * is a one-to-many relation). The map is used to keep {@link
     * ComponentGraph#circuit} up to date.
     * @see ComponentGraph#circuit
     */
    private Map<ComponentWire, Set<Wire>> wireMap;
    final ClockGate mainClockGate;
    final Component mainClockComponent;
    private boolean isClocked = true;

    /**
     * 
     */
    public ComponentGraph() {
        super(ComponentWire.class);

        // Initialise collections.
        wireMap = new HashMap<ComponentWire, Set<Wire>>();
        
        // Add clock to circuit.
        mainClockGate = new ClockGate();
        mainClockComponent =
            ComponentFactory.createComponent(mainClockGate);
        addVertex(mainClockComponent);
    }
    
    /**
     * Setting the argument to false prevents the component graph from
     * connecting clocked components to the main clock. This constructor should
     * only be used for the purpose of creating new components!
     * @param isClocked - whether clocked components are wired to the main
     * clock.
     * @see ComponentGraph#mainClockComponent
     * @see ClockedComponent
     */
    public ComponentGraph(final boolean isClocked) {
        this();
        this.isClocked  = isClocked;
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
        
        if (isClocked && component instanceof ClockedComponent) {
            final ClockedComponent cc = (ClockedComponent) component;
            final List<Component.GateInputPort> list = cc.getClockInputList();
            for (GateInputPort gi: list) {
                final Gate gate = gi.gate;
                final Wire wire = circuit.addEdge(mainClockGate, gate);
                gate.setWire(wire, gi.inputNumber);
            }
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
        boolean hasChanged = false;

        for (ComponentWire wire: wireCollection) {
            boolean result = removeEdge(wire);
            if (result) {
                hasChanged = true;
            }
        }

        return hasChanged;
    }

    /**
     * Removes all wires between two components.
     * @see ComponentGraph#removeEdge(ComponentWire)
     * @see DirectedMultigraph#removeAllEdges(Object, Object)
     * @param source -
     * @param target -
     * @return
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
     * @param componentCollection - Collections of vertices to be removed.
     * @return true if data structure has changed (i.e. at least one vertex has
     * been deleted), false otherwise.
     */
    @Override
    public final boolean removeAllVertices(final Collection<? extends Component>
            componentCollection) {
        boolean hasChanged = false;
        
        for (Component component: componentCollection) {
            boolean result = removeVertex(component);
            if (result) hasChanged = true;
        }
        
        return hasChanged;
    }

    @Override
    public ComponentWire removeEdge(final Component source,
            final Component target) {
        final ComponentWire wire = getEdge(source, target);
        removeEdge(wire);
        return wire;
    }

    @Override
    public boolean removeEdge(final ComponentWire wire) {
        Component source = getEdgeSource(wire);
        Component target = getEdgeTarget(wire);
        if (super.removeEdge(wire)) {
            circuit.removeAllEdges(wireMap.get(wire));
            source.removeOutputWire(wire);
            target.removeInputWire(wire);
            wireMap.remove(wire);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeVertex(final Component component) {
        // Never remove the clock.
        if (component == mainClockComponent) return false;
        
        final Set<ComponentWire> connectedWires = edgesOf(component);
        if (super.removeVertex(component)) {
            final Set<Gate> gateSet = component.getCircuit().vertexSet();
            circuit.removeAllVertices(gateSet);
            
            for (ComponentWire wire: connectedWires) {
                if (wireMap.containsKey(wire)) wireMap.remove(wire);
            }
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean removeAllEdges(final ComponentWire[] wireArray) {
        return removeAllEdges(Arrays.asList(wireArray));
    }

    /**
     * Register a wire with the component input/output it connects to.
     * @param wire - the wire to register. It must be an edge of this graph.
     * @param sourceOutputNumber - number of the output port of the source
     * component.
     * @param targetInputNumber - number of the input port of the target
     * component.
     */
    public void registerEdge(
            final ComponentWire wire,
            final int sourceOutputNumber,
            final int targetInputNumber) {
        if (!edgeSet().contains(wire)) {
            return;
        }
        if (getEdgeSource(wire) == null || getEdgeTarget(wire) == null) {
            return;
        }

        final Component sourceComponent = getEdgeSource(wire);
        final Component targetComponent = getEdgeTarget(wire);
        
        sourceComponent.addOutputWire(sourceOutputNumber, wire);
        targetComponent.setInputWire(targetInputNumber, wire);

        Set<Component.GateInputPort> targets =
            targetComponent.getGateInputPorts(targetInputNumber);

        // Remove old wire.
        if (wireMap.containsKey(wire) && !wireMap.get(wire).isEmpty()) {
            Set<Wire> oldWireSet = wireMap.get(wire);

            if (!circuit.removeAllEdges(oldWireSet))
                throw new IllegalStateException("Inconsistent state.");
        }
        
        Set<Wire> newWireSet = new HashSet<Wire>();
        for (GateInputPort targetGateInputPort: targets) {
            int targetGateInputNumber = targetGateInputPort.inputNumber;
            
            Gate sourceGate = sourceComponent.getOutputGate(sourceOutputNumber);
            Gate targetGate = targetGateInputPort.gate;
    
            Wire newWire = circuit.addEdge(sourceGate, targetGate);
            if (newWire == null) continue;
            
            // Register gate...
            targetGate.setWire(newWire, targetGateInputNumber);
            newWireSet.add(newWire);
        }
        wireMap.put(wire, newWireSet);
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

        Circuit clone = (Circuit) circuit.clone();
        
        return clone;
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
    
    public void setClockFrequency(int frequency) {
        mainClockGate.setClockFrequency(frequency);
    }
}
