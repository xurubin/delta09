package org.delta.circuit;

import org.jgrapht.graph.DefaultDirectedGraph;

public class Circuit extends DefaultDirectedGraph<Gate, Wire> {
    private WireFactory wireFactory;

    public Circuit() {
        super(new WireFactory());
        wireFactory = (WireFactory) getEdgeFactory();
        wireFactory.setCircuit(this);
    }
    

    @Override
    public Object clone() {
        // TODO Auto-generated method stub
        return super.clone();
    }
}
