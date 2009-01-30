package org.delta.gui;
import java.awt.event.*;
import javax.swing.*;

import org.jgraph.*;
import org.jgraph.graph.*;

public class UndoAction extends AbstractAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UndoAction(String text,ImageIcon icon)
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
			undoManager.undo(cache);
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