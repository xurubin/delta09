package org.delta.gui.diagram;

import java.awt.geom.Point2D;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

/**
 * Abstract class for all circuit diagram components. Is used to define a common
 * method for adding input/output ports, overriding the toString method and
 * ensuring all components have a method to keep their user objects updated.
 * @author Group Delta 2009
 */
public abstract class DeltaComponent extends DefaultGraphCell {
	/** Needed for correct serialization. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Add an input port to this component.
	 * @param offset - where around the edge of the component the port should be positioned. 
	 * @param number - the number of the port on the component.
	 */
	protected void addInputPort(Point2D offset, int number) {
		DeltaInputPort port = new DeltaInputPort(number);
		GraphConstants.setOffset(port.getAttributes(),offset);
		add(port);
	}
	
	/**
	 * Add an output port to this component.
	 * @param offset - where around the edge of the component the port should be positioned. 
	 * @param number - the number of the port on the component.
	 */
	protected void addOutputPort(Point2D offset, int number) {
		DeltaOutputPort port = new DeltaOutputPort(number);
		GraphConstants.setOffset(port.getAttributes(),offset);
		add(port);
	}
	
	/**
	 * Override this method so that nothing is displayed. Otherwise the string
	 * value of every component's userObject would be displayed on the cell. 
	 */
	@Override
	public String toString() {
		return null;
	}
	
	/**
	 * Abstract method that will be used when copying components in order to
	 * replace the userObject (grapht component) with a new one. It will only be 
	 */
	abstract protected void replaceUserObject();
}