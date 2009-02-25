package org.delta.gui;
import java.awt.event.*;
import java.util.Set;

import javax.swing.*;

import org.delta.gui.diagram.DeltaGraph;
//import org.delta.gui.diagram.DeltaGraphModel;
//import org.delta.gui.diagram.Ledg;
//import org.delta.gui.diagram.Ledr;
//import org.delta.gui.diagram.SevenSegment;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.Edge;


public class CutAction extends AbstractAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Action action;

	public CutAction(String text, ImageIcon icon, String accelerator, int mnemonic)
	{
		super(text);
		this.action = javax.swing.TransferHandler.getCutAction();
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator));
		this.putValue(Action.LARGE_ICON_KEY, icon);
		this.putValue(Action.MNEMONIC_KEY, mnemonic);
	}
	
	/**
	 * Perform the "cut" action for the given event. This moves all cells
	 * in the current selection to the clipboard (using the default Swing
	 * action) and deletes all edges connected to them to ensure we are
	 * not left with dangling edges.
	 * @param e - event that fired the DeleteAction
	 */
	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e)
	{
		e = new ActionEvent(MainWindow.get().circuit_panel.getGraph(),
				e.getID(),e.getActionCommand(),e.getModifiers());

		// INSERTED
		DeltaGraph graph = (DeltaGraph)MainWindow.get().circuit_panel.getGraph();
		
		// Only need to remove edges if something is actually selected
		if (!graph.isSelectionEmpty()) {
			
			// Create array of all components in the selection
			Object[] components = graph.getSelectionCells();
			components = graph.getDescendants(components);
			
			// If any of the components are LEDs or seven segment displays, free them up for reuse
			/*for (int i=0; i<components.length; i++) {
				if (components[i] instanceof Ledr) {
					Ledr ledr = (Ledr)components[i];
					DeltaGraphModel model = (DeltaGraphModel)graph.getModel();
					model.setLedUsed(ledr.getLedNumber(), ComponentPanel.LEDR, false);
				}
				else if (components[i] instanceof Ledg) {
					Ledg ledg = (Ledg)components[i];
					DeltaGraphModel model = (DeltaGraphModel)graph.getModel();
					model.setLedUsed(ledg.getLedNumber(), ComponentPanel.LEDG, false);
				}
				else if (components[i] instanceof SevenSegment) {
					SevenSegment sevenSegment = (SevenSegment)components[i];
					DeltaGraphModel model = (DeltaGraphModel)graph.getModel();
					model.setSevenSegmentUsed(sevenSegment.getSevenSegmentNumber(), false);
				}
			}*/
			
			// Create array of all edges connected to components in the selection
			Set<Edge> edgeSet = DefaultGraphModel.getEdges(graph.getModel(), components);
			Object[] edgeArray = edgeSet.toArray();
			
			// Pass the array of edges to the GraphModel for removal
			graph.getModel().remove(edgeArray);
		}
		// END OF INSERT
		action.actionPerformed(e);
		// TODO: If an LED or seven segment display is cut (and not pasted back), make it available for reuse.
	}
	
}