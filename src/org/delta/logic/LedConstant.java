package org.delta.logic;

import org.delta.circuit.Wire;
import org.delta.transport.BoardInterface;

public class LedConstant extends Constant {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int ledNumber;

    public LedConstant(State state) {
        super(state);
    }
    
    public LedConstant(Wire wire) {
        super(wire);
    }
    
    public LedConstant() {
        super(State.SX);
    }
    
    public void setLedNumber(int ledNumber) {
        this.ledNumber = ledNumber;
    }
    
    protected BoardInterface getBoardInterface() {
        return BoardInterface.getInstance();
    }

    @Override
    public State evaluate() {
        final State state = super.evaluate();
        BoardInterface board = getBoardInterface(); 
        
        if (state == State.S0) {
            board.sendLEDEvent(ledNumber, false);
        } else if (state == State.S1) {
            board.sendLEDEvent(ledNumber, true);
        }
        
        return state;
    }

}
