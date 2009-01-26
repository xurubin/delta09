package org.delta.circuit;

import org.delta.logic.State;
import org.jgrapht.graph.DefaultEdge;

public class Wire extends DefaultEdge {
    private State state = State.SX;
    
    public State getState() {
        return state;
    }
    
    public void setValue(State value) {
        this.state = value;
    }

}
