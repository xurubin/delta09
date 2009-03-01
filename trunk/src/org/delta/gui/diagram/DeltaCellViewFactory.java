package org.delta.gui.diagram;

import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.PortView;
import org.jgraph.graph.VertexView;

/**
 * Extension of the DefaultCellViewFactory in JGraph that creates the
 * correct views for our new types of port (input and output), and all
 * of the new components ("vertices").
 * @author Group Delta 2009
 */
public class DeltaCellViewFactory extends DefaultCellViewFactory {
    /** Needed for correct serialization. */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new CellView for any type of cell (port, edge
     * or vertex) by calling the relevant method.
     * @param model - graphModel view is being created from.
     * @param cell - cell view is being created for.
     * @return the new CellView.
     */
    public final CellView createView(final GraphModel model,
            final Object cell) {
        CellView view = null;
        if (model.isPort(cell)) {
            view = createPortView(cell);
        } else if (model.isEdge(cell)) {
            view = createEdgeView(cell);
        } else {
            view = createVertexView(cell);
        }
        return view;
    }

    /**
     * Constructs a VertexView view for the specified cell. A different
     * type of view is returned depending upon the cell, e.g. an AndGate
     * component will return an AndGateView.
     * @param cell - the cell (vertex) to construct a view for.
     * @return a new VertexView.
     */
    protected final VertexView createVertexView(final Object cell) {
        if (cell instanceof DeltaComponent) {
            return new DeltaComponentView(cell);
        }
        return new VertexView(cell);
    }

    /**
     * Constructs a DeltaEdgeView view for the specified object.
     * @param cell - the cell (edge) to construct a view for.
     * @return a new DeltaEdgeView.
     */
    protected final EdgeView createEdgeView(final Object cell) {
        return new DeltaEdgeView(cell);
    }

    /**
     * Constructs a PortView view for the specified cell. A different
     * type of view is returned depending upon whether the port is
     * a DeltaInputPort or a DeltaOutputPort.
     * @param cell - the cell (port) to construct a view for.
     * @return a new PortView.
     */
    protected final PortView createPortView(final Object cell) {
        if (cell instanceof DeltaInputPort) {
            return new DeltaInputPortView(cell);
        } else if (cell instanceof DeltaOutputPort) {
            return new DeltaOutputPortView(cell);
        }
        return new PortView(cell);
    }

}
