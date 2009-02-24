package org.delta.circuit.gate;

import java.util.ArrayList;

import org.delta.logic.Constant;
import org.delta.logic.Formula;
import org.delta.logic.SsdFunction;

public class SsdGate extends AbstractOutputGate {
    private static final long serialVersionUID = 1L;
    private SsdFunction ssd = new SsdFunction();
    private int ssdNumber;
   
    public SsdGate(int ssdNumber) {
        super(7);
        this.ssdNumber = ssdNumber;
    }
    
    public void setSsdNumber(int ssdNumber) {
        this.ssdNumber = ssdNumber;
    }

    @Override
    public Formula getFormula() {
        ssd.setSsdNumber(ssdNumber);
        for (int i = 0; i < getInputCount(); ++i) {
            ssd.setArgument(i, new Constant(getWire(i)));
        }
        
        return ssd;
    }
    
    @Override
    public String getVerilogMethod(String name, ArrayList<String> out,
            ArrayList<String> in) {
    	String result = "hexdisplay " + name + "(HEX" + this.ssdNumber + ","; 
    	for(String s : in) {
    		result += s + ", ";
    	}
    	return result.substring(0, result.length() - 2) + ");";
   
    }
}
