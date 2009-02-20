package org.delta.circuit;

import java.util.List;


public class ClockedComponent extends Component {
    private List<GateInputPort> clockInputList;

    public ClockedComponent(int inputCount, int outputCount) {
        super(inputCount, outputCount);
    }
    
    void addClockInput(Gate gate, int inputNumber) {
        clockInputList.add(new GateInputPort(gate, inputNumber));
    }
    
    List<GateInputPort> getClockInputList() {
        return clockInputList;
    }

}
