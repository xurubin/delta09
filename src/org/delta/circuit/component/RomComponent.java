package org.delta.circuit.component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.delta.circuit.Component;
import org.delta.circuit.ComponentGraph;
import org.delta.circuit.ComponentWire;

public class RomComponent extends Component {
  /**
	 * UID for serialisation. 
	 */
	private static final long serialVersionUID = 1L;
	private List<Integer> presetValue;

	public RomComponent(int selectCount, int outputCount,
            List<Integer> store) {
        super(selectCount, outputCount);
        this.presetValue = store;
        
        ComponentGraph graph = new ComponentGraph(false);
        
        RamComponent rom = new RamComponent(selectCount, outputCount);
        graph.addVertex(rom);

        rom.preset(store);
        
        // Set inputs.
        List<Set<ComponentPort>> inputs =
            new ArrayList<Set<ComponentPort>>(getInputCount());
        for (int i = 0; i < getInputCount(); ++i) {
            Set<ComponentPort> list = new HashSet<ComponentPort>(1);
            list.add(new ComponentPort(rom, i + 2));
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
    
    @Override
	public String getVerilogMethod(String name, LinkedHashMap<ComponentWire, String> inputWires,
			LinkedHashMap<ComponentWire, String> outputWires) {

		ComponentWire[] output1 = new ComponentWire[this.getOutputWires(0).size()];
		this.getOutputWires(0).toArray(output1);

		String mainOutput1 = output1.length > 0 ? outputWires.get(output1[0]) : "";
		
		String data = "assign " + name + "_data_wire  = 16'b";
		for(int i = 15; i >= 0; i--) {
			data += (this.presetValue.get(i) == 0 ? "0" : "1");
		}
		data += ";\n";
		String result = "rom16 " + name + "(" + mainOutput1 + ", "
				+ inputWires.get(this.getInputWire(0)) + ", "
				+ inputWires.get(this.getInputWire(1))  + ", "
				+ inputWires.get(this.getInputWire(2))  + ", "
				+ inputWires.get(this.getInputWire(3)) + ", "
				+ name + "_data_wire" + 
				", world_clock);";

		for (int i = 1; i < output1.length; i++) {
			result += "\nassign " + outputWires.get(output1[i]) + " = " + mainOutput1;
		}

		return data + result;
	}

}
