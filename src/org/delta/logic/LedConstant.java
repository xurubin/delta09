package org.delta.logic;

import org.delta.circuit.Wire;
import org.delta.transport.BoardInterface;

public class LedConstant extends Constant {
    private int ledNumber;

    public LedConstant(State state) {
        super(state);
    }
    
    public LedConstant(Wire wire) {
        super(wire);
    }
    
    public void setLedNumber(int ledNumber) {
        this.ledNumber = ledNumber;
    }

    @Override
    public State evaluate() {
        State state = super.evaluate();
        BoardInterface board = BoardInterface.getInstance(); 
        
        if (state == State.S0) {
            board.sendLEDEvent(ledNumber, false);
        } else if (state == State.S1) {
            board.sendLEDEvent(ledNumber, true);
        }
        
        return state;
    }

}
