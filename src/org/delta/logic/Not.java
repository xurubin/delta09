package org.delta.logic;

public class Not extends Function {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Not(Formula argument) {
        this();
        setArgument(0, argument);
    }
    
    public Not() {
        super(1);
    }

    @Override
    public State evaluate() {
       switch (evaluateArgument(0)) {
       case S0: return State.S1;
       case S1: return State.S0;
       case SX:
       default: return State.SX;
       }
    }

}
