package org.delta.logic;

public class Or extends BinaryFunction {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public State evaluate() {
        switch (evaluateArgument(0)) {
        case S1: return State.S1;
        case SX: return (evaluateArgument(1) == State.S1)? State.S1 : State.SX;
        case S0:
        default: return evaluateArgument(1);
        }
    }

}
