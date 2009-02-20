package org.delta.circuit.component;

import org.delta.circuit.Component;
import org.delta.circuit.Gate;
import org.delta.circuit.gate.DebugOutputGate;

public class DebugOutputComponent extends Component {

    public DebugOutputComponent() {
        this("");
    }
    
    public DebugOutputComponent(String name) {
        super(1, 0);
        
        Gate out = new DebugOutputGate(name);
        getCircuit().addVertex(out);
        
        addInputGate(0, out, 0);
    }
}
