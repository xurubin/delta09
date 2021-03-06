package org.delta.logic;

public class Nand extends BinaryFunction {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public State evaluate() {
        switch(evaluateArgument(0)) {
        case S0: return State.S1;
        case S1: return (new Not(getArgument(1))).evaluate();
        case SX:
        default: return (evaluateArgument(1) == State.S0)? State.S1 : State.SX;
        }
    }

}
