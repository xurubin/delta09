package org.delta.circuit.gate;

import java.util.ArrayList;

import org.delta.logic.State;
import org.delta.transport.BoardInterface;

public class SwitchGate extends AbstractInputGate {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
    
    public String getVerilogMethod(String name, ArrayList<String> out, ArrayList<String> in) {
    	String result = "";
    	String key = "SW";
    	int number = this.switchNumber;
    	if(this.switchNumber >= 18) {
    		key = "~KEY";
    		number -= 18;
    	}
    	for(String s : out) {
    		result += "assign " + s + " = " + key + "[" + number + "];\n";
    	}
    	if(out.size() != 0) result = result.substring(0, result.length() - 1);
    	return result;
    }

}
