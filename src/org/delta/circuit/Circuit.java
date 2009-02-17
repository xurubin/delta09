package org.delta.circuit;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.jgrapht.graph.DefaultDirectedGraph;

/**
 * The simulatable gate-level representation of a circuit.
 * @author Group Delta 2009
 *
 */
public class Circuit extends DefaultDirectedGraph<Gate, Wire> {
	private static final long serialVersionUID = 1L;

	public Circuit() {
        super(Wire.class);
    }

    @Override
    public boolean removeAllEdges(final Collection<? extends Wire>
            wireCollection) {
        final Map<Wire, Gate> wireToGateMap =
            new HashMap<Wire, Gate>();
        for (Wire wire: wireCollection) {
            if (!containsEdge(wire)) continue;
            wireToGateMap.put(wire, getEdgeTarget(wire));
        }
        
        if (super.removeAllEdges(wireCollection)) {
            for (Entry<Wire, Gate> entry: wireToGateMap.entrySet()) {
                final Gate gate = entry.getValue();
                final Wire wire = entry.getKey();
                gate.removeWire(wire);
            }
            return true;
        }
        return false;
    }
    
    @Override
    public Set<Wire> removeAllEdges(final Gate source, final Gate target) {
        final Map<Wire, Gate> wireToGateMap =
            new HashMap<Wire, Gate>();
        for (Wire wire: getAllEdges(source, target)) {
            wireToGateMap.put(wire, getEdgeTarget(wire));
        }
        
        final Set<Wire> wireSet = super.removeAllEdges(source, target);
        
        if (wireSet != null) {
            for (Entry<Wire, Gate> entry: wireToGateMap.entrySet()) {
                final Gate gate = entry.getValue();
                final Wire wire = entry.getKey();
                gate.removeWire(wire);
            }
        }
        return wireSet;
    }
    
    @Override
    protected boolean removeAllEdges(final Wire[] wireArray) {
        final Map<Wire, Gate> wireToGateMap =
            new HashMap<Wire, Gate>(wireArray.length);
        for (Wire wire: wireArray) {
            if (!containsEdge(wire)) continue;
            wireToGateMap.put(wire, getEdgeTarget(wire));
        }
        
        if (super.removeAllEdges(wireArray)) {
            for (Entry<Wire, Gate> entry: wireToGateMap.entrySet()) {
                final Gate gate = entry.getValue();
                final Wire wire = entry.getKey();
                gate.removeWire(wire);
            }
            return true;
        }
        return false;
    }
    
    @Override
    public Wire removeEdge(final Gate source, final Gate target) {
        final Wire wire = super.removeEdge(source, target);
        
        if (wire != null) {
            target.removeWire(wire);
        }
        return wire;
        
    }
    
    @Override
    public boolean removeEdge(final Wire wire) {
        if (wire == null) return false;

        final Gate targetGate = getEdgeTarget(wire);
        
        if (super.removeEdge(wire)) {
            targetGate.removeWire(wire);
            return true;
        }
        return false;
    }
    
}
