package org.delta.logic;

import java.io.Serializable;

import org.delta.circuit.Wire;

public class Constant implements Formula, Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
