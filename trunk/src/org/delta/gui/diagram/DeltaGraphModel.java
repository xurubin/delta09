/**
 * 
 */
package org.delta.gui.diagram;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.Port;
import org.jgraph.graph.GraphModel;

/**
 * @author chris
 *
 */
public class DeltaGraphModel extends DefaultGraphModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Overwritten version of cloneCells that performs a deep clone, i.e.
	 * including each cell's children.
	 * @param model
	 * @param cells
	 * @return
	 */
	public Map cloneCells(GraphModel model, Object[] cells) {
		Map<Object,Object> map = new Hashtable<Object,Object>();
		// Add Cells to Queue
		for (int i = 0; i < cells.length; i++)
			map.put(cells[i], cloneCell(model, cells[i]));
		// Replace Parent and Anchors
		Iterator it = map.entrySet().iterator();
		Object obj, cell, parent;
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
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
}
