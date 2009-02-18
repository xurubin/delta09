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

	public UndoAction(String text, ImageIcon icon, String accelerator, int mnemonic)
	{
		super(text);
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator));
		this.putValue(Action.LARGE_ICON_KEY, icon);
		this.putValue(Action.MNEMONIC_KEY, mnemonic);
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
			MainWindow.get().undo_action.setEnabled(undoManager.canUndo(graph.getGraphLayoutCache()));
			MainWindow.get().redo_action.setEnabled(undoManager.canRedo(graph.getGraphLayoutCache()));
		}
	}
}