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

	public RedoAction(String text, ImageIcon icon)
	{
		super(text,icon);
	}
	
	public RedoAction(String text) {
		super(text);
	}

	public void actionPerformed(ActionEvent e)
	{
		JGraph graph = MainWindow.get().circuit_panel.getGraph();
		GraphLayoutCache cache = graph.getGraphLayoutCache();
		GraphUndoManager undoManager = MainWindow.get().circuit_panel.getGraphUndoManager();
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
			// Update the undo/redo actions
			boolean canUndo = undoManager.canUndo(graph.getGraphLayoutCache());
			MainWindow.get().undo_action.setEnabled(canUndo);
			MainWindow.get().undo_toolbar_action.setEnabled(canUndo);
			boolean canRedo = undoManager.canRedo(graph.getGraphLayoutCache());
			MainWindow.get().redo_action.setEnabled(canRedo);
			MainWindow.get().redo_toolbar_action.setEnabled(canRedo);
		}
	}
}