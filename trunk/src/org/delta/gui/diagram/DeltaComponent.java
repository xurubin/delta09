package org.delta.gui.diagram;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import java.awt.geom.Point2D;

public class DeltaComponent extends DefaultGraphCell
{
	/**
    * 
    */
	private static final long serialVersionUID = 1L;
	
	public DeltaComponent()
	{
		super();
	}
	
	public DeltaComponent(Object arg0)
	{
		super(arg0);
	}
	
	protected void addInputPort(Point2D offset)
	{
		DeltaInputPort port = new DeltaInputPort();
		GraphConstants.setOffset(port.getAttributes(),offset);
		add(port);
	}
	
	protected void addOutputPort(Point2D offset)
	{
		DeltaOutputPort port = new DeltaOutputPort();
		GraphConstants.setOffset(port.getAttributes(),offset);
		add(port);
	}
}