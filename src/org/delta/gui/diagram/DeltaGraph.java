package org.delta.gui.diagram;

import javax.swing.JComponent;

import org.jgraph.JGraph;
import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;

/**
 * Subclass of JGraph used to create a DeltaGraphUI rather than a DefaultGraphUI.
 * @author Group Delta 2009
 */
public class DeltaGraph extends JGraph {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public DeltaGraph() {
		super();
	}

	/**
	 * @see org.jgraph.JGraph#JGraph()
	 */
	public DeltaGraph(GraphModel model) {
		super(model);
	}

	/**
	 * @see org.jgraph.JGraph#JGraph(GraphLayoutCache)
	 */
	public DeltaGraph(GraphLayoutCache cache) {
		super(cache);
	}

	/**
	 * @see org.jgraph.JGraph#JGraph(GraphModel, GraphLayoutCache)
	 */
	public DeltaGraph(GraphModel model, GraphLayoutCache cache) {
		super(model, cache);
	}

	/**
	 * @see org.jgraph.JGraph#JGraph(GraphModel, BasicMarqueeHandler)
	 */
	public DeltaGraph(GraphModel model, BasicMarqueeHandler mh) {
		super(model, mh);
	}

	/**
	 * @see org.jgraph.JGraph#JGraph(GraphModel, GraphLayoutCache, BasicMarqueeHandler)
	 */
	public DeltaGraph(GraphModel model, GraphLayoutCache layoutCache,
			BasicMarqueeHandler mh) {
		super(model, layoutCache, mh);
	}
	
	/**
	 * Notification from the <code>UIManager</code> that the L&F has changed.
	 * Replaces the current UI object with the latest version from the
	 * <code>UIManager</code>. Subclassers can override this to support
	 * different GraphUIs.
	 * NOTE: Overwritten to use DeltaGraphUI.
	 * @see JComponent#updateUI
	 */
	public void updateUI() {
		setUI(new DeltaGraphUI());
		invalidate();
	}
}
