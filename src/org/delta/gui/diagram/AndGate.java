package org.delta.gui.diagram;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import org.jgraph.graph.GraphConstants;

public class AndGate extends DeltaComponent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AndGate()
	{
		super();
		this.addInputPort(new Point(0,GraphConstants.PERMILLE / 4));
		this.addInputPort(new Point(0,3*GraphConstants.PERMILLE / 4));
		this.addOutputPort(new Point(GraphConstants.PERMILLE,GraphConstants.PERMILLE / 2));
		GraphConstants.setBounds(this.getAttributes(),new Rectangle2D.Double(50,50,60,40));
	}
}
