package org.delta.verilog;

import static org.junit.Assert.fail;
import junit.framework.Assert;

import org.delta.circuit.Component;
import org.delta.circuit.ComponentGraph;
import org.delta.circuit.ComponentWire;
import org.delta.circuit.component.ComponentFactory;
import org.delta.circuit.component.DFlipFlopComponent;
import org.delta.circuit.gate.GateFactory;
import org.delta.circuit.gate.HighGate;
import org.delta.circuit.gate.LedGate;
import org.delta.circuit.gate.SwitchGate;
import org.delta.logic.And;
import org.delta.logic.Or;
import org.junit.Test;

public class VerilogConverterTest {

	@Test
	public void testConvertToVerilogComponentGraph() {
		ComponentGraph c = new ComponentGraph();
		
		Component andGate = ComponentFactory.createComponent(GateFactory.createGate(And.class, 2));
		Component orGate = ComponentFactory.createComponent(GateFactory.createGate(Or.class, 4));
		Component dFlip = new DFlipFlopComponent();
			
		Component switchOne = ComponentFactory.createComponent(new SwitchGate(1));
		Component highGate = ComponentFactory.createComponent(new HighGate());
		Component ledOne = ComponentFactory.createComponent(new LedGate(1));
		Component ledTwo = ComponentFactory.createComponent(new LedGate(2));
		
		c.addVertex(switchOne);
		c.addVertex(highGate);
		c.addVertex(andGate);
		c.addVertex(orGate);
		c.addVertex(ledOne);
		c.addVertex(dFlip);
		c.addVertex(ledTwo);
		
		ComponentWire w1 = c.addEdge(switchOne, andGate);
		ComponentWire w2 = c.addEdge(highGate, andGate);
		ComponentWire w3 = c.addEdge(andGate, orGate);
		ComponentWire w4 = c.addEdge(switchOne, orGate);
		ComponentWire w5 = c.addEdge(highGate, orGate);
		ComponentWire w6 = c.addEdge(orGate, ledOne);
		ComponentWire w7 = c.addEdge(orGate, dFlip);
		ComponentWire w8 = c.addEdge(dFlip, orGate);
		ComponentWire w9 = c.addEdge(dFlip, ledTwo);
		
		c.registerEdge(w1, 0, 0);
        c.registerEdge(w2, 0, 1);
        c.registerEdge(w3, 0, 0);
		c.registerEdge(w4, 0, 1);
        c.registerEdge(w5, 0, 2);
        c.registerEdge(w6, 0, 0);
        c.registerEdge(w7, 0, 0);
        c.registerEdge(w8, 0, 3);
        c.registerEdge(w9, 1, 0);
        
        String verilog = VerilogConverter.convertToVerilog(c);
        System.out.println(verilog);
        Assert.assertTrue("empty", !verilog.isEmpty());
        Assert.assertTrue("incorrect number of wires", verilog.contains("wire w9"));
        Assert.assertTrue("incorrect number of LEDs", verilog.contains("LEDR[2]"));
        Assert.assertTrue("includes and gate", verilog.contains("and"));
        Assert.assertTrue("includes or gate", verilog.contains("or"));
        Assert.assertTrue("includes dflip gate", verilog.contains("dflip"));
        Assert.assertFalse("doesn't contain any nulls", verilog.contains("null"));
	}

	@Test
	public void testSaveVerilogProject() {
		fail("Not yet implemented");
	}

}
