package org.delta.gui.diagram;

import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.GraphConstants;

import org.delta.circuit.ComponentWire;

/**
 * Custom Edge implementation that help the Edge look like a wire on the diagram.
 * Also uses the userObject to translate between the circuit diagram and the
 * simulation graph.
 * @author Group Delta 2009
 */
public class DeltaEdge extends DefaultEdge {
	/**	Needed for correct serialization. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor a new DeltaEdge with approriate lineStyle, routing, lineWidth and
	 * a ComponentWire as a userObject for graph translation.
	 */
	public DeltaEdge() {
		super();
		
		// Set userObject to allow translation to simulation graph
		this.setUserObject(new ComponentWire());
		
		// Remove routing so placement of wires is handled only by the user
		GraphConstants.setRemoveAttributes(this.getAttributes(), new Object[] {GraphConstants.ROUTING});
		
		// Set style of line
		GraphConstants.setLineWidth(this.getAttributes(), 2.5f);
	}
	
	/**
	 * Override this method so that no label is displayed on the Edge.
	 * Otherwise by default the string value of every Edge's userObject
	 * is displayed on the Edge.
	 */
	@Override
	public String toString() {
		return null;
	}
	
	/**
	 * Replaces the userObject with a new one. This is used when copying the Edge so that
	 * a new ComponentWire is created on the simulation graph.
	 */
	protected void replaceUserObject() {
		this.setUserObject(new ComponentWire());
	}
	
}