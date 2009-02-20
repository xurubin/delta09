package org.delta.gui.diagram;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.UndoableEditEvent;

import org.delta.circuit.ComponentGraph;
import org.delta.circuit.ListenableComponentGraph;
import org.delta.gui.MainWindow;
import org.jgraph.JGraph;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphUndoManager;

public class CircuitPanel extends JPanel {
	/** Needed for correct serialization. */ 
	private static final long serialVersionUID = 1L;
	private JGraph graph;
	private GraphUndoManager undoManager;
	
	public CircuitPanel() {
		// Create the underlying simulation graph
		ListenableComponentGraph grapht =
			new ListenableComponentGraph(new ComponentGraph());
		// Create a new model for the display graph using the simulation graph
		DeltaGraphModel model = new DeltaGraphModel(grapht);
		GraphLayoutCache view = new GraphLayoutCache(model, new DeltaCellViewFactory(), true);
		graph = new DeltaGraph(model, view);
		graph.setXorEnabled(false);
		
		// Set the "first" cell to be invisible (it is the clock component)
		view.setVisible(model.getRootAt(0), false);
		
		// Create undo manager and add to graph
		undoManager = new GraphUndoManager() {
			private static final long serialVersionUID = 1L;
			// Override superclass method so we can update undo/redo buttons
			public void undoableEditHappened(UndoableEditEvent e) {
				super.undoableEditHappened(e);
				MainWindow.get().getUndoAction().setEnabled(undoManager.canUndo(graph.getGraphLayoutCache()));
				MainWindow.get().getRedoAction().setEnabled(undoManager.canRedo(graph.getGraphLayoutCache()));
			}
		};
		graph.getModel().addUndoableEditListener(undoManager);
		
		// Prevent user from changing size of components
		graph.setSizeable(false);
		
		// Prevent user from adding labels to components
		graph.setEditable(false);
		
		// Make ports visible
		graph.setPortsVisible(true);
		
		// Change TransferHandler to implement custom dropping
		DeltaGraphTransferHandler handler = new DeltaGraphTransferHandler();
		handler.setAlwaysReceiveAsCopyAction(true);
		graph.setTransferHandler(handler);
		
		// Change MarqueeHandler to implement custom edges and popup menus
		graph.setMarqueeHandler(new DeltaMarqueeHandler(graph));
		
		// Add graph to the panel
		this.setLayout(new BorderLayout());
		this.add(new JScrollPane(graph));
		this.setVisible(true);
	}
	
	public JGraph getGraph() {
		return this.graph;
	}
	
	public void setGraph(DeltaGraph newGraph) {
		this.graph = newGraph;
	}
	
	/**
	 * Accessor method for the ComponentGraph created by the ComponentGraphAdapter,
	 * which constantly mirrors the circuit diagram. Used so the circuit can be
	 * retrieved for simulation.
	 * @return the ComponentGraph that represents the circuit diagram.
	 */
	public ComponentGraph getComponentGraph() {
		DeltaGraphModel model = (DeltaGraphModel)this.graph.getModel();
		return model.getComponentGraph();
	}
	
	public GraphUndoManager getGraphUndoManager() {
		return this.undoManager;
	}
	
}
