package org.delta.circuit;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.jgrapht.graph.DirectedMultigraph;

/**
 * The simulatable gate-level representation of a circuit.
 * @author Group Delta 2009
 *
 */
public class Circuit extends DirectedMultigraph<Gate, Wire> {
	private static final long serialVersionUID = 1L;

	public Circuit() {
        super(Wire.class);
    }
	
	public void addCircuit(Circuit circuit) {
	    for (Gate gate: circuit.vertexSet()) {
	        addVertex(gate);
	    }
	    for (Wire wire: circuit.edgeSet()) {
	        Gate source = circuit.getEdgeSource(wire);
	        Gate target = circuit.getEdgeTarget(wire);
	        
	        addEdge(source, target, wire);
	    }
	}

    @Override
    public boolean removeAllEdges(final Collection<? extends Wire>
            wireCollection) {
        if (wireCollection == null) return false;

        boolean hasChanged = false;        
        for (Wire wire: wireCollection) {
            boolean result = removeEdge(wire);
            if (result) hasChanged = true;
        }

        return hasChanged;
    }
    
    @Override
    public Set<Wire> removeAllEdges(final Gate source, final Gate target) {
        final Set<Wire> wireSet = getAllEdges(source, target);
        removeAllEdges(wireSet);
        
        return wireSet;
    }
    
    @Override
    protected boolean removeAllEdges(final Wire[] wireArray) {
        return removeAllEdges(Arrays.asList(wireArray));
    }
    
    @Override
    public Wire removeEdge(final Gate source, final Gate target) {
        final Wire wire = getEdge(source, target);
        removeEdge(wire);

        return wire;
        
    }
    
    @Override
    public boolean removeEdge(final Wire wire) {
        if (wire == null) return false;

        final Gate targetGate = getEdgeTarget(wire);
        
        if (super.removeEdge(wire)) {
            if (targetGate.isConnectedTo(wire)) targetGate.removeWire(wire);
            return true;
        }
        return false;
    }
}
