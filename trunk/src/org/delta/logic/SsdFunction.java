package org.delta.logic;

import org.delta.transport.BoardInterface;

public class SsdFunction extends Function {
    private int ssdNumber;
    
    public SsdFunction() {
        super(7);
    }
    
    public void setSsdNumber(int ssdNumber) {
        this.ssdNumber = ssdNumber;
    }

    @Override
    public State evaluate() {
        BoardInterface board = BoardInterface.getInstance(); 
        
        for (int i = 0; i < getArgumentCount(); ++i) {
            State state = evaluateArgument(i); 
            if (state == State.S0) {
                board.sendHEXEvent(ssdNumber, i, false);
            } else if (state == State.S1) {
                board.sendHEXEvent(ssdNumber, i, false);
            }
        }
        
        // Return value does not matter as it will not be used.
        return State.SX;
    }

}
