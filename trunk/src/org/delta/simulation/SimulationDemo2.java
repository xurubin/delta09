package org.delta.simulation;

import org.delta.circuit.Circuit;
import org.delta.circuit.Component;
import org.delta.circuit.ComponentGraph;
import org.delta.circuit.ComponentWire;
import org.delta.circuit.component.DebugInputComponent;
import org.delta.circuit.component.DebugOutputComponent;
import org.delta.circuit.component.SrLatchComponent;
import org.delta.logic.State;

public class SimulationDemo2 {

    public static void main(String[] args) {
        Simulator sim = new Simulator();
        
        SimulationScheduler sched = new SimulationScheduler(sim);
        sched.setSimulationFrequency(200L);
        
        ComponentGraph c = new ComponentGraph();
        
        Component srlatch = new SrLatchComponent();
        Component in0 = new DebugInputComponent(State.S1);
        Component in1 = new DebugInputComponent(State.S0);
        Component out = new DebugOutputComponent();
        
        c.addVertex(srlatch);
        c.addVertex(in0);
        c.addVertex(in1);
        c.addVertex(out);
        
        ComponentWire win0 = c.addEdge(in0, srlatch);
        ComponentWire win1 = c.addEdge(in1, srlatch);
        ComponentWire wsr = c.addEdge(srlatch, out);
        
        boolean b1 = c.registerEdge(win0, 0, 0);
        boolean b2 = c.registerEdge(win1, 0, 1);
        boolean b3 = c.registerEdge(wsr, 0, 0);
        
        Circuit circ = c.getCircuit();
        sim.setCircuit(circ);
        sched.start();
    }
}
