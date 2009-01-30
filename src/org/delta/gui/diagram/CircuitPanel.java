package org.delta.gui.diagram;

import org.delta.gui.CircuitPanelTest;
import org.jgraph.*;
import org.jgraph.graph.*;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;

public class CircuitPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JGraph graph;
	private GraphUndoManager undoManager;
	
	public CircuitPanel()
	{
		GraphModel model = new DefaultGraphModel();
		GraphLayoutCache view = new GraphLayoutCache(model, new DefaultCellViewFactory());
		graph = new JGraph(model, view);
		DefaultGraphCell[] cells = new DefaultGraphCell[3];
		
		cells[0] = new DefaultGraphCell(new String("Hello"));
		GraphConstants.setBounds(cells[0].getAttributes(), new Rectangle2D.Double(20,20,40,20));
		GraphConstants.setGradientColor(cells[0].getAttributes(),Color.blue);
		GraphConstants.setOpaque(cells[0].getAttributes(), true);
		
		DefaultPort port0 = new DefaultPort();
		cells[0].add(port0);
		
		cells[1] = new DefaultGraphCell(new String("World"));
		GraphConstants.setBounds(cells[1].getAttributes(), new Rectangle2D.Double(140,140,40,20));
		GraphConstants.setGradientColor(cells[1].getAttributes(),Color.red);
		GraphConstants.setOpaque(cells[1].getAttributes(), true);
   
		DefaultPort port1 = new DefaultPort();
		cells[1].add(port1);
		DeltaEdge edge = new DeltaEdge();
		edge.setSource(cells[0].getChildAt(0));
		edge.setTarget(cells[1].getChildAt(0));
		cells[2] = edge;
		
		graph.getGraphLayoutCache().insert(cells);
		undoManager = new GraphUndoManager()
		{
			private static final long serialVersionUID = 1L;
			// Override superclass method so we can update undo/redo buttons
			public void undoableEditHappened(UndoableEditEvent e)
			{
				super.undoableEditHappened(e);
				CircuitPanelTest.getUndo().setEnabled(undoManager.canUndo(graph.getGraphLayoutCache()));
				CircuitPanelTest.getRedo().setEnabled(undoManager.canRedo(graph.getGraphLayoutCache()));
			}
		};
		graph.getModel().addUndoableEditListener(undoManager);
		graph.setSizeable(false);
		
		this.setLayout(new BorderLayout());
		this.add(new JScrollPane(graph));
		this.setVisible(true);
	}
	
	public JGraph getGraph()
	{
		return this.graph;
	}
	
	public GraphUndoManager getGraphUndoManager()
	{
		return this.undoManager;
	}
}
