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

	public RedoAction(String text, ImageIcon icon, String accelerator)
	{
		super(text,icon);
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator));
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
			MainWindow.get().undo_action.setEnabled(undoManager.canUndo(graph.getGraphLayoutCache()));
			MainWindow.get().redo_action.setEnabled(undoManager.canRedo(graph.getGraphLayoutCache()));
		}
	}
}