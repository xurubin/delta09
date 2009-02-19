package org.delta.verilog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.delta.circuit.Circuit;
import org.delta.circuit.Component;
import org.delta.circuit.ComponentGraph;
import org.delta.circuit.ComponentWire;
import org.delta.circuit.Gate;
import org.delta.circuit.Wire;
import org.delta.circuit.component.GateComponentFactory;
import org.delta.circuit.component.SrLatchComponent;
import org.delta.circuit.gate.LedGate;
import org.delta.circuit.gate.SwitchGate;

/**
 * Converts our data structure into Verilog.
 * 
 * @author Group Delta 2009
 * 
 */
public class VerilogConverter {

	private static File verilogProjectFolder = new File("verilog/");
	private static String verilogTopLevel = "test_verilog";

	/**
	 * converts a named circuit into a verilog declaration. 
	 * @param name
	 * @param circuit
	 * @param inputWires
	 * @param outputWires
	 * @param inputGates
	 * @param outputGates
	 * @return
	 */
	public static String convertToVerilog(String name, Circuit circuit,
			ArrayList<String> inputWires, ArrayList<String> outputWires, ArrayList<Gate> inputGates, ArrayList<Gate> outputGates) {
		Set<Wire> wires = circuit.edgeSet();
		Set<Gate> gates = circuit.vertexSet();
		
		String verilog = "";
		HashMap<Wire, String> wireNames = new HashMap<Wire, String>();
		int wCounter = 0;
		for(Wire w : wires) {
			String wireName = name + "_w" + wCounter++;
			wireNames.put(w, wireName);
			verilog += "wire " + wireName + ";";
		}
		
		int gCounter = 0;
		for(Gate g : gates) {
			ArrayList<String> inputs = new ArrayList<String>();
			
			for(int i = 0; i < g.getInputCount(); i++) {
				inputs.add(wireNames.get(g.getWire(i)));
			}
			
			String output = "";
			int gateIndex;
			
			if(circuit.outgoingEdgesOf(g).size() != 0) {
				output += wireNames.get(circuit.outgoingEdgesOf(g).iterator().next());
			}
			else if((gateIndex = outputGates.indexOf(g)) != -1) {
				//i.e. we are an output gate from this circuit
				output += outputWires.get(gateIndex);
			}
			
			if(circuit.incomingEdgesOf(g).size() == 0) {
				if((gateIndex = inputGates.indexOf(g)) != -1) {
					//i.e. we are an input gate from this circuit
					inputs.add(inputWires.get(gateIndex));
				}
			}
			
			verilog += g.getVerilogMethod(name + "_g" + gCounter++, output, inputs);
		}
		return verilog;
	}

	/**
	 * Constructs a Verilog source code file that can be used to simulate the
	 * circuit on the DE2 board.
	 * 
	 * @param circuit
	 *            the top-level ComponentGraph used for simulation. This is
	 *            assumed to be valid.
	 * @return a string containing the source to a .v verilog program.
	 */
	private static String convertToVerilog(ComponentGraph circuit) {
		// get list of wires and gates
		Set<ComponentWire> wires = circuit.edgeSet();
		Set<Component> components = circuit.vertexSet();

		HashMap<ComponentWire, String> wireNames = new HashMap<ComponentWire, String>();

		int wCounter = 0;
		String wireDecl = "";
		for (ComponentWire w : wires) {
			String name = "w" + (++wCounter);
			wireNames.put(w, name);
			wireDecl += "wire " + name + "; \n";
		}

		String componentDecl = "";
		int cCounter = 0;
		for (Component c : components) {
			ArrayList<String> output = new ArrayList<String>();
			ArrayList<Gate> outputGates = new ArrayList<Gate>();
			ArrayList<String> input = new ArrayList<String>();
			ArrayList<Gate> inputGates = new ArrayList<Gate>();
			
			/*
			 * Get outputs and inputs (in order)
			 */
			for (int i = 0; i < c.getOutputCount(); i++) {
				output.add(wireNames.get(c.getOutputWire(i)));
				outputGates.add(c.getOutputGate(i));
			}
			for (int i = 0; i < c.getInputCount(); i++) {
				input.add(wireNames.get(c.getInputWire(i)));
				inputGates.add(c.getInputGate(i));
			}

			componentDecl += c.getVerilogMethod("component_" + cCounter++, input, output, inputGates, outputGates);
			componentDecl += "\n";

		}

		return wireDecl + componentDecl;

	}

	/**
	 * Copies a file using nio package.
	 * 
	 * @param in
	 *            source File
	 * @param out
	 *            destination File
	 * @throws IOException
	 *             If we encounter a file error
	 */
	private static void copyFile(File in, File out) throws IOException {
		FileChannel inChannel = new FileInputStream(in).getChannel();
		FileChannel outChannel = new FileOutputStream(out).getChannel();
		try {
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} catch (IOException e) {
			/*
			 * TODO Proper exception handling
			 */
			e.printStackTrace();
		} finally {
			if (inChannel != null)
				inChannel.close();
			if (outChannel != null)
				outChannel.close();
		}
	}

	/**
	 * Copies a full directory using copyFile recursively.
	 * 
	 * @param srcDir
	 *            source directory
	 * @param dstDir
	 *            destination directory
	 * @throws IOException
	 *             If we encounter a file error.
	 * @see #copyFile(File, File)
	 */
	private static void copyDirectory(File srcDir, File dstDir)
			throws IOException {
		if (srcDir.isDirectory()) {
			if (!dstDir.exists())
				dstDir.mkdir();

			String[] children = srcDir.list();
			for (int i = 0; i < children.length; i++) {
				copyDirectory(new File(srcDir, children[i]), new File(dstDir,
						children[i]));
			}
		} else
			copyFile(srcDir, dstDir);
	}

	/**
	 * Saves a circuit to a file in Verilog format
	 * 
	 * @param saveFolder
	 *            folder to save circuit.
	 * @param c
	 *            CompoentGraph object. This is assumed to be a valid circuit.
	 * @see #convertToVerilog(Circuit)
	 * @see #copyDirectory(File, File)
	 */
	public static void saveVerilogProject(File saveFolder, ComponentGraph c) {
		try {
			// copy verilog project to saveFolder
			copyDirectory(VerilogConverter.verilogProjectFolder, saveFolder);
			// open main file for modification.

			File mainVerilogFile = new File(saveFolder, verilogTopLevel + ".v");
			FileReader inputReader = new FileReader(mainVerilogFile);
			BufferedReader bufferedReader = new BufferedReader(inputReader);

			ArrayList<String> buffer = new ArrayList<String>();
			while (bufferedReader.ready()) {
				String line = bufferedReader.readLine();
				if (line.matches("(.*)%%PLACEHOLDER%%(.*)")) {
					String[] circuitVerilog = convertToVerilog(c).split("\n");
					for (String s : circuitVerilog) {
						buffer.add(s);
					}
				} else
					buffer.add(line);
			}
			inputReader.close();

			FileWriter fileWriter = new FileWriter(mainVerilogFile);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			for (String s : buffer) {
				bufferedWriter.write(s);
				bufferedWriter.newLine();
			}

			bufferedWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Demo main function.
	 * 
	 * @param args
	 *            command line arguments.
	 */
	public static void main(String[] args) {
		// some simple demo code.
		ComponentGraph c = new ComponentGraph();
		
		SrLatchComponent sr = new SrLatchComponent();
		Component switchOne = GateComponentFactory.createComponent(new SwitchGate(1));
		Component switchTwo = GateComponentFactory.createComponent(new SwitchGate(2));
		Component ledOne = GateComponentFactory.createComponent(new LedGate(1));
		Component ledTwo = GateComponentFactory.createComponent(new LedGate(2));
		
		c.addVertex(switchOne);
		c.addVertex(switchTwo);
		c.addVertex(sr);
		c.addVertex(ledOne);
		c.addVertex(ledTwo);
		
		c.addEdge(switchOne, sr);
		c.addEdge(switchTwo, sr);
		c.addEdge(sr, ledOne);
		c.addEdge(sr, ledTwo);
		
		System.out.println(VerilogConverter.convertToVerilog(c));
		
	}
}
