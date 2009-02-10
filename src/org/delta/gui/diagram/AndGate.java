package org.delta.gui.diagram;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import org.jgraph.graph.GraphConstants;
//import org.jgrapht.ext.*;
//import org.delta.circuit.*;
//import org.delta.circuit.component.*;

public class AndGate extends DeltaComponent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//public AndGate(/*JGraphModelAdapter<Component,ComponentWire> model*/)
	public AndGate()
	{
		super();
		
		this.addInputPort(new Point(0,GraphConstants.PERMILLE / 4));
		this.addInputPort(new Point(0,3*GraphConstants.PERMILLE / 4));
		this.addOutputPort(new Point(GraphConstants.PERMILLE,GraphConstants.PERMILLE / 2));
	}
	
	public AndGate(Point position)
	{
		super();
		//AndComponent component = new AndComponent(2);
		//model.getVertexCell(component);
		
		this.addInputPort(new Point(0,GraphConstants.PERMILLE / 4));
		this.addInputPort(new Point(0,3*GraphConstants.PERMILLE / 4));
		this.addOutputPort(new Point(GraphConstants.PERMILLE,GraphConstants.PERMILLE / 2));
		Rectangle2D bounds = new Rectangle2D.Double(position.getX(),position.getY(),60,40);
		GraphConstants.setBounds(this.getAttributes(),bounds);
	}
}
