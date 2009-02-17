package org.delta.simulation;

import org.delta.circuit.Circuit;
import org.delta.circuit.Gate;
import org.delta.circuit.Wire;
import org.delta.circuit.gate.DebugInputGate;
import org.delta.circuit.gate.DebugOutputGate;
import org.delta.circuit.gate.GateFactory;
import org.delta.circuit.gate.HighGate;
import org.delta.circuit.gate.InverterGate;
import org.delta.circuit.gate.LowGate;
import org.delta.logic.Nor;
import org.delta.logic.State;

public class SimulationDemo {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Circuit circ = new Circuit();
        Circuit circ2 = new Circuit();
        Simulator sim = new Simulator();

        SimulationScheduler sched = new SimulationScheduler(sim);
        sched.setSimulationFrequency(200L);

        Gate n1 = GateFactory.createGate(Nor.class, 2);
        Gate n2 = GateFactory.createGate(Nor.class, 2);
        HighGate h1 = new HighGate();
        LowGate l1 = new LowGate();
        DebugOutputGate d1 = new DebugOutputGate();
        DebugOutputGate d2 = new DebugOutputGate();

        circ.addVertex(n1);
        circ.addVertex(n2);
        circ.addVertex(h1);
        circ.addVertex(l1);
        circ.addVertex(d1);
        circ.addVertex(d2);

        Wire n1_in = circ.addEdge(l1, n1);
        Wire n2_in = circ.addEdge(l1, n2);
        n1.setWire(n1_in, 0);
        n2.setWire(n2_in, 1);

        Wire n1_out = circ.addEdge(n1, d1);
        Wire n2_out = circ.addEdge(n2, d2);
        d1.setWire(n1_out, 0);
        d2.setWire(n2_out, 0);

        Wire cross_12 = circ.addEdge(n1, n2);
        Wire cross_21 = circ.addEdge(n2, n1);
        n1.setWire(cross_21, 1);
        n2.setWire(cross_12, 0);

        InverterGate inv1 = new InverterGate();
        InverterGate inv2 = new InverterGate();
        Gate nor1 = GateFactory.createGate(Nor.class, 2);
        DebugInputGate din1 = new DebugInputGate();
        DebugOutputGate dout1 = new DebugOutputGate();

        circ2.addVertex(inv1);
        circ2.addVertex(inv2);
        circ2.addVertex(nor1);
        circ2.addVertex(din1);
        circ2.addVertex(dout1);

        Wire nor1_in0 = circ2.addEdge(din1, nor1);
        nor1.setWire(nor1_in0, 0);
        Wire nor1_in1 = circ2.addEdge(inv2, nor1);
        nor1.setWire(nor1_in1, 1);
        Wire inv1_in = circ2.addEdge(nor1, inv1);
        inv1.setWire(inv1_in, 0);
        Wire inv2_in = circ2.addEdge(inv1, inv2);
        inv2.setWire(inv2_in, 0);
        Wire dout1_in = circ2.addEdge(inv1, dout1);
        dout1.setWire(dout1_in, 0);

//        sim.addEvent(new SimulationEvent(h1));
//        sim.addEvent(new SimulationEvent(l1));
        sim.setCircuit(circ2);
        din1.setState(State.S1);
        sim.run();
        sim.run();
        sim.run();
        sim.run();
//        sim.DEBUG_RECURSE = true;
        din1.setState(State.S0);
//        sim.run();
        sched.start();
    }

}
