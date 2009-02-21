package org.delta.circuit.component;

import java.util.ArrayList;
import java.util.List;

import org.delta.circuit.Circuit;
import org.delta.circuit.Component;
import org.delta.circuit.Gate;
import org.delta.circuit.Wire;
import org.delta.circuit.gate.GateFactory;
import org.delta.logic.And;
import org.delta.logic.Not;

public class DecoderComponent extends Component {
    private static final long serialVersionUID = 1L;

    public DecoderComponent(int inputCount) {
        super(inputCount, 1 << inputCount);
        
        final Circuit circuit = getCircuit();
        final List<Gate> andPlane = new ArrayList<Gate>(getOutputCount());
        final List<Gate> invertedInputs = new ArrayList<Gate>(getInputCount());
        
        for (int i = 0; i < getInputCount(); ++i) {
            final Gate inv = GateFactory.createGate(Not.class);
            invertedInputs.add(inv);
            circuit.addVertex(inv);
            addInputGate(i, inv, 0);
        }
        
        for (int i = 0; i < getOutputCount(); ++i) {
            final Gate and = GateFactory.createGate(And.class, getInputCount());
            andPlane.add(and);
            circuit.addVertex(and);
            setOutputGate(i, and);

            for (int j = 0; j < getInputCount(); ++j) {
                if ((i & (1 << j)) != 0) {
                    addInputGate(j, and, j);
                } else {
                    final Wire wire =
                        circuit.addEdge(invertedInputs.get(j), and);
                    and.setWire(wire, j);
                }
            }
        }
    }

}
