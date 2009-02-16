package org.delta.gui.diagram;

import org.jgraph.graph.*;


public class DeltaCellViewFactory extends DefaultCellViewFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CellView createView(GraphModel model, Object cell) {
		CellView view = null;
		if (model.isPort(cell))
			view = createPortView(cell);
		else if (model.isEdge(cell))
			view = createEdgeView(cell);
		else
			view = createVertexView(cell);
		return view;
	}

	/**
	 * Constructs a VertexView view for the specified object.
	 */
	protected VertexView createVertexView(Object cell) {
		if (cell instanceof AndGate)
			return new AndGateView(cell);
		else if (cell instanceof OrGate)
			return new OrGateView(cell);
		return new VertexView(cell);
	}

	/**
	 * Constructs an EdgeView view for the specified object.
	 */
	protected EdgeView createEdgeView(Object cell) {
		return new DeltaEdgeView(cell);
	}

	/**
	 * Constructs a PortView view for the specified object.
	 */
	protected PortView createPortView(Object cell) {
		if (cell instanceof DeltaInputPort)
			return new DeltaInputPortView(cell);
		else if (cell instanceof DeltaOutputPort)
			return new DeltaOutputPortView(cell);
		return new PortView(cell);
	}
}
