package org.delta.gui;

import java.awt.event.*;
import javax.swing.*;
import java.util.Set;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphModel;

/**
 * RemoveAction is used to centralise the execution of the "remove" command.
 * The action performed is to delete the current selection on the circuit diagram.
 * In order to avoid dangling edges connected to the selection are removed as well.
 * @author Christopher Wilson
 *
 */
public class RemoveAction extends AbstractAction
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Create new RemoveAction with a name and icon.
	 * @param text - name for the action
	 * @param icon - icon to represent the Action
	 */
	public RemoveAction(String text,ImageIcon icon)
	{
		super(text,icon);
	}
	
	/**
	 * Perform the "remove" action for the given event. This deletes all cells
	 * in the current selection and all edges connected to them to ensure we are
	 * not left with dangling edges.
	 * @param e - event that fired the RemoveAction
	 */
	public void actionPerformed(ActionEvent e)
	{
		JGraph graph = MainWindow.get().circuit_panel.getGraph();
		
		// Only need to perform action if something is selected
		if (!graph.isSelectionEmpty()) {
			
			// Create array of all components in the selection
			Object[] components = graph.getSelectionCells();
			components = graph.getDescendants(components);
			
			// Create array of all edges connected to components in the selection
			Set edgeSet = DefaultGraphModel.getEdges(graph.getModel(), components);
			Object[] edgeArray = edgeSet.toArray();
			
			// Combine these two arrays
			Object[] cellsToRemove = new Object[components.length+edgeArray.length];
			int i = 0;
			for (Object component : components) {
				cellsToRemove[i] = component;
				i++;
			}
			for (Object edge : edgeArray) {
				cellsToRemove[i] = edge;
				i++;
			}
			
			// Pass the combined array to the GraphModel for removal
			graph.getModel().remove(cellsToRemove);
		}
	}
	
}