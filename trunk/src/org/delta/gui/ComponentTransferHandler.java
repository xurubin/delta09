package org.delta.gui;

import java.awt.datatransfer.Transferable;
import java.awt.geom.Rectangle2D;
import java.awt.Point;
import java.util.Map;

import javax.swing.TransferHandler;
import javax.swing.JComponent;

import org.jgraph.JGraph;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphTransferable;
import org.jgraph.graph.GraphTransferHandler;
import org.jgraph.graph.DefaultGraphCell;

import org.delta.gui.diagram.AndGate;

public class ComponentTransferHandler extends GraphTransferHandler {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int getSourceActions(JComponent comp) {
	    return TransferHandler.COPY;
	  }
	
	protected Transferable createTransferable(JComponent comp) {
		DefaultGraphCell component = new AndGate(new Point(200,200));
		Object[] cell = {(Object)component};
		JGraph graph = MainWindow.get().circuit_panel.getGraph();
		Map attributes = GraphConstants.createAttributes(cell,graph.getGraphLayoutCache());
		Rectangle2D bounds = GraphConstants.getBounds(component.getAttributes());
		return new GraphTransferable(cell,attributes,bounds,new ConnectionSet(),null);
	}
	
	/*public void exportAsDrag(JComponent JavaDoc comp, InputEvent e, int action) {
		int srcActions = getSourceActions(comp);
		int dragAction = srcActions & action;
		if (! (e instanceof MouseEvent)) {
			// only mouse events supported for drag operations
			dragAction = NONE;
		}
		if (dragAction != NONE && !GraphicsEnvironment.isHeadless()) {
			if (recognizer == null) {
				recognizer = new SwingDragGestureRecognizer(new DragHandler());
			}
			recognizer.gestured(comp, (MouseEvent)e, srcActions, dragAction);
		} else {
			exportDone(comp, null, NONE);
		}
	}*/
}
