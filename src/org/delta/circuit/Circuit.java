package org.delta.circuit;

import org.jgrapht.graph.DefaultDirectedGraph;

public class Circuit extends DefaultDirectedGraph<Gate, Wire> {

    public Circuit() {
        super(Wire.class);
    }


    public Gate getClock() {
        // TODO Auto-generated method stub
        return null;
    }
}
