package org.delta.circuit.gate;

import org.delta.logic.Constant;
import org.delta.logic.Not;

public class ClockGate extends AbstractInputGate {

    public ClockGate() {
    }

    public void tick() {
        setValue( (new Not(new Constant( getValue() ))).evaluate() );
    }    

}
