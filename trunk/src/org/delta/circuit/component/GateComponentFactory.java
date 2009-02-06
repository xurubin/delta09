package org.delta.circuit.component;

import org.delta.circuit.Component;
import org.delta.circuit.Gate;

public class GateComponentFactory {

    public static Component createComponent(final Class<Gate> gateClass)
            throws InstantiationException, IllegalAccessException {
        final Gate gate = gateClass.newInstance();

        return new Component(gate.getInputCount(), 1) {
            {
                circuit.addVertex(gate);

                for (int i = 0; i < gate.getInputCount(); i++) {
                    inputMap.put(i, gate);
                }
                outputList.set(0, gate);
            }
        };
    }
}
