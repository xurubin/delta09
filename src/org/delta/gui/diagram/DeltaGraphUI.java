package org.delta.gui.diagram;

import java.awt.HeadlessException;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import org.jgraph.plaf.basic.BasicGraphUI;
import org.jgraph.graph.CellHandle;
import org.jgraph.graph.CellView;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphContext;

/**
 * Extension of BasicGraphUI that uses a custom RootHandle. This is so
 * root handles cannot be used to grag wires - only control points.
 * @author Group Delta 2009
 */
public class DeltaGraphUI extends BasicGraphUI {

	/** Needed for correct serialization. */
	private static final long serialVersionUID = 1L;

	/**
	 * Create a new DeltaGraphUI.
	 */
	public DeltaGraphUI() {
		super();
	}

	/**
	 * Constructs the "root handle" for <code>context</code>.<br>
	 * NOTE: Overwritten to create a DeltaRootHandle rather than the normal
	 * RootHandle from BasicGraphUI.
	 * 
	 * @param context
	 *            reference to the context of the current selection.
	 */
	public CellHandle createHandle(GraphContext context) {
		if (context != null && !context.isEmpty() && graph.isEnabled()) {
			try {
				return new DeltaRootHandle(context);
			} catch (HeadlessException e) {
				// Assume because of running on a server
			} catch (RuntimeException e) {
				throw e;
			}
		}
		return null;
	}
	
	/** 
	 * @author Group Delta 2009
	 * Overwritten prevent an edge's rootHandle being used to drag the edge.
	 * @see org.jgraph.plaf.basic.BasicGraphUI.RootHandle
	 */
	public class DeltaRootHandle extends BasicGraphUI.RootHandle implements Serializable {

		/** Needed for correct serialization. */
		private static final long serialVersionUID = 1L;

		/**
		 * @see org.jgraph.plaf.basic.BasicGraphUI.RootHandle#RootHandle(GraphContext)
		 */
		public DeltaRootHandle(GraphContext ctx) {
			super(ctx);
		}
		
		/**
		 * Overritten to prevent an edge's rootHandle being used
		 * to drag the edge.
		 * @see org.jgraph.plaf.basic.BasicGraphUI.RootHandle#mousePressed(MouseEvent)
		 */
		public void mousePressed(MouseEvent event) {
			if (!event.isConsumed() && graph.isMoveable()) {
				if (handles != null) { // Find Handle
					for (int i = handles.length - 1; i >= 0; i--) {
						if (handles[i] != null) {
							handles[i].mousePressed(event);
							if (event.isConsumed()) {
								activeHandle = handles[i];
								return;
							}
						}
					}
				}
				// CHANGED - check that the selection is not an edge
				boolean edgeOnly = (views.length == 1) && (views[0] instanceof EdgeView);
				if (views != null && !edgeOnly) { // Start Move if over a (non-edge) cell
				// END OF CHANGE
					Point2D screenPoint = event.getPoint();
					Point2D pt = graph
							.fromScreen((Point2D) screenPoint.clone());
					CellView view = findViewForPoint(pt);
					if (view != null) {
						if (snapSelectedView) {
							Rectangle2D bounds = view.getBounds();
							start = graph.toScreen(new Point2D.Double(bounds
									.getX(), bounds.getY()));
							snapStart = graph.snap((Point2D) start.clone());
							_mouseToViewDelta_x = screenPoint.getX()
									- start.getX();
							_mouseToViewDelta_y = screenPoint.getY()
									- start.getY();
						} else { // this is the original RootHandle's mode.
							snapStart = graph.snap((Point2D) screenPoint
									.clone());
							_mouseToViewDelta_x = snapStart.getX()
									- screenPoint.getX();
							_mouseToViewDelta_y = snapStart.getY()
									- screenPoint.getY();
							start = (Point2D) snapStart.clone();
						}
						last = (Point2D) start.clone();
						snapLast = (Point2D) snapStart.clone();
						isContextVisible = contextViews != null
								&& contextViews.length < MAXCELLS
								&& (!event.isControlDown() || !graph
										.isCloneable());
						event.consume();
					}
				}
				// Analyze for common parent
				if (graph.isMoveIntoGroups() || graph.isMoveOutOfGroups()) {
					Object[] cells = context.getCells();
					Object ignoreGroup = graph.getModel().getParent(cells[0]);
					for (int i = 1; i < cells.length; i++) {
						Object tmp = graph.getModel().getParent(cells[i]);
						if (ignoreGroup != tmp) {
							ignoreGroup = null;
							break;
						}
					}
					if (ignoreGroup != null)
						ignoreTargetGroup = graph.getGraphLayoutCache()
								.getMapping(ignoreGroup, false);
				}
			}
		}
	}

}
