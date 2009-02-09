package org.delta.circuit.component;

import org.delta.circuit.Component;
import org.delta.circuit.Gate;
import org.delta.circuit.Wire;
import org.delta.circuit.gate.NorGate;

public class SrLatchComponent extends Component {

    public SrLatchComponent() {
        super(2, 2);
        
        final Gate nor1 = new NorGate();
        final Gate nor2 = new NorGate();
        
        final Wire nor1ToNor2 = circuit.addEdge(nor1, nor2);
        final Wire nor2ToNor1 = circuit.addEdge(nor2, nor1);
        
        nor1.setWire(nor2ToNor1, 1);
        nor2.setWire(nor1ToNor2, 0);
        
        

        circuit.addVertex(nor1);
        circuit.addVertex(nor2);
    }

}
