package org.delta.circuit.component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.delta.circuit.ClockedComponent;
import org.delta.circuit.Component;
import org.delta.circuit.ComponentGraph;
import org.delta.circuit.ComponentWire;
import org.delta.circuit.Gate;
import org.delta.circuit.gate.GateFactory;
import org.delta.logic.Not;

public class DFlipFlopComponent extends ClockedComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DFlipFlopComponent() {
		super(2, 2);
		
    ComponentGraph graph = new ComponentGraph(false);
    
    Component master = new DLatchComponent();
    Component slave = new DLatchComponent();
    
    Gate invGate = GateFactory.createGate(Not.class);
    Component inv = ComponentFactory.createComponent(invGate);
    
    graph.addVertex(master);
    graph.addVertex(slave);
    graph.addVertex(inv);
    
    ComponentWire masterToSlave = graph.addEdge(master, slave);
    graph.registerEdge(masterToSlave, 0, 1);
    
    ComponentWire invToMaster = graph.addEdge(inv, master);
    graph.registerEdge(invToMaster, 0, 0);
    
    Set<ComponentPort> clockInputs = new HashSet<ComponentPort>(2);
    clockInputs.add(new ComponentPort(inv, 0));
    clockInputs.add(new ComponentPort(slave, 0));
    
    List<Set<ComponentPort>> inputs = new ArrayList<Set<ComponentPort>>(1);
    Set<ComponentPort> input0 = new HashSet<ComponentPort>(1);
    input0.add(new ComponentPort(master, 1));
    inputs.add(input0);
    
    List<ComponentPort> outputs = new ArrayList<ComponentPort>(2);
    outputs.add(new ComponentPort(slave, 0));
    outputs.add(new ComponentPort(slave, 1));
    
    fromComponentGraph(graph, inputs, outputs, clockInputs);
	}

	@Override
	public String getVerilogMethod(String name, LinkedHashMap<ComponentWire, String> inputWires,
			LinkedHashMap<ComponentWire, String> outputWires) {

		ComponentWire[] output1 = new ComponentWire[this.getOutputWires(0).size()];
		this.getOutputWires(0).toArray(output1);
		ComponentWire[] output2 = new ComponentWire[this.getOutputWires(1).size()];
		this.getOutputWires(1).toArray(output2);

		String mainOutput1 = output1.length > 0 ? outputWires.get(output1[0]) : "";
		String mainOutput2 = output2.length > 0 ? outputWires.get(output2[0]) : "";

		String result = "dflip " + name + "(" + mainOutput1 + ", " + mainOutput2 + ", "
				+ inputWires.get(this.getInputWire(0)) + ", world_clock);";

		for (int i = 1; i < output1.length; i++) {
			result += "\nassign " + outputWires.get(output1[i]) + " = " + mainOutput1;
		}

		for (int i = 1; i < output2.length; i++) {
			result += "\nassign " + outputWires.get(output2[i]) + " = " + mainOutput2;
		}

		return result;
	}

}
