/**
 * 
 */
package org.delta.gui.diagram;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

import org.delta.circuit.Component;
import org.delta.circuit.ComponentWire;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.Port;
import org.jgraph.graph.GraphModel;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.ListenableGraph;

/**
 * @author Group Delta 2009
 * 
 */
public class DeltaGraphModel extends JGraphModelAdapter<Component,ComponentWire> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * This constructor simply calls the parent constructor with the given grapht graph.
	 * This allows the JGraphModelAdapter to be created properly.
	 * @param grapht - grapht graph to connect with this model.
	 */
	public DeltaGraphModel(ListenableGraph<Component,ComponentWire> grapht) {
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
	
}
