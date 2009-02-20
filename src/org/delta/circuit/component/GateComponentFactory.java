package org.delta.circuit.component;

import org.delta.circuit.Component;
import org.delta.circuit.Gate;
import org.delta.circuit.gate.GateFactory;
import org.delta.logic.BinaryFunction;

public class GateComponentFactory {
    
    private GateComponentFactory() {}

    public static Component createComponent(final Gate gate) {

        return new Component(gate.getInputCount(), 1) {
            {
                getCircuit().addVertex(gate);

                for (int i = 0; i < gate.getInputCount(); ++i) {
                    addInputGate(i, gate, i);
                }

                setOutputGate(0, gate);
            }
        };
    }
}
