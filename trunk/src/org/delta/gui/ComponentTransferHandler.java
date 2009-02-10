package org.delta.gui;

import java.awt.datatransfer.Transferable;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import javax.swing.TransferHandler;
import javax.swing.JComponent;

import org.jgraph.JGraph;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphTransferable;
import org.jgraph.graph.DefaultGraphCell;

import org.delta.gui.diagram.AndGate;

public class ComponentTransferHandler extends TransferHandler {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int getSourceActions(JComponent comp) {
	    return TransferHandler.COPY;
	  }
	
	protected Transferable createTransferable(JComponent comp) {
		DefaultGraphCell component = new AndGate();
		Object[] cell = {(Object)component};
		JGraph graph = MainWindow.get().circuit_panel.getGraph();
		Map attributes = GraphConstants.createAttributes(cell,graph.getGraphLayoutCache());
		Rectangle2D bounds = GraphConstants.getBounds(component.getAttributes());
		return new GraphTransferable(cell,attributes,bounds,new ConnectionSet(),null);
	}
}
