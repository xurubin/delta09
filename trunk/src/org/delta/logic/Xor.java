package org.delta.logic;

public class Xor extends BinaryFunction {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public State evaluate() {
        switch (evaluateArgument(0)) {
        case S0: return evaluateArgument(1);
        case SX: return State.SX;
        case S1:
        default: return (new Not(getArgument(1))).evaluate();
        }
    }
}
