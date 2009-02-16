package org.delta.gui.diagram;

import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.GraphConstants;

import org.delta.circuit.ComponentWire;

/**
 * @author Christopher Wilson
 * Custom Edge implementation that help the Edge look like a wire on the diagram.
 * Also uses the userObject to translate between the circuit diagram and the
 * simulation graph.
 */
public class DeltaEdge extends DefaultEdge
{
	/**	Needed for correct serialization. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor a new DeltaEdge with approriate lineStyle, routing, lineWidth and
	 * a ComponentWire as a userObject for graph translation.
	 */
	public DeltaEdge()
	{
		super();
		
		this.setUserObject(new ComponentWire());
		
		GraphConstants.setLineStyle(this.getAttributes(), GraphConstants.STYLE_ORTHOGONAL);
		//GraphConstants.setRouting(this.getAttributes(), GraphConstants.ROUTING_SIMPLE);
		GraphConstants.setRemoveAttributes(this.getAttributes(), new Object[] {GraphConstants.ROUTING});
		GraphConstants.setLineWidth(this.getAttributes(), 2);
		GraphConstants.setMoveable(this.getAttributes(), true);
	}
	
	/**
	 * Override this method so that nothing is displayed. Otherwise the string
	 * value of every Edge's userObject would be displayed on the Edge.
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