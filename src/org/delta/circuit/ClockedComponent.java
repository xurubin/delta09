package org.delta.circuit;

import java.util.List;

public class ClockedComponent extends Component {
    private List<GateInput> clockInputList;

    public ClockedComponent(int inputCount, int outputCount) {
        super(inputCount, outputCount);
    }
    
    void addClockInput(Gate gate, int inputNumber) {
        clockInputList.add(new GateInput(gate, inputNumber));
    }
    
    List<GateInput> getClockInputList() {
        return clockInputList;
    }

}
