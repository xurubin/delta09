package org.delta.logic;

public class Xnor extends BinaryFunction {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public State evaluate() {
        State arg0 = evaluateArgument(0);
        if (arg0 == State.SX) return State.SX;
        
        State arg1 = evaluateArgument(1);
        if (arg1 == State.SX) return State.SX;
        
        if (arg0 == arg1) {
            return State.S1;
        } else {
            return State.S0;
        }
    }
}
