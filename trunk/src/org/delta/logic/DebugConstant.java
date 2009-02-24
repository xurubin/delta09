package org.delta.logic;

import org.delta.circuit.Wire;

public class DebugConstant extends Constant {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name = "";

    public DebugConstant(State state) {
        super(state);
    }

    public DebugConstant(Wire wire) {
        super(wire);
    }
    
    public DebugConstant(Wire wire, String name) {
        this(wire);
        this.name = name;
    }

    @Override
    public State evaluate() {
        State result = super.evaluate();
        System.out.println("Debug: " + result + " (" + name + ").");
        return result;
    }

}
