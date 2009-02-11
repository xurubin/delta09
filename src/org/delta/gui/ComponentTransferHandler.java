package org.delta.gui;

import java.awt.datatransfer.Transferable;
import java.awt.geom.Rectangle2D;
import java.awt.Point;
import java.util.Map;

import javax.swing.TransferHandler;
import javax.swing.JComponent;

import org.jgraph.JGraph;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.GraphTransferable;
import org.jgraph.graph.GraphTransferHandler;

import org.delta.gui.diagram.*;

public class ComponentTransferHandler extends GraphTransferHandler {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int getSourceActions(JComponent comp) {
	    return TransferHandler.COPY;
	  }
	
	protected Transferable createTransferable(JComponent comp) {
		// Create relevant component by checking the label
		ComponentPanelLabel draggedComponent = (ComponentPanelLabel) comp;
		int componentKey = draggedComponent.getComponentKey();
		Object[] cells = new Object[1];
		// TODO: Complete case statement
		switch (componentKey) {
		//case 0: unused
		case 1: cells[0] = new AndGate(new Point(0,0)); break;
		//case 2: cells[0] = new AndGate(new Point(0,0)); break;
		case 3: cells[0] = new OrGate(new Point(0,0)); break;
		//case 4: cells[0] = new AndGate(new Point(0,0)); break;
		}
		
		// Create a new graph containing only the new component
		GraphModel model = new DefaultGraphModel();
		GraphLayoutCache view = new GraphLayoutCache(model, new DeltaCellViewFactory());
		JGraph graph = new JGraph(model, view);
		graph.getGraphLayoutCache().insert(cells);
		
		// Use this graph to find the relevant arguments for the GraphTransferable constructor
		Map attributes = GraphConstants.createAttributes(cells, graph.getGraphLayoutCache());
		Rectangle2D bounds = graph.getCellBounds(cells);
		ConnectionSet cs = ConnectionSet.create(graph.getModel(), cells, false);
		
		// Use the library GraphTransferable constructor
		return new GraphTransferable(cells, attributes, bounds, cs, null);
	}
}
