package org.delta.gui;
import java.awt.event.*;
import javax.swing.*;

import org.jgraph.JGraph;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphUndoManager;

public class RedoAction extends AbstractAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RedoAction(String text,ImageIcon icon)
	{
		super(text,icon);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		JGraph graph = CircuitPanelTest.panel.getGraph();
		GraphLayoutCache cache = graph.getGraphLayoutCache();
		GraphUndoManager undoManager = CircuitPanelTest.panel.getGraphUndoManager();
		try
		{
			// Try to undo the previously undone action
			undoManager.redo(cache);
		}
		catch (Exception ex)
		{
			System.err.println(ex);
		}
		finally
		{
			// Update the undo/redo buttons
			CircuitPanelTest.undo.setEnabled(undoManager.canUndo(graph.getGraphLayoutCache()));
			CircuitPanelTest.redo.setEnabled(undoManager.canRedo(graph.getGraphLayoutCache()));
		}
	}
}