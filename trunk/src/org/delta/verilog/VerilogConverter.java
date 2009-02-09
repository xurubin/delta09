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
	private static String verilogTopLevel = "test_verilog";
	
	/**
	 * Constructs a Verilog source code file that can be used to simulate the circuit on the DE2 board. 
	 * @param	circuit	the top-level ComponentGraph used for simulation. This is assumed to be valid.
	 * @return	a string containing the source to a .v verilog program. 
	 */
	private static String convertToVerilog(ComponentGraph circuit) {
		//get list of wires and gates
		Set<ComponentWire> wires = circuit.edgeSet();
		Set<Component> components = circuit.vertexSet();
		
		HashMap<ComponentWire, String> wireNames = new HashMap<ComponentWire,String>();
		int wCounter = 0;
		String wireDecl = "";
		for(ComponentWire w : wires) { 
			String name = "w" + (++wCounter);
			wireNames.put(w, name);
			wireDecl += "wire [1:0] " + name + "; \n";
		}
		
		String componentDecl = "";
		int gCounter = 0;
		for(Component c : components) {
			Set<ComponentWire> outgoingWires = circuit.outgoingEdgesOf(c);
			Set<ComponentWire> incomingWires = circuit.incomingEdgesOf(c);
			
			String name = "g" + (++gCounter);
			
			
			if(c.getInputCount() == 0) {
				//we have an output only component
				/*
				 * TODO: to be confirmed with Justus
				 */
				if(c instanceof SwitchComponent) {
					for(int i = 0; i < c.getOutputCount(); i++) {
						componentDecl += "assign SW[" + ((SwitchComponent) c).getNumber() + "] = "
						+ wireNames.get(c.getOutputWire(i)) + "; \n";
					}
				}
			}
			else if(c.getOutputCount() == 0) {
				//we have an input only component
				/*
				 * TODO: to be confirmed against Justus' code. 
				 */
				if(c instanceof LEDComponent) {
					componentDecl += "assign LEDR[" + ((LEDComponent) c).getNumber() + "] = " +
					wireNames.get(c.getInputWire(0)) + "; \n";
				}
			}
			else {
				componentDecl += c.getVerilogMethod() + " #(1,1) " + name + "(";
		
				/*
				 * Get outputs (in order)
				 */
				for(int i = 0; i < c.getOutputCount(); i++) {
					componentDecl += wireNames.get(c.getOutputWire(i)) + ", ";
				}
				/*
				 * Get inputs (in order)
				 */
				
				for(int i = 0; i < c.getInputCount(); i++) {
					componentDecl += wireNames.get(c.getInputWire(i)) + ", ";
				}
				
				//delete superfluous comma. 
				componentDecl = componentDecl.substring(0, componentDecl.length() - 2);
				componentDecl += "); \n";
			}
			
		}
		
		return wireDecl + componentDecl;
		
	}
	
	/**
	 * Copies a file using nio package.
	 * @param in	source File
	 * @param out	destination File
	 * @throws IOException	If we encounter a file error
	 */
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
	/**
	 * Copies a full directory using copyFile recursively.
	 * @param srcDir	source directory
	 * @param dstDir	destination directory
	 * @throws IOException	If we encounter a file error. 
	 * @see #copyFile(File, File)
	 */
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
	
	/**
	 * Saves a circuit to a file in Verilog format
	 * @param saveFolder	folder to save circuit. 
	 * @param c	CompoentGraph object. This is assumed to be a valid circuit. 
	 * @see #convertToVerilog(Circuit)
	 * @see #copyDirectory(File, File)
	 */
	public static void saveVerilogProject(File saveFolder, ComponentGraph c) {
		try {
			//copy verilog project to saveFolder
			copyDirectory(VerilogConverter.verilogProjectFolder, saveFolder);
			//open main file for modification.
			
			File mainVerilogFile = new File(saveFolder, verilogTopLevel + ".v");
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
	
	
	/**
	 * Demo main function.
	 * @param args	command line arguments.
	 */
	public static void main(String[] args) {
		//some simple demo code. 
		
        
        
		
		//System.out.println(convertToVerilog(circ));
		//saveVerilogProject(new File("verilog_test/"),circ);
	}
}
