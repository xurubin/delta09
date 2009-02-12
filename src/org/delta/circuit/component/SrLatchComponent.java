package org.delta.circuit.component;

import java.util.ArrayList;

import org.delta.circuit.Circuit;
import org.delta.circuit.Component;
import org.delta.circuit.Gate;
import org.delta.circuit.Wire;
import org.delta.circuit.gate.NorGate;

public class SrLatchComponent extends Component {

    public SrLatchComponent() {
        super(2, 2);
        
        final Circuit circuit = getCircuit();
        
        final Gate nor0 = new NorGate();
        final Gate nor1 = new NorGate();
        
        circuit.addVertex(nor0);
        circuit.addVertex(nor1);
        
        final Wire nor0ToNor1 = circuit.addEdge(nor0, nor1);
        final Wire nor1ToNor0 = circuit.addEdge(nor1, nor0);
        
        nor0.setWire(nor1ToNor0, 1);
        nor1.setWire(nor0ToNor1, 0);
        
        setInputGate(0, nor0, 0);
        setInputGate(1, nor1, 1);

        setOutputGate(0, nor0);
        setOutputGate(0, nor1);
    }

    public String getVerilogMethod(String name, ArrayList<String> input, ArrayList<String> output) {
		String rs_latch = "rs_latch " + name + "("+ output.get(0) + ", " + output.get(1)
							+ ", " + input.get(0) + ", " + input.get(1) + ");";
		return rs_latch;
    }

}
