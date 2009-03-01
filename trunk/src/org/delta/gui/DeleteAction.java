package org.delta.gui;

import java.awt.event.ActionEvent;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import org.delta.gui.diagram.DeltaGraph;
import org.delta.gui.diagram.DeltaGraphModel;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.Edge;

/**
 * @author Christopher Wilson
 * DeleteAction is used to centralise the execution of the "remove" command.
 * The action performed is to delete the current selection on the circuit diagram.
 * In order to avoid dangling edges connected to the selection are removed as well.
 */
public class DeleteAction extends AbstractAction
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Create new DeleteAction with a name and icon.
	 * @param text - name for the action
	 * @param icon - icon to represent the Action
	 */
	public DeleteAction(String text, ImageIcon icon, String accelerator, int mnemonic)
	{
		super(text);
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator));
		this.putValue(Action.LARGE_ICON_KEY, icon);
		this.putValue(Action.MNEMONIC_KEY, mnemonic);
	}
	
	/**
	 * Perform the "remove" action for the given event. This deletes all cells
	 * in the current selection and all edges connected to them to ensure we are
	 * not left with dangling edges.
	 * @param e - event that fired the DeleteAction
	 */
	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e)
	{
		DeltaGraph graph = (DeltaGraph)MainWindow.get().circuit_panel.getGraph();
		
		// Only need to perform action if something is selected
		if (!graph.isSelectionEmpty()) {
			
			// Create array of all components in the selection
			Object[] components = graph.getSelectionCells();
			components = graph.getDescendants(components);
			
			// Create array of all edges connected to components in the selection
			Set<Edge> edgeSet = DefaultGraphModel.getEdges(graph.getModel(), components);
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
			
			// If any of the components were LEDs or seven segment displays, free them up for reuse
			DeltaGraphModel model = (DeltaGraphModel)graph.getModel();
			model.checkUsedComponents();
		}
	}
	
}