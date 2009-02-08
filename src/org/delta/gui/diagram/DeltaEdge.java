package org.delta.gui.diagram;
import org.jgraph.graph.*;

public class DeltaEdge extends DefaultEdge
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DeltaEdge()
	{
		super();
		GraphConstants.setLineStyle(this.getAttributes(), GraphConstants.STYLE_ORTHOGONAL);
		GraphConstants.setRouting(this.getAttributes(), GraphConstants.ROUTING_SIMPLE);
		GraphConstants.setLineWidth(this.getAttributes(), 2);
	}
}