package org.delta.circuit.component;

import org.delta.circuit.ClockedComponent;
import org.delta.circuit.Component;
import org.delta.circuit.Gate;

public class ComponentFactory {
    
    private ComponentFactory() {}

    public static Component createComponent(final Gate gate) {
        return new Component(gate.getInputCount(), 1) {
            /**
             * UID for serialisation.
             */
            private static final long serialVersionUID = 1L;
            {
                getCircuit().addVertex(gate);

                for (int i = 0; i < gate.getInputCount(); ++i) {
                    addInputGate(i, gate, i);
                }

                setOutputGate(0, gate);
            }
        };
    }
    
    public static ClockedComponent createClockedComponent(final Gate gate) {
        return new ClockedComponent(gate.getInputCount(), 1) {
            /**
             * UID for serialisation.
             */
            private static final long serialVersionUID = 1L;
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
