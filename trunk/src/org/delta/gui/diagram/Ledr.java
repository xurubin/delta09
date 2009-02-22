package org.delta.gui.diagram;

import java.awt.Point;
import java.awt.geom.Rectangle2D;

import org.jgraph.graph.GraphConstants;

import org.delta.circuit.Component;
import org.delta.circuit.Gate;
import org.delta.circuit.component.GateComponentFactory;
import org.delta.circuit.gate.LedGate;

/**
 * Class to represent the "model" of a red LED in the circuit diagram.
 * @author Group Delta 2009
 */
public class Ledr extends DeltaComponent {
	/** Needed for correct serialization. */
	private static final long serialVersionUID = 1L;
	
	/** Marker for the red LED this component represents. */
	private int ledRnumber;
	
	/**
	 * Creates a new red LED at a default position.
	 */
	public Ledr() {
		this(new Point(100,100));
	}
	
	/**
	 * Creates a new red LED at the given position on the screen.
	 * @param position - where to insert the new component.
	 */
	public Ledr(Point position) {
		super();
		
		// TODO: Choose LEDR number.
		this.setLedRNumber(0);
		this.replaceUserObject();
		
		this.addInputPort(new Point(0,32*GraphConstants.PERMILLE / 50),0);
		this.addOutputPort(new Point(GraphConstants.PERMILLE,32*GraphConstants.PERMILLE / 50),0);
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
		// TODO: Find out how the red/green numbers work in the circuit code.
	    Gate gate = new LedGate(ledRnumber);
        Component component = GateComponentFactory.createComponent(gate);
		this.setUserObject(component);
	}
	
	/**
	 * Sets the number of the red LED that this component represents.
	 * @param number - the number (0-17) of the red LED on the DE2 board.
	 */
	public void setLedRNumber(int number) {
		// TODO: Check this number is not already in use.
		this.ledRnumber = number;
	}
}
