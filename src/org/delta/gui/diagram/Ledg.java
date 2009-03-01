package org.delta.gui.diagram;

import java.awt.Point;
import java.awt.geom.Rectangle2D;

import org.delta.circuit.Component;
import org.delta.circuit.Gate;
import org.delta.circuit.component.ComponentFactory;
import org.delta.circuit.gate.LedGate;
import org.jgraph.graph.GraphConstants;

/**
 * Class to represent the "model" of a red LED in the circuit diagram.
 * @author Group Delta 2009
 */
public class Ledg extends DeltaComponent {
	/** Needed for correct serialization. */
	private static final long serialVersionUID = 1L;
	
	/** Marker for the green LED this component represents. */
	private int ledgnumber = -1;
	
	/**
	 * Creates a new green LED at a default position.
	 */
	public Ledg() {
		this(new Point(100,100));
	}
	
	/**
	 * Creates a new green LED at the given position on the screen.
	 * @param position - where to insert the new component.
	 */
	public Ledg(Point position) {
		super();
		
		this.addInputPort(new Point(0,460),0);
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
	    Gate gate = new LedGate(ledgnumber + 18);
        Component component = ComponentFactory.createComponent(gate);
		this.setUserObject(component);
	}
	
	/** Accessor method for the LED's number. */
	public int getLedNumber() {
		return this.ledgnumber;
	}
	
	/**
	 * Sets the number of the green LED that this component represents.
	 * @param number - the number (0-7) of the green LED on the DE2 board.
	 */
	public void setLedgNumber(int number) {
		this.ledgnumber = number;
		this.replaceUserObject();
	}
	
	/** Override toString to display the Led Number. */
	@Override
	public String toString() {
		return "LEDG"+Integer.toString(this.ledgnumber);
	}
	
}