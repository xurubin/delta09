package org.delta.circuit.gate;

import org.delta.circuit.Gate;
import org.delta.logic.Constant;
import org.delta.logic.Formula;
import org.delta.logic.State;

public abstract class AbstractInputGate extends Gate {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private State state = State.SX;

    public AbstractInputGate() {
        super(0);
    }
    
    public abstract boolean update();
    
    void setState(State state) {
        this.state = state;
    }
    
    State getState() {
        return state;
    }

    @Override
    public Formula getFormula() {
        return new Constant(state);
    }
}
