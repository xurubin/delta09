package org.delta.gui.diagram;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.UndoableEditEvent;

import org.delta.circuit.ComponentGraph;
import org.delta.circuit.ListenableComponentGraph;
import org.delta.gui.MainWindow;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphUndoManager;

public class CircuitPanel extends JPanel {
	/** Needed for correct serialization. */ 
	private static final long serialVersionUID = 1L;
	
	/** Local reference to the graph currently being displayed. */
	private DeltaGraph graph;
	
	/** Local reference to the undo manager for the current graph. */
	private GraphUndoManager undoManager;
	
	/**
	 * Local reference to the panel's JScrollPane, which is
	 * what actually contains the graph.
	 */
	private JScrollPane scrollPane;
	
	/**
	 * Create a new CircuitPanel with an empty diagram.
	 */
	public CircuitPanel() {
		super();
		
		this.setLayout(new BorderLayout());
		scrollPane = new JScrollPane();
		this.add(scrollPane);
		this.setVisible(true);
		
		this.setGraph();
	}
	
	/**
	 * Accessor method for the diagram (i.e. the DeltaGraph).
	 * @return the DeltaGraph representing the diagram.
	 */
	public DeltaGraph getGraph() {
		return this.graph;
	}
	
	/**
	 * Creates a new blank DeltaGraph to use on the CircuitPanel.
	 */
	public void setGraph() {
		// Create a new underlying simulation graph
		ListenableComponentGraph grapht =
			new ListenableComponentGraph(new ComponentGraph());
		// Create a new model for the display graph using the simulation graph
		DeltaGraphModel model = new DeltaGraphModel(grapht);
		GraphLayoutCache view = new GraphLayoutCache(model, new DeltaCellViewFactory(), true);
		graph = new DeltaGraph(model, view);
		this.setGraph(graph);
	}
	
	/**
	 * Change the graph being shown on the panel to the one specified.
	 * @param newGraph - the graph to use as the new diagram.
	 */
	public void setGraph(DeltaGraph newGraph) {
		this.graph = newGraph;
		GraphLayoutCache view = graph.getGraphLayoutCache();
		DeltaGraphModel model = (DeltaGraphModel)graph.getModel();
		
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
		scrollPane.setViewportView(graph);
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
	
	/**
	 * Accessor method for the GraphUndoManager. This is required for the
	 * undo and redo actions.
	 * @return the GraphUndoManager for this graph.
	 */
	public GraphUndoManager getGraphUndoManager() {
		return this.undoManager;
	}
	
}
