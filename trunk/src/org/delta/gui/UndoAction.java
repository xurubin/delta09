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

	public UndoAction(String text, ImageIcon icon)
	{
		super(text,icon);
	}
	
	public UndoAction(String text) {
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
			undoManager.undo(cache);
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