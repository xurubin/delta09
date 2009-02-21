package org.delta.circuit.component;

import java.util.HashMap;

import org.delta.circuit.Circuit;
import org.delta.circuit.ClockedComponent;
import org.delta.circuit.ComponentWire;
import org.delta.circuit.Gate;
import org.delta.circuit.Wire;
import org.delta.circuit.gate.InverterGate;

public class DFlipFlopComponent extends ClockedComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DFlipFlopComponent() {
		super(1, 2);
		Circuit circuit = getCircuit();

		ClockedComponent master = new DLatchComponent();
		circuit.addCircuit(master.getCircuit());
		ClockedComponent slave = new DLatchComponent();
		circuit.addCircuit(slave.getCircuit());

		for (GateInputPort gateInputPort : slave.getGateInputPorts(0)) {
			Gate gate = gateInputPort.getGate();
			int inputNumber = gateInputPort.getInputNumber();

			Wire wire = circuit.addEdge(master.getOutputGate(0), gate);
			gate.setWire(wire, inputNumber);
		}

		Gate inv = new InverterGate();
		circuit.addVertex(inv);

		for (GateInputPort gateInputPort : master.getClockInputList()) {
			Gate gate = gateInputPort.getGate();
			int inputNumber = gateInputPort.getInputNumber();

			Wire wire = circuit.addEdge(inv, gate);
			gate.setWire(wire, inputNumber);
		}

		addClockInput(inv, 0);

		for (GateInputPort gateInputPort : slave.getClockInputList()) {
			Gate gate = gateInputPort.getGate();
			int inputNumber = gateInputPort.getInputNumber();

			addClockInput(gate, inputNumber);
		}

		for (GateInputPort gateInputPort : master.getGateInputPorts(0)) {
			Gate gate = gateInputPort.getGate();
			int gateInputNumber = gateInputPort.getInputNumber();

			addInputGate(0, gate, gateInputNumber);
		}

		setOutputGate(0, slave.getOutputGate(0));
		setOutputGate(1, slave.getOutputGate(1));
	}

	@Override
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

		String result = "dflip " + name + "(" + mainOutput1 + ", "
				+ mainOutput2 + ", " + inputWires.get(this.getInputWire(0))
				+ ", world_clock);";

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
