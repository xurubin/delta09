package org.delta.gui.diagram;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import org.jgraph.graph.GraphConstants;

public class OrGate extends DeltaComponent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public OrGate()
	{
		super();
		
		this.addInputPort(new Point(0,GraphConstants.PERMILLE / 4));
		this.addInputPort(new Point(0,3*GraphConstants.PERMILLE / 4));
		this.addOutputPort(new Point(GraphConstants.PERMILLE,GraphConstants.PERMILLE / 2));
//		If not given a position, use default values
		Rectangle2D bounds = new Rectangle2D.Double(100,100,60,40);
		GraphConstants.setBounds(this.getAttributes(),bounds);
	}
	
	public OrGate(Point position)
	{
		super();
		this.addInputPort(new Point(0,GraphConstants.PERMILLE / 4));
		this.addInputPort(new Point(0,3*GraphConstants.PERMILLE / 4));
		this.addOutputPort(new Point(GraphConstants.PERMILLE,GraphConstants.PERMILLE / 2));
//		Set position based on parameter
		Rectangle2D bounds = new Rectangle2D.Double(position.getX(),position.getY(),60,40);
		GraphConstants.setBounds(this.getAttributes(),bounds);
	}
}
