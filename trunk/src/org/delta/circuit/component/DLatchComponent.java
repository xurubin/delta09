package org.delta.circuit.component;

import org.delta.circuit.Circuit;
import org.delta.circuit.ClockedComponent;
import org.delta.circuit.Component;
import org.delta.circuit.Gate;
import org.delta.circuit.Wire;
import org.delta.circuit.gate.GateFactory;
import org.delta.circuit.gate.InverterGate;
import org.delta.logic.And;

public class DLatchComponent extends ClockedComponent {

    public DLatchComponent() {
        super(1, 2);
        
        Component srLatch = new SrLatchComponent();
        Circuit dLatch = srLatch.getCircuit();
        
        Gate inv = new InverterGate();
        Gate and0 = GateFactory.createGate(And.class, 2);
        Gate and1 = GateFactory.createGate(And.class, 2);
        
        dLatch.addVertex(inv);
        dLatch.addVertex(and0);
        dLatch.addVertex(and1);
        
        Wire invToAnd0 = dLatch.addEdge(inv, and0);
        and0.setWire(invToAnd0, 1);
        
        for (GateInputPort gateInputPort: srLatch.getGateInputPorts(0)) {
            Gate gate = gateInputPort.gate;
            int inputNumber = gateInputPort.inputNumber;
            Wire and0ToLatch0 = dLatch.addEdge(and0, gate);
            gate.setWire(and0ToLatch0, inputNumber);
        }
        
        for (GateInputPort gateInputPort: srLatch.getGateInputPorts(1)) {
            Gate gate = gateInputPort.gate;
            int inputNumber = gateInputPort.inputNumber;
            Wire and1ToLatch0 = dLatch.addEdge(and1, gate);
            gate.setWire(and1ToLatch0, inputNumber);
        }
        
        setCircuit(dLatch);
        
        addClockInput(and0, 0);
        addClockInput(and1, 0);
        
        addInputGate(0, and1, 1);
        addInputGate(0, inv, 0);
        
        setOutputGate(0, srLatch.getOutputGate(0));
        setOutputGate(1, srLatch.getOutputGate(1));
    }

}
