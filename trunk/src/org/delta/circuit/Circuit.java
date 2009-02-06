package org.delta.circuit;

import org.delta.circuit.gate.Clock;
import org.jgrapht.graph.DefaultDirectedGraph;

public class Circuit extends DefaultDirectedGraph<Gate, Wire> {

    public Circuit() {
        super(Wire.class);
    }


    public Clock getClock() {
        // TODO Auto-generated method stub
        return null;
    }
}
