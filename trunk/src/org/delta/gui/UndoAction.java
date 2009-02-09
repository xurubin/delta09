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
			// Update the undo/redo buttons
			MainWindow.get().toolbar.undo_button.getAction().setEnabled(undoManager.canUndo(graph.getGraphLayoutCache()));
			MainWindow.get().toolbar.redo_button.getAction().setEnabled(undoManager.canRedo(graph.getGraphLayoutCache()));
		}
	}
}