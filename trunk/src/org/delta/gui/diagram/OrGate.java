package org.delta.gui.diagram;

import java.awt.Point;
import java.awt.geom.Rectangle2D;

import org.jgraph.graph.GraphConstants;
import org.delta.circuit.component.SrLatchComponent;

/**
 * @author Group Delta 2009
 * Class to represent the "model" of an OrGate in the circuit diagram.
 */
public class OrGate extends DeltaComponent {
	/** Needed for correct serialization. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new OrGate at a default position.
	 */
	public OrGate() {
		this(new Point(100,100));
	}
	
	/**
	 * Creates a new OrGate at the given position on the screen.
	 * @param position - where to insert the new component.
	 */
	public OrGate(Point position) {
		super();
		
		// Set userObject so component is reflected in the simulation graph
		// FIXME: Use correct component as userObject 
		SrLatchComponent component = new SrLatchComponent();
		this.setUserObject(component);
		
		this.addInputPort(new Point(0,GraphConstants.PERMILLE / 4));
		this.addInputPort(new Point(0,3*GraphConstants.PERMILLE / 4));
		this.addOutputPort(new Point(GraphConstants.PERMILLE,GraphConstants.PERMILLE / 2));
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
		// FIXME: Use correct component as userObject
		this.setUserObject(new SrLatchComponent());
	}
}
