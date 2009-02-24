package org.delta.gui.diagram;

import java.awt.Point;
import java.awt.geom.Rectangle2D;

import org.delta.circuit.Component;
import org.delta.circuit.Gate;
import org.delta.circuit.component.GateComponentFactory;
import org.delta.circuit.gate.SwitchGate;
import org.jgraph.graph.GraphConstants;

/**
 * Class to represent the "model" of a constant high input in the circuit diagram.
 * @author Group Delta 2009
 */
public class Switch extends DeltaComponent {
	/** Needed for correct serialization. */
	private static final long serialVersionUID = 1L;
	
	/** Marker for the seven segment display this component represents. */
	private int switchNumber = -1;
	
	/**
	 * Creates a new Switch at a default position.
	 */
	public Switch() {
		this(new Point(100,100));
	}
	
	/**
	 * Creates a new Switch at the given position on the screen.
	 * @param position - where to insert the new component.
	 */
	public Switch(Point position) {
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
	    Gate gate = new SwitchGate(switchNumber);
        Component component = GateComponentFactory.createComponent(gate);
		this.setUserObject(component);
	}
	
	/** Accessor method for the seven segment display's number. */
	public int getSwitchNumber() {
		return this.switchNumber;
	}
	
	/**
	 * Sets the number of the switch that this component represents.
	 * @param number - the number of the switch on the DE2 board.
	 */
	public void setSwitchNumber(int number) {
		this.switchNumber = number;
		this.replaceUserObject();
	}
	
	/** Override toString to display the switch number. */
	@Override
	public String toString() {
		return "SW"+Integer.toString(this.switchNumber);
	}
	
}