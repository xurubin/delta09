package org.delta.gui.diagram;

import java.awt.Point;
import java.awt.geom.Rectangle2D;

import org.delta.circuit.Component;
import org.delta.circuit.Gate;
import org.delta.circuit.component.GateComponentFactory;
import org.delta.circuit.gate.LedGate;
import org.jgraph.graph.GraphConstants;

/**
 * Class to represent the "model" of a constant high input in the circuit diagram.
 * @author Group Delta 2009
 */
public class SevenSegment extends DeltaComponent {
	/** Needed for correct serialization. */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new Switch at a default position.
	 */
	public SevenSegment() {
		this(new Point(100,100));
	}
	
	/**
	 * Creates a new Switch at the given position on the screen.
	 * @param position - where to insert the new component.
	 */
	public SevenSegment(Point position) {
		super();
		
		this.replaceUserObject();

		this.addInputPort(new Point(GraphConstants.PERMILLE,GraphConstants.PERMILLE / 2),1);
		this.addInputPort(new Point(GraphConstants.PERMILLE,790),6);
		this.addInputPort(new Point(GraphConstants.PERMILLE,230),2);
		this.addInputPort(new Point(0,180),0);
		this.addInputPort(new Point(0,390),5);
		this.addInputPort(new Point(0,630),4);
		this.addInputPort(new Point(0,850),3);
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
		 * TODO: work out 7 segment
		 */
	    Gate gate = new SsdGate();
        Component component = GateComponentFactory.createComponent(gate);
		this.setUserObject(component);
	}
}
