package org.delta.gui.diagram;

import org.jgraph.graph.DefaultPort;

/**
 * Subclass of DefaultPort that represents outputs from a component.
 * @author Group Delta 2009
 */
public class DeltaOutputPort extends DefaultPort {
	/** Needed for correct serialization. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Number of port with respect to the component it is attached to.
	 * Used for translation to the simulation graph.
	 */
	private int portNumber;
	
	/**
	 * Create a DeltaInputPort with the given portNumber.
	 * @param number - the number of this port with respect to its component.
	 */
	public DeltaOutputPort(int number) {
		super();
		portNumber = number;
	}
	
	/**
	 * Accessor method for portNumber.
	 * @return The number of this port with respect to it's component.
	 */
	public int getPortNumber() {
		return portNumber;
	}
}
