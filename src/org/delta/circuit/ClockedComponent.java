package org.delta.circuit;

import java.util.ArrayList;
import java.util.List;


public class ClockedComponent extends Component {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<GateInputPort> clockInputList;

    public ClockedComponent(int inputCount, int outputCount) {
        super(inputCount, outputCount);
        
        clockInputList = new ArrayList<GateInputPort>();
    }
    
    protected void addClockInput(Gate gate, int inputNumber) {
        clockInputList.add(new GateInputPort(gate, inputNumber));
    }
    
    public List<GateInputPort> getClockInputList() {
        return clockInputList;
    }

}
