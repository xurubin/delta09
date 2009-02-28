package org.delta.gui.diagram;

import java.awt.Point;
import java.awt.geom.Rectangle2D;

import org.delta.circuit.Component;
import org.delta.circuit.component.RamComponent;
import org.jgraph.graph.GraphConstants;

/**
 * Class to represent the "model" of a RAM in the circuit diagram.
 * @author Group Delta 2009
 */
public class RAM extends DeltaComponent {
	/** Needed for correct serialization. */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new RAM at a default position.
	 */
	public RAM() {
		this(new Point(100,100));
	}
	
	/**
	 * Creates a new RAM at the given position on the screen.
	 * @param position - where to insert the new component.
	 */
	public RAM(Point position) {
		super();
		
		this.replaceUserObject();
		
		this.addInputPort(new Point(0,120),2);
		this.addInputPort(new Point(0,280),3);
		this.addInputPort(new Point(0,430),4);
		this.addInputPort(new Point(0,580),5);
		this.addInputPort(new Point(0,790),1);
		this.addInputPort(new Point(0,920),6);
		this.addOutputPort(new Point(GraphConstants.PERMILLE,180),0);
		// Set position based on parameter
		Rectangle2D bounds = new Rectangle2D.Double(position.getX(),position.getY(),60,40);
		GraphConstants.setBounds(this.getAttributes(),bounds);
	}
	
	/**
	 * Replaces the userObject with a new one. This is used when copying the cell so that
	 * a new component is created on the simulation graph - otherwise we would have two
	 * display graph components being represented by just one simulation graph component.
	 */
	protected void replaceUserObject() {
		/*
		 * TODO: add correct ROM component
		 */
        Component component = new RamComponent(4, 1);
		this.setUserObject(component);
	}
}
