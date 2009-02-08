package org.delta.verilog;

import org.delta.circuit.*;
import org.delta.circuit.gate.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashMap;

/**
 * Converts our data structure into Verilog. 
 * @author Group Delta 2009
 *
 */
public class VerilogConverter {
	
	private static File verilogProjectFolder = new File("verilog/");
	
	/**
	 * Constructs a Verilog source code file that can be used to simulate the circuit on the DE2 board. 
	 * @param	circuit	the top-level circuit used for simulation. This is assumed to be valid.
	 * @return	a string containing the source to a .v verilog program. 
	 */
	public static String convertToVerilog(Circuit circuit) {
		//get list of wires and gates
		Set<Wire> wires = circuit.edgeSet();
		Set<Gate> gates = circuit.vertexSet();
		
		HashMap<Wire, String> wireNames = new HashMap<Wire,String>();
		int wCounter = 0;
		String wireDecl = "";
		for(Wire w : wires) { 
			String name = "w" + (++wCounter);
			wireNames.put(w, name);
			wireDecl += "wire [1:0] " + name + "; \n";
		}
		
		String gateDecl = "";
		int gCounter = 0;
		int cCounter = 0;
		for(Gate g : gates) {
			Set<Wire> outgoingWires = circuit.outgoingEdgesOf(g);
			Set<Wire> incomingWires = circuit.incomingEdgesOf(g);
			
			String name = "g" + (++gCounter);
			
			if(outgoingWires.isEmpty()) {
				//we have an input only gate
				if(g instanceof DebugOutputGate) {
					/*
					 * TODO Need to get output location from class. 
					 */
					gateDecl += "assign PLACEHOLDER = " + wireNames.get(incomingWires.iterator().next()) + "; \n";
				}
				/*
				 * Need to add handling for output to LEDs.
				 */
			}
			else if(incomingWires.isEmpty()) {
				//we have an output only gate 
				String clockName = "c" + (++cCounter);
				
				if(g instanceof ClockGate) {
					/*
					 * TODO: Get clock properties.
					 */
					int mult = 0;
					int div = 0;
					gateDecl += "get_clock(CLOCK_50, " + mult + ", " + div + ", " + clockName + ") \n";
				}
				
				gateDecl += "assign " + wireNames.get(outgoingWires.iterator().next()) + " = ";
				
				if(g instanceof HighGate) {
					gateDecl += "1'b1";
				}
				else if(g instanceof LowGate) {
					gateDecl += "1'b0";
				}
				else if(g instanceof ClockGate) {
					gateDecl += clockName;
				}
				gateDecl += "; \n";
				/*
				 * TODO Need to add handling for input from clock and switches. 
				 */
			}
			else {
				//we have a normal gate. 
				gateDecl += g.getVerilogGateName() + " #(1,1) " + name + "(";
				
				for(Wire o : outgoingWires) {
					gateDecl += wireNames.get(o) + ", ";
				}
				for(Wire i : incomingWires) {
					gateDecl += wireNames.get(i) + ", ";
				}
				//delete superfluous comma. 
				gateDecl = gateDecl.substring(0, gateDecl.length() - 2);
				gateDecl += "); \n";
			}
			
		}
		
		return wireDecl + gateDecl;
		
	}
	
	private static void copyFile(File in, File out) throws IOException {
	    FileChannel inChannel = new FileInputStream(in).getChannel();
	    FileChannel outChannel = new FileOutputStream(out).getChannel();
	    try {
	        inChannel.transferTo(0, inChannel.size(), outChannel);
	    } 
	    catch (IOException e) {
	        /*
	         * TODO Proper exception handling
	         */
	    	e.printStackTrace();
	    }
	    finally {
	        if (inChannel != null) inChannel.close();
	        if (outChannel != null) outChannel.close();
	    }
	}

	private static void copyDirectory(File srcDir, File dstDir) throws IOException {
		if (srcDir.isDirectory()) {
			if (!dstDir.exists()) dstDir.mkdir();
			
			String[] children = srcDir.list();
			for (int i=0; i<children.length; i++) {
				copyDirectory(new File(srcDir, children[i]), new File(dstDir, children[i]));
			}
		}
		else copyFile(srcDir, dstDir);
		}
	
	public static void saveVerilogProject(File saveFolder, Circuit c) {
		try {
			//copy verilog project to saveFolder
			copyDirectory(VerilogConverter.verilogProjectFolder, saveFolder);
			//open main file for modification.
			
			File mainVerilogFile = new File(saveFolder, "test_verilog.v");
			FileReader inputReader = new FileReader(mainVerilogFile);
			BufferedReader bufferedReader = new BufferedReader(inputReader);
			
			ArrayList<String> buffer = new ArrayList<String>();
			while(bufferedReader.ready()) {
				String line = bufferedReader.readLine();
				if(line.matches("(.*)%%PLACEHOLDER%%(.*)")) {
						String[] circuitVerilog = convertToVerilog(c).split("\n");
						for(String s : circuitVerilog) {
							buffer.add(s);
						}
				}
				else buffer.add(line);
			}
			inputReader.close();
			
			FileWriter fileWriter = new FileWriter(mainVerilogFile);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			
			for(String s : buffer) {
				bufferedWriter.write(s);
				bufferedWriter.newLine();
			}
			
			bufferedWriter.close();
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		//some simple demo code. 
		Circuit circ = new Circuit();
		
		HighGate h1 = new HighGate();
		LowGate l1 = new LowGate();
		AndGate a1 = new AndGate(2);
		DebugOutputGate d1 = new DebugOutputGate();
		
		circ.addVertex(h1);
		circ.addVertex(l1);
		circ.addVertex(a1);
		circ.addVertex(d1);
		
        Wire a1_in = circ.addEdge(l1, a1);
        Wire a2_in = circ.addEdge(h1, a1);
        Wire out = circ.addEdge(a1, d1);
        a1.setWire(a1_in, 0);
        a1.setWire(a2_in, 1);
        d1.setWire(out, 0);
        
        
		
		//System.out.println(convertToVerilog(circ));
		saveVerilogProject(new File("verilog_test/"),circ);
	}
}
