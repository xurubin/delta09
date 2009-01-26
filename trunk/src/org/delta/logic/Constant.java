package org.delta.logic;

import org.delta.circuit.Wire;

public class Constant extends Formula {
    private State state = State.SX;

    public Constant(State state) {
        if (state != null) {
            this.state = state;
        }
    }
    
    public Constant(Wire wire) {
        if (wire != null && wire.getState() != null) {
            state = wire.getState();
        }
    }

    @Override
    public State evaluate() {
        return state;
    }

}
