package org.delta.circuit.gate;

import java.util.ArrayList;

import org.delta.logic.Formula;
import org.delta.logic.LedConstant;

public class LedGate extends AbstractOutputGate {
    private int ledNumber;
    
    public LedGate(int l) {
        super(1);
    	this.ledNumber = l;
    }
    
    public int getLedNumber() {
        return ledNumber;
    }

    @Override
    public Formula getFormula() {
        LedConstant led = new LedConstant(getWire(0));
        led.setLedNumber(ledNumber);
        
        return led;
    }
    
    public String getVerilogMethod(String name, String out, ArrayList<String> in) {
    	return "assign LEDR[" + this.getLedNumber() + "] = " + in.get(0) + ";";
    }

}
