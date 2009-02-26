package org.delta.circuit.component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.delta.circuit.Component;
import org.delta.circuit.ComponentGraph;
import org.delta.circuit.Gate;
import org.delta.circuit.gate.GateFactory;
import org.delta.circuit.gate.MemoryFunction;
import org.delta.logic.And;

public class RomComponent extends Component {

    public RomComponent(int selectCount, int outputCount,
            List<Integer> store) {
        super(selectCount, outputCount);
        
        ComponentGraph graph = new ComponentGraph(false);
        
        RamComponent rom = new RamComponent(selectCount, outputCount);
        graph.addVertex(rom);
        
        rom.preset(store);
        
        // Set inputs.
        List<Set<ComponentPort>> inputs =
            new ArrayList<Set<ComponentPort>>(getInputCount());
        for (int i = 0; i < getInputCount(); ++i) {
            Set<ComponentPort> list = new HashSet<ComponentPort>(1);
            list.add(new ComponentPort(rom, i + 1));
            inputs.add(list);
        }
        
        // Set outputs.
        List<ComponentPort> outputs =
            new ArrayList<ComponentPort>(getOutputCount());
        for (int i = 0; i < getOutputCount(); ++i) {
            outputs.add(new ComponentPort(rom, i));
        }
        
        fromComponentGraph(graph, inputs, outputs);
    }

}
