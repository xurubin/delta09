package org.delta.circuit;

import org.jgrapht.EdgeFactory;

public class WireFactory implements EdgeFactory<Gate, Wire> {
    private Circuit circuit;
    
    public WireFactory() {
    }
    
    public void setCircuit(Circuit circuit) {
        this.circuit = circuit;
    }

    @Override
    public Wire createEdge(Gate sourceVertex, Gate targetVertex) {
        return null;
    }

}
