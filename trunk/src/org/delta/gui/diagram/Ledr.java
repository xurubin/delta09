package org.delta.gui.diagram;

import java.awt.Point;
import java.awt.geom.Rectangle2D;

import org.jgraph.graph.GraphConstants;

import org.delta.circuit.Component;
import org.delta.circuit.Gate;
import org.delta.circuit.component.ComponentFactory;
import org.delta.circuit.gate.LedGate;
import org.delta.gui.ComponentPanel;

/**
 * Class to represent the "model" of a red LED in the circuit diagram.
 * @author Group Delta 2009
 */
public class Ledr extends DeltaComponent {
	/** Needed for correct serialization. */
	private static final long serialVersionUID = 1L;
	
	/** Marker for the red LED this component represents. */
	private int ledrnumber = -1;
	
	/** Reference to the model this LED is a member of. */
	private DeltaGraphModel model;
	
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
	    Gate gate = new LedGate(ledrnumber);
        Component component = ComponentFactory.createComponent(gate);
		this.setUserObject(component);
	}
	
	/**
	 * Set the reference to the model - used when inserting into the graph.
	 * @param graphModel - the model this LED is a part of.
	 */
	public void setModel(DeltaGraphModel graphModel) {
		this.model = graphModel;
	}
	
	/** Accessor method for the LED's number. */
	public int getLedNumber() {
		return this.ledrnumber;
	}
	
	/**
	 * Sets the number of the red LED that this component represents.
	 * @param number - the number (0-17) of the red LED on the DE2 board.
	 */
	public void setLedrNumber(int number) {
		if (ledrnumber != -1)
			model.setLedUsed(this.ledrnumber, ComponentPanel.LEDR, false);
		this.ledrnumber = number;
		model.setLedUsed(this.ledrnumber, ComponentPanel.LEDR, true);
		this.replaceUserObject();
	}
	
	/** Override toString to display the Led Number. */
	@Override
	public String toString() {
		return "LEDR"+Integer.toString(this.ledrnumber);
	}
	
}