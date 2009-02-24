package org.delta.circuit.component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.delta.circuit.ClockedComponent;
import org.delta.circuit.Component;
import org.delta.circuit.ComponentGraph;
import org.delta.circuit.ComponentWire;
import org.delta.circuit.Gate;
import org.delta.circuit.gate.GateFactory;
import org.delta.circuit.gate.InverterGate;
import org.delta.logic.And;

public class DLatchComponent extends ClockedComponent {

    public DLatchComponent() {
        super(1, 2);
        
        ComponentGraph graph = new ComponentGraph(false);
        
        Component srlatch = new SrLatchComponent();
        
        Gate invGate = new InverterGate();
        Component inv = ComponentFactory.createComponent(invGate);
        
        Gate and0Gate = GateFactory.createGate(And.class, 2);
        Component and0 = ComponentFactory.createComponent(and0Gate);
        
        Gate and1Gate = GateFactory.createGate(And.class, 2);
        Component and1 = ComponentFactory.createComponent(and1Gate);
        
        graph.addVertex(srlatch);
        graph.addVertex(inv);
        graph.addVertex(and0);
        graph.addVertex(and1);
        
        ComponentWire invToAnd0_1 = graph.addEdge(inv, and0);
        graph.registerEdge(invToAnd0_1, 0, 1);
        
        ComponentWire and0ToLatch_0 = graph.addEdge(and0, srlatch);
        graph.registerEdge(and0ToLatch_0, 0, 0);
        
        ComponentWire and1ToLatch_1 = graph.addEdge(and1, srlatch);
        graph.registerEdge(and1ToLatch_1, 0, 1);
        
        Set<ComponentPort> clockInputs = new HashSet<ComponentPort>(2);
        clockInputs.add(new ComponentPort(and0, 0));
        clockInputs.add(new ComponentPort(and1, 0));
        
        List<Set<ComponentPort>> inputs = new ArrayList<Set<ComponentPort>>(1);
        Set<ComponentPort> input0 = new HashSet<ComponentPort>(2);
        input0.add(new ComponentPort(and1, 1));
        input0.add(new ComponentPort(inv, 0));
        inputs.add(input0);
        
        List<ComponentPort> outputs = new ArrayList<ComponentPort>(2);
        outputs.add(new ComponentPort(srlatch, 0));
        outputs.add(new ComponentPort(srlatch, 1));
        
        fromComponentGraph(graph, inputs, outputs, clockInputs);
    }

}
