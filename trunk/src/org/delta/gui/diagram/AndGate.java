package org.delta.gui.diagram;

import java.awt.Point;
import java.awt.geom.Rectangle2D;

import org.jgraph.graph.GraphConstants;

import org.delta.circuit.Component;
import org.delta.circuit.Gate;
import org.delta.circuit.component.GateComponentFactory;
import org.delta.circuit.gate.GateFactory;
import org.delta.logic.And;

/**
 * Class to represent the "model" of an AndGate in the circuit diagram.
 * @author Group Delta 2009
 */
public class AndGate extends DeltaComponent {
	/** Needed for correct serialization. */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new OrGate at a default position.
	 */
	public AndGate() {
		this(new Point(100,100));
	}
	
	/**
	 * Creates a new AndGate at the given position on the screen.
	 * @param position - where to insert the new component.
	 */
	public AndGate(Point position) {
		super();
		
		this.replaceUserObject();
		
		this.addInputPort(new Point(0,GraphConstants.PERMILLE / 4),0);
		this.addInputPort(new Point(0,3*GraphConstants.PERMILLE / 4),1);
		this.addOutputPort(new Point(GraphConstants.PERMILLE,GraphConstants.PERMILLE / 2),0);
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
	    Gate and = GateFactory.createGate(And.class, 2);
        Component component = GateComponentFactory.createComponent(and);
		this.setUserObject(component);
	}
}
