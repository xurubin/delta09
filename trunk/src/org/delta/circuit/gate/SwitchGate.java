package org.delta.circuit.gate;

import java.util.ArrayList;

import org.delta.logic.State;
import org.delta.transport.BoardInterface;

public class SwitchGate extends AbstractInputGate {
    private int switchNumber;
    
    public SwitchGate(int switchNumber) {
        this.switchNumber = switchNumber;
    }
    
    @Override
    public boolean update() {
        BoardInterface board = BoardInterface.getInstance(); 
        State newState;
        if (board.getSwitchStatus(switchNumber)) {
            newState = State.S1;
        } else {
            newState = State.S0;
        }
        
        if (getState() != newState) {
            setState(newState);
            return true;
        }
        return false;
    }
    
    public String getVerilogMethod(String name, String out, ArrayList<String> in) {
    	return "assign SW[" + this.switchNumber + "] = " + out + ";";
    }

}
