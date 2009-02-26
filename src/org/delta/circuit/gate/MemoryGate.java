package org.delta.circuit.gate;

import org.delta.circuit.Gate;
import org.delta.logic.Constant;
import org.delta.logic.Formula;
import org.delta.logic.Function;
import org.delta.logic.State;

public class MemoryGate extends Gate {
    /**
     * UID for serialisation.
     */
    private static final long serialVersionUID = 1L;
    private MemoryFunction function = new MemoryFunction(); 

    public MemoryGate() {
        super(2);
    }

    public void setStore(State state) {
        function.setStore(state);
    }
    
    @Override
    public Formula getFormula() {
        function.setArgument(0, new Constant(getWire(0)));
        function.setArgument(1, new Constant(getWire(1)));

        return function;
    }

}
