package org.delta.circuit.component;

import java.util.ArrayList;

import org.delta.circuit.Component;
import org.delta.circuit.ComponentWire;
import org.delta.circuit.Gate;
import org.delta.circuit.gate.AndGate;

public class AndComponent extends Component {

    public AndComponent(int inputCount) {
        super(inputCount, 1);

        AndGate andGate = new AndGate(inputCount);
        circuit.addVertex(andGate);

        for (int i = 0; i < inputCount; i++) {
            inputMap.put(i, andGate);
        }
        outputList.add(andGate);
    }

    public String getVerilogMethod(String name, String out, ArrayList<String> in) {
    	return Gate.constructDefaultVerilogMethod("and", name, out, in);
    }

}
