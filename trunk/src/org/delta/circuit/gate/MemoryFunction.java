package org.delta.circuit.gate;

import org.delta.logic.Function;
import org.delta.logic.State;

public class MemoryFunction extends Function {
    /**
     * UID for serialisation.
     */
    private static final long serialVersionUID = 1L;

    public MemoryFunction() {
        super(2);
    }

    private State store = State.SX;
    
    public void setStore(State store) {
        this.store = store;
    }

    @Override
    public State evaluate() {
        State result = store;
        if (evaluateArgument(0) == State.S1) {
            store = evaluateArgument(1);
        }
        
        return result;
    }

}
