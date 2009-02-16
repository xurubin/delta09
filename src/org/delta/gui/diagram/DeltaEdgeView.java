package org.delta.gui.diagram;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jgraph.graph.CellHandle;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphContext;
import org.jgraph.graph.PortView;

/**
 * @author Group Delta 2009
 * View class for Edges. It's main purpose is to allow the use of a DeltaEdgeRenderer,
 * but it also deals with creating the initial control point on an edge.
 */
public class DeltaEdgeView extends EdgeView {

	/** Needed for correct serialization. */
	private static final long serialVersionUID = 1L;
	
	/** Renderer for the class. */
	public static transient DeltaEdgeRenderer renderer = new DeltaEdgeRenderer();
	
	/**
	 * Constructs an empty DeltaEdgeView (calls super class's constructor).
	 */
	public DeltaEdgeView() {
		super();
	}
	
	/**
	 * Constructs a DeltaEdgeView for the specified "model" object
	 * (calls super class's constructor).
	 * @param cell - reference to the "model" cell being represented.
	 */
	public DeltaEdgeView(Object cell) {
		super(cell);
	}
	
	/**
	 * Returns the local renderer. Do not access the renderer field directly.
	 * Use this method instead. Note: This method is package private. This had
	 * to be overwritten to return a DeltaEdgeRenderer rather than a plain
	 * EdgeRenderer. Note getShape and getBounds have also been overwritten.
	 */
	protected DeltaEdgeRenderer getEdgeRenderer() {
		return (DeltaEdgeRenderer) getRenderer();
	}
	
	/**
	 * Returns a renderer for the class.
	 */
	@Override
	public CellViewRenderer getRenderer() {
		return renderer;
	}
	
	/**
	 * Returns the shape of the view according to the last rendering state.
	 * Note this has been overwritten to call the local version of getEdgeRenderer.
	 */
	@Override
	public Shape getShape() {
		if (sharedPath != null)
			return sharedPath;
		else {
			return sharedPath = (GeneralPath) getEdgeRenderer().createShape();
		}
	}
	
	/**
	 * Returns the location for this edgeview.
	 * Note this has been overwritten to call the local version of getEdgeRenderer.
	 */
	public Rectangle2D getBounds() {
		Rectangle2D rect = super.getBounds();
		if (rect == null) {
			if (cachedBounds == null) {
				cachedBounds = getEdgeRenderer().getBounds(this);
			}
			rect = cachedBounds;
		}
		return rect;
	}
	
	/**
	 * Returns a DeltaEdgeHandle for the view.
	 */
	@Override
	public CellHandle getHandle(GraphContext context) {
		return new DeltaEdgeHandle(this, context);
	}

	/**
	 * Sets the <code>sourceView</code> of the edge.
	 */
	public void setSource(CellView sourceView) {
		sourceParentView = null;
		source = sourceView;
		if (source != null)
			points.set(0, source);
		else
			points.set(0, getPoint(0));
		// If the only points are the source and target, add a middle point
		if ((target != null) && (points.size() == 2))
			this.addPoint(1, getMidPoint());
		invalidate();
	}

	/**
	 * Sets the <code>targetView</code> of the edge.
	 */
	public void setTarget(CellView targetView) {
		target = targetView;
		targetParentView = null;
		int n = points.size() - 1;
		if (target != null)
			points.set(n, target);
		else
			points.set(n, getPoint(n));
		// If the only points are the source and target, add a middle point
		if ((source != null) && (points.size() == 2))
			this.addPoint(1, getMidPoint());
		invalidate();
	}
	
	/**
	 * Internal method used to set the initial control point when the edge is
	 * assigned a source and target for the first time.
	 * @return the point midway (along the diagonal) between source and target.
	 */
	private Point2D.Double getMidPoint() {
		// Read initial end points
		PortView sourceView = (PortView)source;
		PortView targetView = (PortView)target;
		Point2D.Double source = (Point2D.Double)sourceView.getLocation();
		Point2D.Double target = (Point2D.Double)targetView.getLocation();
		// Construct new point halfway between them
		double diffX = target.getX() - source.getX();
		double newX = source.getX() + diffX/2;
		double diffY = target.getY() - source.getY();
		double newY = source.getY() + diffY/2;
		Point2D.Double midPoint = new Point2D.Double(newX,newY);
		return midPoint;
	}

	/**
	 * @author Group Delta 2009
	 * Customised edge handle that uses double clicks to add/remove points.
	 */
	public class DeltaEdgeHandle extends EdgeView.EdgeHandle {

		/** Needed for correct serialization. */
		private static final long serialVersionUID = 1L;

		/**
		 * Creates a new DeltaEdgeHandle.
		 * @param edge - the edge the handle is for.
		 * @param ctx - the graph context.
		 */
		public DeltaEdgeHandle(EdgeView edge, GraphContext ctx) {
			super(edge, ctx);
		}
		
		/**
		 * Tests a MouseEvent to see if it represents a point being added to the edge.
		 * @param event - MouseEvent to test.
		 * @return true if the MouseEvent should be interpreted as adding a point to the edge.
		 */
		@Override
		public boolean isAddPointEvent(MouseEvent event) {
			// Points are added using a double click
			return (event.getClickCount() == 2);
		}

		/**
		 * Tests a MouseEvent to see if it represents a point being removed from the edge.
		 * @param event - MouseEvent to test.
		 * @return true if the MouseEvent should be interpreted as removing a point to the edge.
		 */
		@Override
		public boolean isRemovePointEvent(MouseEvent event) {
			// Points are removed using a double click
			return (event.getClickCount() == 2);
		}
		
		/**
		 * Handle mouse pressed event - will either start a control point drag,
		 * add a point, remove a point, or do nothing.
		 * @param event - MouseEvent that triggered this method.
		 */
		@Override
		public void mousePressed(MouseEvent event) {
			/* INV: currentPoint = null; source = target = label = false; */
			if (!edge.isLeaf())
				return;
			boolean bendable = graph.isBendable()
					&& GraphConstants.isBendable(edge.getAllAttributes());
			int x = event.getX();
			int y = event.getY();
			
			// Detect hit on control point
			int index = 0;
			for (index = 0; index < r.length; index++) {
				if (r[index].contains(x, y)) {
					currentPoint = edge.getPoint(index);
					currentIndex = index;
					source = index == 0;
					target = index == r.length - 1;
					break;
				}
			}
			
			// Remove Point
			if (isRemovePointEvent(event)
					&& currentPoint != null
					&& !source
					&& !target
					&& bendable
					&& (edge.getSource() == null || currentIndex > 0)
					&& (edge.getTarget() == null || currentIndex < edge
							.getPointCount() - 1)) {
				edge.removePoint(index);
				edgeModified = true;
				mouseReleased(event);
			
			// Add Point
			} else if (isAddPointEvent(event) && !isEditing() && bendable) {
				int s = graph.getHandleSize();
				Rectangle2D rect = graph.fromScreen(new Rectangle(x - s, y - s,
						2 * s, 2 * s));
				if (edge.intersects(graph, rect)) {
					Point2D point = graph.fromScreen(graph.snap(new Point(event
							.getPoint())));
					double min = Double.MAX_VALUE, dist = 0;
					for (int i = 0; i < edge.getPointCount() - 1; i++) {
						Point2D p = edge.getPoint(i);
						Point2D p1 = edge.getPoint(i + 1);
						dist = new Line2D.Double(p, p1).ptSegDistSq(point);
						if (dist < min) {
							min = dist;
							index = i + 1;
						}
					}
					edge.addPoint(index, point);
					edgeModified = true;
					currentPoint = point;
					reloadPoints(edge);
					paint(graph.getGraphics());
				}
			}
			if (isEditing())
				event.consume();
		}

	}
}
