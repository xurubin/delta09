package org.delta.gui.diagram;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.UndoableEditEvent;

import org.delta.circuit.ComponentGraph;
import org.delta.circuit.ListenableComponentGraph;
import org.delta.gui.MainWindow;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphTransferHandler;
import org.jgraph.graph.GraphUndoManager;

/**
 * Panel that displays the electrical circuit. It uses an extended version of
 * the JGraph library to store and display the diagram, and has various methods
 * that allow the rest of the application to interact with it.
 * @author Group Delta 2009
 */
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
    public final DeltaGraph getGraph() {
        return this.graph;
    }

    /**
     * Creates a new blank DeltaGraph to use on the CircuitPanel.
     */
    public final void setGraph() {
        // Create a new underlying simulation graph
        ListenableComponentGraph grapht =
            new ListenableComponentGraph(new ComponentGraph());
        // Create a new model for the display graph using the simulation graph
        DeltaGraphModel model = new DeltaGraphModel(grapht);
        DeltaCellViewFactory factory = new DeltaCellViewFactory();
        GraphLayoutCache view = new GraphLayoutCache(model, factory, true);
        graph = new DeltaGraph(model, view);
        this.setGraph(graph);
    }

    /**
     * Change the graph being shown on the panel to the one specified.
     * @param newGraph - the graph to use as the new diagram.
     */
    public final void setGraph(final DeltaGraph newGraph) {
        this.graph = newGraph;
        GraphLayoutCache view = graph.getGraphLayoutCache();
        DeltaGraphModel model = (DeltaGraphModel) graph.getModel();

        graph.setXorEnabled(false);

        // Set the "first" cell to be invisible (it is the clock component)
        view.setVisible(model.getRootAt(0), false);

        // Create undo manager and add to graph
        undoManager = new GraphUndoManager() {
            private static final long serialVersionUID = 1L;
            // Override superclass method so we can update undo/redo buttons
            @Override
            public void undoableEditHappened(final UndoableEditEvent e) {
                super.undoableEditHappened(e);
                MainWindow window = MainWindow.get();
                GraphLayoutCache cache = graph.getGraphLayoutCache();
                window.getUndoAction().setEnabled(undoManager.canUndo(cache));
                window.getRedoAction().setEnabled(undoManager.canRedo(cache));
            }
            // Override so we can check used components after an undo
            @Override
            public void undo(final Object source) {
                super.undo(source);
                GraphLayoutCache cache = (GraphLayoutCache) source;
                DeltaGraphModel model = (DeltaGraphModel) cache.getModel();
                model.checkUsedComponents();
            }
            // Override so we can check used components after a redo
            @Override
            public void redo(final Object source) {
                super.redo(source);
                GraphLayoutCache cache = (GraphLayoutCache) source;
                DeltaGraphModel model = (DeltaGraphModel) cache.getModel();
                model.checkUsedComponents();
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
        GraphTransferHandler handler = new DeltaGraphTransferHandler(model);
        handler.setAlwaysReceiveAsCopyAction(true);
        graph.setTransferHandler(handler);

        // Change MarqueeHandler to implement custom edges and popup menus
        graph.setMarqueeHandler(new DeltaMarqueeHandler(graph));

        // Add graph to the panel
        scrollPane.setViewportView(graph);
    }

    /**
     * Accessor method for the ComponentGraph used by the ComponentGraphAdapter,
     * which constantly mirrors the circuit diagram. Used so the circuit can be
     * retrieved for simulation.
     * @return the ComponentGraph that represents the circuit diagram.
     */
    public final ComponentGraph getComponentGraph() {
        DeltaGraphModel model = (DeltaGraphModel) this.graph.getModel();
        return model.getComponentGraph();
    }

    /**
     * Accessor method for the undo manager. This is required for the
     * undo and redo actions.
     * @return the GraphUndoManager for this graph.
     */
    public final GraphUndoManager getGraphUndoManager() {
        return this.undoManager;
    }

}
