package org.delta.circuit.component;

import org.delta.circuit.Component;
import org.delta.circuit.gate.DebugInputGate;
import org.delta.logic.State;

public class DebugInputComponent extends Component {

    public DebugInputComponent(State state) {
        super(0, 1);
        
        DebugInputGate in = new DebugInputGate();
        in.setState(state);
        
        getCircuit().addVertex(in);
        
        setOutputGate(0, in);
    }

    @Override
    public String getVerilogMethod() {
        return "debug";
    }
}
