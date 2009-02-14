package org.delta.gui.diagram;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import java.awt.geom.Point2D;

public abstract class DeltaComponent extends DefaultGraphCell {
	/**
    * 
    */
	private static final long serialVersionUID = 1L;
	
	/*public DeltaComponent() {
		super();
	}
	
	public DeltaComponent(Object arg0) {
		super(arg0);
	}*/
	
	protected void addInputPort(Point2D offset) {
		DeltaInputPort port = new DeltaInputPort();
		GraphConstants.setOffset(port.getAttributes(),offset);
		add(port);
	}
	
	protected void addOutputPort(Point2D offset) {
		DeltaOutputPort port = new DeltaOutputPort();
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