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
import org.delta.circuit.gate.MemoryGate;
import org.delta.logic.And;
import org.delta.logic.Or;
import org.delta.logic.State;

public class RamComponent extends ClockedComponent {
    /**
     * UID for serialisation.
     */
    private static final long serialVersionUID = 1L;
    private List<MemoryGate> memoryCells = new ArrayList<MemoryGate>();

    public RamComponent(final int selectCount, final int outputCount) {
        super(selectCount + outputCount + 2, outputCount);
        
        // List of inputs.
        List<Set<ComponentPort>> inputs =
            new ArrayList<Set<ComponentPort>>(getInputCount() - 1);
        for (int i = 0; i < getInputCount() - 1; ++i) {
            inputs.add(new HashSet<ComponentPort>());
        }
        
        // List of outputs.
        List<ComponentPort> outputs =
            new ArrayList<ComponentPort>(getOutputCount());
        
        // Set of clock inputs.
        Set<ComponentPort> clockInputs =
            new HashSet<ComponentPort>(getOutputCount());
        
        ComponentGraph graph = new ComponentGraph(false);
        
        Component dec = new DecoderComponent(selectCount);
        graph.addVertex(dec);
        
        // Add decoder inputs.
        for (int i = 0; i < dec.getInputCount(); ++i) {
            inputs.get(i + 1).add(new ComponentPort(dec, i));
        }
        
        final int decoderOutputCount = dec.getOutputCount();
        
        // Simulates bus.
        List<Component> orPlane = new ArrayList<Component>(getOutputCount());
        for (int i = 0; i < getOutputCount(); ++i) {
            Gate orGate = GateFactory.createGate(Or.class, decoderOutputCount);
            Component or = ComponentFactory.createComponent(orGate);
            orPlane.add(or);
            graph.addVertex(or);
            
            // Set as component output.
            outputs.add(new ComponentPort(or, i));
        }
        
        // Or clock and WE and decoder outputs.
        List<Component> weList = new ArrayList<Component>();
        for (int i = 0; i < decoderOutputCount; ++i) {
            Gate andGate = GateFactory.createGate(And.class, 3);
            Component and = ComponentFactory.createComponent(andGate);
            weList.add(and);
            graph.addVertex(and);
            
            // Connect to clock.
            clockInputs.add(new ComponentPort(and, 0));
            // Connect to WE.
            inputs.get(0).add(new ComponentPort(and, 1));
            // Connect to associated decoder output.
            ComponentWire wire = graph.addEdge(dec, and);
            graph.registerEdge(wire, i, 2);
        }
        
        /* Initialise an array of memory arrays where each memory array is one
         * word.
         */
        List<List<Component>> memoryCellArrays =
            new ArrayList<List<Component>>(decoderOutputCount);
        
        // Initialise "words".
        for (int i = 0; i < decoderOutputCount; ++i) {
            List<Component> cellArray = new ArrayList<Component>(outputCount);
            memoryCellArrays.add(cellArray);
        }
        
        // Fill "words" with memoryCells.
        for (int j = 0; j < memoryCellArrays.size(); ++j) {
            List<Component> cellArray = memoryCellArrays.get(j);
            for (int i = 0; i < getOutputCount(); ++i) {
                Gate memoryGate = new MemoryGate();
                ((MemoryGate) memoryGate).setStore(State.S0);
                memoryCells.add((MemoryGate) memoryGate);
                Component memoryCell =
                    ComponentFactory.createComponent(memoryGate); 
                cellArray.add(memoryCell);
                graph.addVertex(memoryCell);
                
                // Connect to AND gate in weList.
                Component weAnd = weList.get(j);
                ComponentWire wireWe = graph.addEdge(weAnd, memoryCell);
                graph.registerEdge(wireWe, 0, 0);
                
                // Connect to data input.
                Set<ComponentPort> inputSet = inputs.get(i + 1 + selectCount);
                inputSet.add(new ComponentPort(memoryCell, 1));
                
                // AND memory cell output and corresponding decoder output.
                Gate andGate = GateFactory.createGate(And.class);
                Component and = ComponentFactory.createComponent(andGate);
                graph.addVertex(and);
                
                ComponentWire decWire = graph.addEdge(dec, and);
                graph.registerEdge(decWire, j, 0);
                
                ComponentWire memoryWire = graph.addEdge(memoryCell, and);
                graph.registerEdge(memoryWire, 0, 1);
                
                // Connect output to orPlane.
                Component or = orPlane.get(i);
                ComponentWire wireOr = graph.addEdge(and, or);
                graph.registerEdge(wireOr, 0, j);
            }
        }
        
        fromComponentGraph(graph, inputs, outputs, clockInputs);
    }
    
    void preset(List<Integer> store) {
        for (int i = 0; i < store.size(); ++i) {
            int result = store.get(i);
            
            for (int j = 0; j < getOutputCount(); ++j) {
                MemoryGate m = memoryCells.get(i*j);
                if ((result & (1 << j)) != 0) {
                    m.setStore(State.S1);
                } else {
                    m.setStore(State.S0);
                }
            }
        }
    }
    
	@Override
	public String getVerilogMethod(String name, LinkedHashMap<ComponentWire, String> inputWires,
			LinkedHashMap<ComponentWire, String> outputWires) {

		ComponentWire[] output1 = new ComponentWire[this.getOutputWires(0).size()];
		this.getOutputWires(0).toArray(output1);

		String mainOutput1 = output1.length > 0 ? outputWires.get(output1[0]) : "";

		String result = "ram16 " + name + "(" + mainOutput1 + ", "
				+ inputWires.get(this.getInputWire(0)) + ", "
				+ inputWires.get(this.getInputWire(1))  + ", "
				+ inputWires.get(this.getInputWire(2))  + ", "
				+ inputWires.get(this.getInputWire(3))  + ", "
				+ inputWires.get(this.getInputWire(4))  + ", "
				+ inputWires.get(this.getInputWire(5))  +
				", world_clock);";

		for (int i = 1; i < output1.length; i++) {
			result += "\nassign " + outputWires.get(output1[i]) + " = " + mainOutput1;
		}


		return result;
	}

}
