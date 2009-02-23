package org.delta.circuit.gate;

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

}
