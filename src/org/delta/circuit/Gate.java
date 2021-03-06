package org.delta.circuit;

import java.io.Serializable;
import java.util.ArrayList;

import org.delta.logic.Formula;
import org.delta.util.BidirectionalIntegerMap;

abstract public class Gate implements Serializable {
    private BidirectionalIntegerMap<Wire> inputMap;
    private static final long serialVersionUID = 1L;

    public Gate(int inputCount) {
        inputMap = new BidirectionalIntegerMap<Wire>(inputCount);
    }

    abstract public Formula getFormula();

    public void setWire(Wire wire, int inputNumber) {
        inputMap.set(inputNumber, wire);
    }
    
    public int removeWire(Wire wire) {
        return inputMap.remove(wire);
    }
    
    public boolean isConnectedTo(Wire wire) {
        return inputMap.containsEntry(wire);
    }

    public Wire getWire(int inputNumber) {
        return inputMap.getEntry(inputNumber);
    }

    public int getInputCount() {
        return inputMap.getSize();
    }
    
    public String getVerilogMethod(String name, ArrayList<String> out,
            ArrayList<String> in) {
    	return "";
    }
    
    public static String constructDefaultVerilogMethod(String type, String name,
            ArrayList<String> out, ArrayList<String> in) {
    	String method = type + " #(1,1) " + name + "(" + out.get(0) + ", ";
    	for(String s : in) {
    		method += s + ", ";
    	}
    	
    	method = method.substring(0, method.length() - 2) + ");";
    	
    	if(out.size() > 1) {
    		String mainOutputWire = out.remove(0);
    		for(String s : out) {
    			method += "\n" + "assign " + s + " = " + mainOutputWire + ";";
    		}
    	}
    	
    	return method;
    }
    
}
