package org.delta.circuit.gate;

import org.delta.logic.Constant;
import org.delta.logic.Formula;
import org.delta.logic.Function;
import org.delta.logic.SsdFunction;

public class SsdGate extends AbstractOutputGate {
    private static final long serialVersionUID = 1L;
    private Function ssd = new SsdFunction();
   
    public SsdGate() {
        super(7);
    }

    @Override
    public Formula getFormula() {
        for (int i = 0; i < getInputCount(); ++i) {
            ssd.setArgument(i, new Constant(getWire(i)));
        }
        
        return ssd;
    }

}
