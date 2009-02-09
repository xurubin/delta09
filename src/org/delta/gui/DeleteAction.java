package org.delta.gui;
import java.awt.event.*;
import javax.swing.*;

import org.jgraph.JGraph;

public class DeleteAction extends AbstractAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DeleteAction(String text,ImageIcon icon)
	{
		super(text,icon);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		JGraph graph = MainWindow.get().circuit_panel.getGraph();
		if (MainWindow.get().circuit_panel.getGraph().isSelectionEmpty() == false)
		{
			Object[] cells = graph.getSelectionCells();
			cells = graph.getDescendants(cells);
			graph.getModel().remove(cells);
		}
	}
}