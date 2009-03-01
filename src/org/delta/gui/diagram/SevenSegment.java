package org.delta.gui.diagram;

import java.awt.Point;
import java.awt.geom.Rectangle2D;

import org.delta.circuit.Component;
import org.delta.circuit.Gate;
import org.delta.circuit.component.ComponentFactory;
import org.delta.circuit.gate.SsdGate;
import org.jgraph.graph.GraphConstants;

/**
 * Class to represent the "model" of a 7 sgement display in the circuit diagram.
 * @author Group Delta 2009
 */
public class SevenSegment extends DeltaComponent {
	/** Needed for correct serialization. */
	private static final long serialVersionUID = 1L;
	
	/** Marker for the seven segment display this component represents. */
	private int sevenSegmentNumber = -1;

	/**
	 * Creates a new Switch at a default position.
	 */
	public SevenSegment() {
		this(new Point(100,100));
	}
	
	/**
	 * Creates a new Seven segment display at the given position on the screen.
	 * @param position - where to insert the new component.
	 */
	public SevenSegment(Point position) {
		super();
		
		this.replaceUserObject();

		this.addInputPort(new Point(GraphConstants.PERMILLE,430),1);
		this.addInputPort(new Point(GraphConstants.PERMILLE,670),6);
		this.addInputPort(new Point(GraphConstants.PERMILLE,220),2);
		this.addInputPort(new Point(0,150),0);
		this.addInputPort(new Point(0,350),5);
		this.addInputPort(new Point(0,530),4);
		this.addInputPort(new Point(0,730),3);
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
	   	Gate gate = new SsdGate(sevenSegmentNumber);
        Component component = ComponentFactory.createComponent(gate);
		this.setUserObject(component);
	}
	
	/** Accessor method for the seven segment display's number. */
	public int getSevenSegmentNumber() {
		return this.sevenSegmentNumber;
	}
	
	/**
	 * Sets the number of the seven segment display that this component represents.
	 * @param number - the number of the seven segment display on the DE2 board.
	 */
	public void setSevenSegmentNumber(int number) {
		this.sevenSegmentNumber = number;
		this.replaceUserObject();
	}
	
	/** Override toString to display the seven segment display number. */
	@Override
	public String toString() {
		return "HEX"+Integer.toString(this.sevenSegmentNumber);
	}
}
