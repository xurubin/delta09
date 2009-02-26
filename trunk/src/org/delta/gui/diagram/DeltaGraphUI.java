package org.delta.gui.diagram;

import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.jgraph.graph.AbstractCellView;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.CellHandle;
import org.jgraph.graph.CellView;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphContext;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.ParentMap;
import org.jgraph.plaf.basic.BasicGraphUI;

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
		
		@Override
		@SuppressWarnings("unchecked")
		public void mouseReleased(MouseEvent event) {
			try {
				if (event != null && !event.isConsumed()) {
					if (activeHandle != null) {
						activeHandle.mouseReleased(event);
						activeHandle = null;
					} else if (isMoving && !event.getPoint().equals(start)) {
						if (cachedBounds != null) {
							
							Point ep = event.getPoint();
							Point2D point = new Point2D.Double(ep.getX()
									- _mouseToViewDelta_x, ep.getY()
									- _mouseToViewDelta_y);
							Point2D snapCurrent = graph.snap(point);
							
							double dx = snapCurrent.getX() - start.getX();
							double dy = snapCurrent.getY() - start.getY();
							
							if (!graph.isMoveBelowZero() && initialLocation.getX() + dx < 0)
							    dx = -1 * initialLocation.getX();
							if (!graph.isMoveBelowZero() && initialLocation.getY() + dy < 0)
							    dy = -1 * initialLocation.getY();
							
							Point2D tmp = graph.fromScreen(new Point2D.Double(
									dx, dy));
							GraphLayoutCache.translateViews(views, tmp.getX(),
									tmp.getY());
						}
						CellView[] all = graphLayoutCache
								.getAllDescendants(views);
						Map attributes = GraphConstants.createAttributes(all,
								null);
						if (event.isControlDown() && graph.isCloneable()) { // Clone
							// Cells
							Object[] cells = graph.getDescendants(graph
									.order(context.getCells()));
							// Include properties from hidden cells
							Map hiddenMapping = graphLayoutCache
									.getHiddenMapping();
							for (int i = 0; i < cells.length; i++) {
								Object witness = attributes.get(cells[i]);
								if (witness == null) {
									CellView view = (CellView) hiddenMapping
											.get(cells[i]);
									if (view != null
											&& !graphModel.isPort(view
													.getCell())) {
										// TODO: Clone required? Same in
										// GraphConstants.
										AttributeMap attrs = (AttributeMap) view
												.getAllAttributes().clone();
										// Maybe translate?
										// attrs.translate(dx, dy);
										attributes.put(cells[i], attrs.clone());
									}
								}
							}
							ConnectionSet cs = ConnectionSet.create(graphModel,
									cells, false);
							ParentMap pm = ParentMap.create(graphModel, cells,
									false, true);
							cells = graphLayoutCache.insertClones(cells, graph
									.cloneCells(cells), attributes, cs, pm, 0,
									0);
						} else if (graph.isMoveable()) { // Move Cells
							ParentMap pm = null;

							// Moves into group
							if (targetGroup != null) {
								pm = new ParentMap(context.getCells(),
										targetGroup.getCell());
							} else if (graph.isMoveOutOfGroups()
									&& (ignoreTargetGroup != null && !ignoreTargetGroup
											.getBounds().intersects(
													AbstractCellView
															.getBounds(views)))) {
								pm = new ParentMap(context.getCells(), null);
							}
							graph.getGraphLayoutCache().edit(attributes,
									disconnect, pm, null);
						}
						/* 
						 * Realign points on edges around the components that have
						 * been moved.
						 */
						// Find all the edges connected to the cells that have been moved
						Set<DeltaEdge> edges = DeltaGraphModel.getEdges(graph.getModel(),
								graph.getGraphLayoutCache().getCells(views));
						int startPoint = 0;
						// For each edge, find its view and call the realign points method
						for (DeltaEdge edge : edges) {
							DeltaEdgeView view = (DeltaEdgeView)graph.getGraphLayoutCache().
								getMapping(edge,false);
							for (int i=0; i<views.length; i++) {
								if (views[i] == edge.getSource())
									startPoint = 0;
								else if (views[i] == edge.getTarget())
									startPoint = view.getPointCount()-1;
							}
							view.realignPointsAround(startPoint, false, false);
						}
						event.consume();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ignoreTargetGroup = null;
				targetGroup = null;
				isDragging = false;
				disconnect = null;
				firstDrag = true;
				current = null;
				start = null;
			}
		}
	}

}
