package org.delta.gui.diagram;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.undo.UndoableEdit;

import org.delta.circuit.Component;
import org.delta.circuit.ComponentGraphAdapter;
import org.delta.circuit.ComponentWire;
import org.delta.circuit.ListenableComponentGraph;
import org.delta.gui.ComponentPanel;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.ParentMap;
import org.jgraph.graph.Port;

/**
 * @author Group Delta 2009
 * 
 */
public class DeltaGraphModel extends ComponentGraphAdapter<Component,ComponentWire> {

	/** Needed for correct serialization. */
	private static final long serialVersionUID = 1L;
	
	/** Array indicating which red LEDs are currently in use. */
	private boolean[] ledrArray = new boolean[18];
	
	/** Array indicating which green LEDs are currently in use. */
	private boolean[] ledgArray = new boolean[9];
	
	/** Array indicating which 7-segment displays are currently in use. */
	private boolean[] sevenSegmentArray = new boolean[8];
	
	/**
	 * This constructor simply calls the parent constructor with the given grapht graph.
	 * This allows the JGraphModelAdapter to be created properly.
	 * @param grapht - grapht graph to connect with this model.
	 */
	public DeltaGraphModel(ListenableComponentGraph grapht) {
		super(grapht);
	}
	
	/**
	 * Adapated version of cloneCells that performs a deep clone, i.e.
	 * including each cell's children.
	 * @param model
	 * @param cells
	 * @return
	 */
	public Map<Object,Object> cloneCells(GraphModel model, Object[] cells) {
		Map<Object,Object> map = new Hashtable<Object,Object>();
		// Add Cells to Queue
		for (int i = 0; i < cells.length; i++)
			map.put(cells[i], cloneCell(model, cells[i]));
		// Replace Parent and Anchors
		Iterator<Map.Entry<Object,Object>> it = map.entrySet().iterator();
		Object obj, cell, parent;
		while (it.hasNext()) {
			Map.Entry<Object,Object> entry = (Map.Entry<Object,Object>) it.next();
			obj = entry.getValue();
			cell = entry.getKey();

			// Replaces the cloned cell's parent with the parent's clone
			parent = getParent(cell);
			if (parent != null)
				parent = map.get(parent);
			if (parent != null)
				((DefaultMutableTreeNode) parent)
						.add((DefaultMutableTreeNode) obj);

			// Replaces the anchors for ports
			if (obj instanceof Port) {
				Object anchor = ((Port) obj).getAnchor();
				if (anchor != null)
					((Port) obj).setAnchor((Port) map.get(anchor));
			}
		}
		return map;
	}
	
	/**
	 * Overridden to implement custom userObject cloning. Instead of keeping
	 * the same userObject it calls the component's replaceUserObject method.
	 * @param cellObj - cell to clone
	 */
	protected Object cloneCell(Object cellObj) {
		if (cellObj instanceof DeltaComponent) {
			// Clones the cell
			DeltaComponent cell = (DeltaComponent) cellObj;
			DeltaComponent clone = (DeltaComponent) cell.clone();
			// Clones the user object
			clone.replaceUserObject();
			return clone;
		}
		if (cellObj instanceof DeltaEdge) {
			// Clones the edge
			DeltaEdge cell = (DeltaEdge) cellObj;
			DeltaEdge clone = (DeltaEdge) cell.clone();
			// Clones the user object
			clone.replaceUserObject();
			return clone;
		}
		else if (cellObj instanceof DefaultGraphCell) {
			// Clones the cell
			DefaultGraphCell cell = (DefaultGraphCell) cellObj;
			DefaultGraphCell clone = (DefaultGraphCell) cell.clone();
			// Clones the user object
			clone.setUserObject(cloneUserObject(cell.getUserObject()));
			return clone;
		}
		return cellObj;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void edit(Object[] inserted, Object[] removed, Map attributes,
			ConnectionSet cs, ParentMap pm, UndoableEdit[] edits) {
		super.edit(inserted,removed,attributes,cs,pm,edits);
		if (inserted != null) {
			Set<Object> portsToFront = new HashSet<Object>();
			for (int i=0; i<inserted.length; i++) {
				if (this.isPort(inserted[i]))
					portsToFront.add(inserted[0]);
			}
			this.toFront(portsToFront.toArray());
		}
	}
	
	/**
	 * Connects or disconnects the edge and port in this model based on
	 * <code>remove</code>. Subclassers should override this to update
	 * connectivity datastructures.
	 */
	@Override
	protected void connect(Object edge, Object port, boolean isSource,
			boolean insert) {
		// Add edge to port
		if (port instanceof Port)
			if (insert)
				((Port) port).addEdge(edge);

			// Only removes if opposite is not
			// connected to same port
			else if ((isSource) ? getTarget(edge) != port
					: getSource(edge) != port)
				((Port) port).removeEdge(edge);
		if (!insert)
			port = null;
		if (edge instanceof Edge) {
			/*if (port instanceof DeltaInputPort)
				((DeltaEdge)edge).setSource(port);
			else
				((DeltaEdge)edge).setTarget(port);*/
			if (isSource)
				((Edge) edge).setSource(port);
			else
				((Edge) edge).setTarget(port);
		}
	}
	
	/**
	 * Override to implement our constraints on electronic circuits. In this case
	 * we need only check that if the source is an input port, and if so whether
	 * it already has edges attached.
	 * @param edge - edge to check if port is a valid source for.
	 * @param port - port to check validity of.
	 * @return true if source is valid, false otherwise.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean acceptsSource(Object edge, Object port) {
		// Check target is not null - we do not allow dangling edges.
		if (port == null)
			return false;
		// If source is an input port, check it has no other edges attached.
		if (port instanceof DeltaInputPort) {
			DeltaInputPort inputPort = (DeltaInputPort)port;
			Set<Edge> portEdges = inputPort.getEdges();
			if (portEdges.contains(edge))
				portEdges.remove(edge);
			if (!portEdges.isEmpty())
				return false;
		}
		// Check other end of edge is not the same type of port
		Edge castEdge = (Edge)edge;
		if ((port instanceof DeltaInputPort)
			&& (castEdge.getTarget() instanceof DeltaInputPort))
				return false;
		else if ((port instanceof DeltaOutputPort)
			&& (castEdge.getTarget() instanceof DeltaOutputPort))
				return false;
		return true;
	}
	
	/**
	 * Override to implement our constraints on electronic circuits. Here we
	 * must check that the target is not null (no dangling edges), that it
	 * is not the same as the source (no loops), and that if the target is
	 * an input port, it has no other edges attached.
	 * @param edge - edge to check if port is a valid target for.
	 * @param port - port to check validity of.
	 * @return true if target is valid, false otherwise.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean acceptsTarget(Object edge, Object port) {
		// Check target is not null - we do not allow dangling edges.
		if (port == null)
			return false;
		// Check target is not same as source - i.e. not a loop
		if (((Edge) edge).getSource() == port)
			return false;
		// If target is an input port, check it has no other edges attached.
		if (port instanceof DeltaInputPort) {
			DeltaInputPort inputPort = (DeltaInputPort)port;
			Set<Edge> portEdges = inputPort.getEdges();
			if (portEdges.contains(edge))
				portEdges.remove(edge);
			if (!portEdges.isEmpty())
				return false;
		}
		// Check other end of edge is not the same type of port
		Edge castEdge = (Edge)edge;
		if ((port instanceof DeltaInputPort)
			&& (castEdge.getSource() instanceof DeltaInputPort))
				return false;
		else if ((port instanceof DeltaOutputPort)
			&& (castEdge.getSource() instanceof DeltaOutputPort))
				return false;
		return true;
	}
	
	public boolean isLedUsed(int number, int ledType) {
		if (ledType == ComponentPanel.LEDR)
			return ledrArray[number];
		else if (ledType == ComponentPanel.LEDG)
			return ledgArray[number];
		else return true;
	}
	
	public void setLedUsed(int number, int ledType, boolean used) {
		if (ledType == ComponentPanel.LEDR)
			ledrArray[number] = used;
		else if (ledType == ComponentPanel.LEDG)
			ledgArray[number] = used;
	}
	
	public boolean isSevenSegmentUsed(int number) {
		return sevenSegmentArray[number];
	}
	
	public void setSevenSegmentUsed(int number, boolean used) {
		sevenSegmentArray[number] = used;
	}
	
}
