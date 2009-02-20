package org.delta.circuit.component;

import java.util.ArrayList;
import java.util.HashMap;

import org.delta.circuit.Circuit;
import org.delta.circuit.Component;
import org.delta.circuit.ComponentWire;
import org.delta.circuit.Gate;
import org.delta.circuit.Wire;
import org.delta.circuit.gate.GateFactory;
import org.delta.logic.Nor;

public class SrLatchComponent extends Component {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SrLatchComponent() {
        super(2, 2);
        
        final Circuit circuit = getCircuit();
        
        final Gate nor0 = GateFactory.createGate(Nor.class, 2);
        final Gate nor1 = GateFactory.createGate(Nor.class, 2);
        
        circuit.addVertex(nor0);
        circuit.addVertex(nor1);
        
        final Wire nor0ToNor1 = circuit.addEdge(nor0, nor1);
        final Wire nor1ToNor0 = circuit.addEdge(nor1, nor0);
        
        nor0.setWire(nor1ToNor0, 1);
        nor1.setWire(nor0ToNor1, 0);
        
        addInputGate(0, nor0, 0);
        addInputGate(1, nor1, 1);

        setOutputGate(0, nor0);
        setOutputGate(1, nor1);
    }

    public String getVerilogMethod(String name, ArrayList<String> inputWires,
    		HashMap<ComponentWire,String> outputWires, HashMap<ComponentWire,String> inputGates,
            ArrayList<Gate> outputGates) {
		String rs_latch = "rs_latch " + name + "("+ outputWires.get(0) + ", " + outputWires.get(1)
							+ ", " + inputWires.get(0) + ", " + inputWires.get(1) + ");";
		return rs_latch;
    }

}
