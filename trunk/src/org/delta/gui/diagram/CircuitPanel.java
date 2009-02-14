package org.delta.gui.diagram;

import org.delta.gui.MainWindow;
import org.delta.gui.diagram.DeltaMarqueeHandler;

import org.jgraph.*;
import org.jgraph.graph.*;

import java.awt.BorderLayout;
import java.awt.Point;

import javax.swing.*;
import javax.swing.event.*;

import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.delta.circuit.*;

public class CircuitPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JGraph graph;
	private GraphUndoManager undoManager;
	
	public CircuitPanel() {
		// Create the underlying simulation graph
		ListenableGraph<Component,ComponentWire> grapht =
			new ListenableDirectedGraph<Component,ComponentWire>
				(/*(DirectedGraph<Component,ComponentWire>)*/new ComponentGraph());
		// Create a new model for the display graph using the simulation graph
		DeltaGraphModel model = new DeltaGraphModel(grapht);
		GraphLayoutCache view = new GraphLayoutCache(model, new DeltaCellViewFactory());
		graph = new JGraph(model, view);
		
		// Create test cells
		DefaultGraphCell[] cells = new DefaultGraphCell[3];
		cells[0] = new AndGate(new Point(100,100));
		cells[1] = new OrGate(new Point(300,300));
		
		// Create test edge
		DeltaEdge edge = new DeltaEdge();
		edge.setSource(cells[0].getChildAt(2));
		edge.setTarget(cells[1].getChildAt(0));
		cells[2] = edge;
		
		// Insert cells into graph
		graph.getGraphLayoutCache().insert(cells);
		
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
	
	public GraphUndoManager getGraphUndoManager() {
		return this.undoManager;
	}
	
}
