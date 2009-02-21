package org.delta.circuit.component;

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

	public String getVerilogMethod(String name,
			HashMap<ComponentWire, String> inputWires,
			HashMap<ComponentWire, String> outputWires) {

		ComponentWire[] output1 = new ComponentWire[this.getOutputWires(0)
				.size()];
		this.getOutputWires(0).toArray(output1);
		ComponentWire[] output2 = new ComponentWire[this.getOutputWires(1)
				.size()];
		this.getOutputWires(1).toArray(output2);

		String mainOutput1 = output1.length > 0 ? outputWires.get(output1[0])
				: "";
		String mainOutput2 = output2.length > 0 ? outputWires.get(output2[0])
				: "";

		String result = "rs_latch " + name + "(" + mainOutput1 + ", "
				+ mainOutput2 + ", " + inputWires.get(this.getInputWire(0))
				+ "," + inputWires.get(this.getInputWire(1)) + ");";

		for (int i = 1; i < output1.length; i++) {
			result += "\nassign " + outputWires.get(output1[i]) + " = "
					+ mainOutput1;
		}

		for (int i = 1; i < output2.length; i++) {
			result += "\nassign " + outputWires.get(output2[i]) + " = "
					+ mainOutput2;
		}

		return result;
	}

}
