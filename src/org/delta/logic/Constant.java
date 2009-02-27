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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Constant other = (Constant) obj;
        if (state == null) {
            if (other.state != null) return false;
        } else if (!state.equals(other.state)) return false;
        return true;
    }

}
