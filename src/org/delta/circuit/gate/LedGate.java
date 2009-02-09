package org.delta.circuit.gate;

import org.delta.logic.Formula;
import org.delta.logic.LedConstant;

public class LedGate extends AbstractOutputGate {
    private int ledNumber;
    
    public LedGate(int ledNumber) {
        super(1);
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

}
