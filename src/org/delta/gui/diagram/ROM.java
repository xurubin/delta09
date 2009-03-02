package org.delta.gui.diagram;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.delta.circuit.Component;
import org.delta.circuit.component.RomComponent;
import org.jgraph.graph.GraphConstants;

/**
 * Class to represent the "model" of a ROM in the circuit diagram.
 * @author Group Delta 2009
 */
public class ROM extends DeltaComponent {
	/** Needed for correct serialization. */
	private static final long serialVersionUID = 1L;
	
	private List<Integer> store = new ArrayList<Integer>();

	/**
	 * Creates a new ROM at a default position.
	 */
	public ROM() {
		this(new Point(100,100));
	}
	
	/**
	 * Creates a new ROM at the given position on the screen.
	 * @param position - where to insert the new component.
	 */
	public ROM(Point position) {
		super();
		
		this.replaceUserObject();
		
		this.addInputPort(new Point(0,180),0);
		this.addInputPort(new Point(0,400),1);
		this.addInputPort(new Point(0,610),2);
		this.addInputPort(new Point(0,830),3);
		this.addOutputPort(new Point(GraphConstants.PERMILLE,180),0);
		// Set position based on parameter
		Rectangle2D bounds = new Rectangle2D.Double(position.getX(),position.getY(),60,40);
		GraphConstants.setBounds(this.getAttributes(),bounds);
	}

    /**
     * Mutator method for the contents of the ROM. Replaces the locally stored
     * ROM contents and replaces the userObject so the simulation is kept
     * up to date.
     * @param userStore
     */
	public void setStore(List<Integer> userStore) {
		this.store = userStore;
		this.replaceUserObject();
	}
	
    /**
     * Accessor method for the contents of the ROM.
     * @return a List representation of the ROM's contents.
     */
    public List<Integer> getStore() {
        return this.store;
    }
    
    /**
     * Replaces the userObject with a new one. This is used when copying the cell so that
     * a new component is created on the simulation graph - otherwise we would have two
     * display graph components being represented by just one simulation graph component.
     */
	protected void replaceUserObject() {
		/*
		 * TODO: add default function.
		 */
        Component component = new RomComponent(4, 1, store);
		this.setUserObject(component);
	}
}
