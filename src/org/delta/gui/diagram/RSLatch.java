package org.delta.gui.diagram;

import java.awt.Point;
import java.awt.geom.Rectangle2D;

import org.delta.circuit.Component;
import org.delta.circuit.component.SrLatchComponent;
import org.jgraph.graph.GraphConstants;

/**
 * Class to represent the "model" of a RSLatch in the circuit diagram.
 * @author Group Delta 2009
 */
public class RSLatch extends DeltaComponent {
	/** Needed for correct serialization. */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new RSLatch at a default position.
	 */
	public RSLatch() {
		this(new Point(100,100));
	}
	
	/**
	 * Creates a new RSLatch at the given position on the screen.
	 * @param position - where to insert the new component.
	 */
	public RSLatch(Point position) {
		super();
		
		this.replaceUserObject();
		
		this.addInputPort(new Point(0,180),0);
		this.addInputPort(new Point(0, 810), 1);
		this.addOutputPort(new Point(GraphConstants.PERMILLE,180),0);
		this.addOutputPort(new Point(GraphConstants.PERMILLE,810),1);
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
        Component component = new SrLatchComponent();
		this.setUserObject(component);
	}
}
