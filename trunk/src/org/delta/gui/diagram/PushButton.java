package org.delta.gui.diagram;

import java.awt.Point;
import java.awt.geom.Rectangle2D;

import org.delta.circuit.Component;
import org.delta.circuit.Gate;
import org.delta.circuit.component.ComponentFactory;
import org.delta.circuit.gate.SwitchGate;
import org.jgraph.graph.GraphConstants;

/**
 * Class to represent the "model" of a push button in the circuit diagram.
 * @author Group Delta 2009
 */
public class PushButton extends DeltaComponent {
	/** Needed for correct serialization. */
	private static final long serialVersionUID = 1L;
	
	/** Marker for the push button ("KEY") this component represents. */
	private int pushButtonNumber = -1;
	
	/**
	 * Creates a new PushButton at a default position.
	 */
	public PushButton() {
		this(new Point(100,100));
	}
	
	/**
	 * Creates a new PushButton at the given position on the screen.
	 * @param position - where to insert the new component.
	 */
	public PushButton(Point position) {
		super();
		
		this.replaceUserObject();

		this.addOutputPort(new Point(GraphConstants.PERMILLE,360),0);
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
		Gate gate = new SwitchGate(pushButtonNumber+18);
        Component component = ComponentFactory.createComponent(gate);
		this.setUserObject(component);
	}
	
	/** Accessor method for the push button's number. */
	public int getPushButtonNumber() {
		return this.pushButtonNumber;
	}
	
	/**
	 * Sets the number of the push button that this component represents.
	 * @param number - the number of the push button on the DE2 board.
	 */
	public void setPushButtonNumber(int number) {
		this.pushButtonNumber = number;
		this.replaceUserObject();
	}
	
	/** Override toString to display the push button number. */
	@Override
	public String toString() {
		return "KEY"+Integer.toString(this.pushButtonNumber);
	}
	
}