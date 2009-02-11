package org.delta.circuit.component;

import org.delta.circuit.Component;
import org.delta.circuit.Gate;
import org.delta.circuit.gate.DebugOutputGate;

public class DebugOutputComponent extends Component {

    public DebugOutputComponent() {
        super(1, 0);
        
        Gate out = new DebugOutputGate();
        getCircuit().addVertex(out);
        
        setInputGate(0, out, 0);
    }

    @Override
    public String getVerilogMethod() {
        return "debug";
    }
}
