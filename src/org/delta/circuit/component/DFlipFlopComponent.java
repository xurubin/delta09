package org.delta.circuit.component;

import org.delta.circuit.Circuit;
import org.delta.circuit.ClockedComponent;
import org.delta.circuit.Gate;
import org.delta.circuit.Wire;
import org.delta.circuit.gate.InverterGate;

public class DFlipFlopComponent extends ClockedComponent {

    public DFlipFlopComponent() {
        super(1, 2);
        Circuit circuit = getCircuit();

        ClockedComponent master = new DLatchComponent();
        circuit.addCircuit(master.getCircuit());
        ClockedComponent slave = new DLatchComponent();
        circuit.addCircuit(slave.getCircuit());
         
        for (GateInputPort gateInputPort: slave.getGateInputPorts(0)) {
            Gate gate = gateInputPort.getGate();
            int inputNumber = gateInputPort.getInputNumber();
            
            Wire wire = circuit.addEdge(master.getOutputGate(0), gate);
            gate.setWire(wire, inputNumber);
        }
        
        Gate inv = new InverterGate();
        circuit.addVertex(inv);
        
        for(GateInputPort gateInputPort: master.getClockInputList()) {
            Gate gate = gateInputPort.getGate();
            int inputNumber = gateInputPort.getInputNumber();
            
            Wire wire = circuit.addEdge(inv, gate);
            gate.setWire(wire, inputNumber);
        }
        
        addClockInput(inv, 0);
        
        for(GateInputPort gateInputPort: slave.getClockInputList()) {
            Gate gate = gateInputPort.getGate();
            int inputNumber = gateInputPort.getInputNumber();
            
            addClockInput(gate, inputNumber);
        }
        
        for (GateInputPort gateInputPort: master.getGateInputPorts(0)) {
            Gate gate = gateInputPort.getGate();
            int gateInputNumber = gateInputPort.getInputNumber();
            
            addInputGate(0, gate, gateInputNumber);
        }
        
        setOutputGate(0, slave.getOutputGate(0));
        setOutputGate(1, slave.getOutputGate(1));
    }

}
