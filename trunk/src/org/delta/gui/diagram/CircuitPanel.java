package org.delta.gui.diagram;

import org.delta.gui.MainWindow;
import org.jgraph.*;
import org.jgraph.graph.*;
import java.awt.BorderLayout;
import java.awt.geom.*;
import java.awt.Point;
import javax.swing.*;
import javax.swing.event.*;
//import org.jgrapht.*;
//import org.jgrapht.graph.*;
//import org.jgrapht.ext.*;
//import org.delta.circuit.*;
//import org.delta.circuit.component.*;

public class CircuitPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JGraph graph;
	private GraphUndoManager undoManager;
	
	public CircuitPanel() {
		GraphModel model = new DefaultGraphModel();
		/*ListenableGraph<Component,ComponentWire> grapht =
			new ListenableDirectedGraph<Component,ComponentWire>
				((DirectedGraph<Component,ComponentWire>)new ComponentGraph());
		JGraphModelAdapter<Component,ComponentWire> model = new JGraphModelAdapter<Component,ComponentWire>(grapht);*/
		GraphLayoutCache view = new GraphLayoutCache(model, new DeltaCellViewFactory());
		graph = new JGraph(model, view);
		
		//DebugComponent component = new DebugComponent();
		//DefaultGraphCell fred = model.getVertexCell(component);
		DefaultGraphCell[] cells = new DefaultGraphCell[3];
		//cells[0] = fred;
		
		cells[0] = new AndGate(new Point(100,100));
		//cells[0] = new DefaultGraphCell(new String("Hello"));
		//GraphConstants.setBounds(cells[0].getAttributes(), new Rectangle2D.Double(20,20,40,20));
		//GraphConstants.setGradientColor(cells[0].getAttributes(),Color.blue);
		//GraphConstants.setOpaque(cells[0].getAttributes(), true);
		
		//DefaultPort port0 = new DefaultPort();
		//cells[0].add(port0);
		
		cells[1] = new OrGate(new Point(300,300));
		//cells[1] = new DefaultGraphCell(new String("World"));cells[1]
		//GraphConstants.setBounds(cells[1].getAttributes(), new Rectangle2D.Double(140,140,40,20));
		//GraphConstants.setGradientColor(cells[1].getAttributes(),Color.red);
		//GraphConstants.setOpaque(cells[1].getAttributes(), true);
   
		//DefaultPort port1 = new DefaultPort();
		//cells[1].add(port1);
		DeltaEdge edge = new DeltaEdge();
		edge.setSource(cells[0].getChildAt(2));
		edge.setTarget(cells[1].getChildAt(0));
		cells[2] = edge;
		
		graph.getGraphLayoutCache().insert(cells);
		graph.setPortsVisible(true);
		
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
