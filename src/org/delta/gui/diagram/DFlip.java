package org.delta.gui.diagram;

import java.awt.Point;
import java.awt.geom.Rectangle2D;

import org.delta.circuit.Component;
import org.delta.circuit.component.DFlipFlopComponent;
import org.jgraph.graph.GraphConstants;

/**
 * Class to represent the "model" of a DFlip (D flip flop) in the circuit diagram.
 * @author Group Delta 2009
 */
public class DFlip extends DeltaComponent {
	/** Needed for correct serialization. */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new NotGate at a default position.
	 */
	public DFlip() {
		this(new Point(100,100));
	}
	
	/**
	 * Creates a new DFlip at the given position on the screen.
	 * @param position - where to insert the new component.
	 */
	public DFlip(Point position) {
		super();
		
		this.replaceUserObject();
		
		this.addInputPort(new Point(0,490),0);
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
        Component component = new DFlipFlopComponent();
		this.setUserObject(component);
	}
}
