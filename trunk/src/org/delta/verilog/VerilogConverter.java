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
import java.util.List;
import java.util.Set;

import org.delta.circuit.Circuit;
import org.delta.circuit.Component;
import org.delta.circuit.ComponentGraph;
import org.delta.circuit.ComponentWire;
import org.delta.circuit.Gate;
import org.delta.circuit.Wire;
import org.delta.circuit.component.DFlipFlopComponent;
import org.delta.circuit.component.GateComponentFactory;
import org.delta.circuit.gate.GateFactory;
import org.delta.circuit.gate.HighGate;
import org.delta.circuit.gate.LedGate;
import org.delta.circuit.gate.SwitchGate;
import org.delta.logic.And;
import org.delta.logic.Or;


/*
 * TODO: ADD HASHMAP passing of wire names! 
 */

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
	public static String convertToVerilog(String name, Component component,
			HashMap<ComponentWire,String> inputWires, HashMap<ComponentWire,String> outputWires) {
		Circuit circuit = component.getCircuit();
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
		
		HashMap<Gate,List<Integer>> gateInputNumberMap = new HashMap<Gate, List<Integer>>();
		HashMap<Gate, List<Integer>> gateOutputNumberMap = new HashMap<Gate, List<Integer>>();
		
		//get list of input gates
		for(int i = 0; i < component.getInputCount(); i++) {			
			List<Gate> inputGates = component.getInputGates(i);
			for(Gate g : inputGates) {
				List<Integer> current = gateInputNumberMap.get(g);
				if(current == null) {
					current = new ArrayList<Integer>();
				}
				current.add(i);
				gateInputNumberMap.put(g, current);
			}
		}
		
		//get list of output gates
		/*
		 * TODO: fix this! This is incorrect
		 */
		for(int i = 0; i < component.getOutputCount(); i++) {			
			Gate outputGate  = component.getOutputGate(i);
			List<Integer> current = gateOutputNumberMap.get(outputGate);
			if(current == null) {
				current = new ArrayList<Integer>();
			}
			current.add(i);
			gateOutputNumberMap.put(outputGate, current);
		}
		

		int gCounter = 0;
		for(Gate g : gates) {
			ArrayList<String> inputs = new ArrayList<String>();
			
			if(circuit.incomingEdgesOf(g).size() == 0) {
				List<Integer> listOfConnections = gateInputNumberMap.get(g);
				if(listOfConnections != null) {
					for(Integer i : listOfConnections) {
						inputs.add(inputWires.get(component.getInputWire(i)));
					}
				}
				
			}
			else {
				for(int i = 0; i < g.getInputCount(); i++) {
					inputs.add(wireNames.get(g.getWire(i)));
				}
			}
			
			
			ArrayList<String> output = new ArrayList<String>();
			
			if(circuit.outgoingEdgesOf(g).size() != 0) {
				for(Wire w : circuit.outgoingEdgesOf(g)) {
					output.add(wireNames.get(w));
				}
			}
			else {
				//i.e. we are an output gate from this circuit
//				List<Integer> listOfConnections = gateOutputNumberMap.get(g);
//				if(listOfConnections != null && listOfConnections.size() != 0 && !outputWires.isEmpty()) {
//					for(Integer w : listOfConnections) {
//						output.add(outputWires.get(listOfConnections.get(w)));
//					}
//				}
				for(int i = 0; i < component.getOutputCount(); i++) {
					for(ComponentWire w : component.getOutputWires(i)) {
						output.add(outputWires.get(w));
					}
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
			HashMap<ComponentWire,String> output = new HashMap<ComponentWire,String>();
			HashMap<ComponentWire,String> input = new HashMap<ComponentWire,String>();
			
			/*
			 * Get outputs and inputs (in order)
			 */
			if(circuit.outDegreeOf(c) > 0) {
				for (int i = 0; i < c.getOutputCount(); i++) {
					for(ComponentWire w : c.getOutputWires(i)) {
						output.put(w, wireNames.get(w));
					}
				}
			}
			for (int i = 0; i < c.getInputCount(); i++) {
				input.put(c.getInputWire(i), wireNames.get(c.getInputWire(i)));
			}
			componentDecl += c.getVerilogMethod("component_" + cCounter++, input, output);
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
		
		Component andGate = GateComponentFactory.createComponent(GateFactory.createGate(And.class, 2));
		Component orGate = GateComponentFactory.createComponent(GateFactory.createGate(Or.class, 4));
		Component dFlip = new DFlipFlopComponent();
			
		Component switchOne = GateComponentFactory.createComponent(new SwitchGate(1));
		Component highGate = GateComponentFactory.createComponent(new HighGate());
		Component ledOne = GateComponentFactory.createComponent(new LedGate(1));
		Component ledTwo = GateComponentFactory.createComponent(new LedGate(2));
		
		c.addVertex(switchOne);
		c.addVertex(highGate);
		c.addVertex(andGate);
		c.addVertex(orGate);
		c.addVertex(ledOne);
		c.addVertex(dFlip);
		c.addVertex(ledTwo);
		
		ComponentWire w1 = c.addEdge(switchOne, andGate);
		ComponentWire w2 = c.addEdge(highGate, andGate);
		ComponentWire w3 = c.addEdge(andGate, orGate);
		ComponentWire w4 = c.addEdge(switchOne, orGate);
		ComponentWire w5 = c.addEdge(highGate, orGate);
		ComponentWire w6 = c.addEdge(orGate, ledOne);
		ComponentWire w7 = c.addEdge(orGate, dFlip);
		ComponentWire w8 = c.addEdge(dFlip, orGate);
		ComponentWire w9 = c.addEdge(dFlip, ledTwo);
		
		c.registerEdge(w1, 0, 0);
        c.registerEdge(w2, 0, 1);
        c.registerEdge(w3, 0, 0);
		c.registerEdge(w4, 0, 1);
        c.registerEdge(w5, 0, 2);
        c.registerEdge(w6, 0, 0);
        c.registerEdge(w7, 0, 0);
        c.registerEdge(w8, 0, 3);
        c.registerEdge(w9, 1, 0);
        
        
		System.out.println(VerilogConverter.convertToVerilog(c));

		
	}
}
