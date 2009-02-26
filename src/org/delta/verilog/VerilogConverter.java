package org.delta.verilog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import org.delta.circuit.Circuit;
import org.delta.circuit.Component;
import org.delta.circuit.ComponentGraph;
import org.delta.circuit.ComponentWire;
import org.delta.circuit.Gate;
import org.delta.circuit.Wire;
import org.delta.gui.MainWindow;
import org.delta.util.Unzip;

/**
 * Converts our data structure into Verilog.
 * 
 * @author Group Delta 2009
 * 
 */
public class VerilogConverter {

	private static String verilogProjectFolder = "org/delta/verilog/verilog_src.zip";
	private static String verilogTopLevel = "verilog/test_verilog";

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
	public static String convertToVerilog(ComponentGraph circuit) {
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
			System.out.println(saveFolder.getAbsolutePath() + "/");
			Unzip.unzip(VerilogConverter.verilogProjectFolder, saveFolder.getAbsolutePath() + "\\");
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
			
		} 
		catch(FileNotFoundException e) {
			JOptionPane.showMessageDialog(MainWindow.get(), "Can't write to specified folder.", "Error Saving Verilog", JOptionPane.ERROR_MESSAGE);
			return;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
