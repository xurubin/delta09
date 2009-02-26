package org.delta.gui.diagram;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jgraph.JGraph;
import org.jgraph.graph.CellHandle;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphContext;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.PortView;

/**
 * @author Group Delta 2009
 * View class for Edges. It allows us to use DeltaEdgeRenderer and deals with
 * adding, removing and moving of control points on the edge.
 */
public class DeltaEdgeView extends EdgeView {
	/** Needed for correct serialization. */
	private static final long serialVersionUID = 1L;
	
	/** Renderer for the class. */
	public static transient DeltaEdgeRenderer renderer = new DeltaEdgeRenderer();
	
	/** Cached coordinates of start point (i.e. source port). */
	//private transient Point2D cachedStartPoint;
	
	/** Cached coordinates of end point (i.e. target port). */
	//private transient Point2D cachedEndPoint;
	
	/** Indicates if the start and end points have been cached yet. */
	//private transient boolean pointsCached = false;
	
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
	 * Returns true if this view intersects the given rectangle.
	 * Note this has been overwritten to call the local version of getEdgeRenderer.
	 */
	@Override
	public boolean intersects(JGraph graph, Rectangle2D rect) {
		boolean intersects = super.intersects(graph, rect);
		if (!isLeaf()) {
			return intersects;
		} else if (intersects) {
			Rectangle r = new Rectangle((int) rect.getX(), (int) rect.getY(),
					(int) rect.getWidth(), (int) rect.getHeight());
			return getEdgeRenderer().intersects(graph, this, r);
		}
		return false;
	}
	
	/**
	 * Returns a DeltaEdgeHandle for the view.
	 */
	@Override
	public CellHandle getHandle(GraphContext context) {
		return new DeltaEdgeHandle(this, context);
	}
	
	/**
	 * Update attributes and recurse children.
	 * NOTE: Overwritten for custom edge routing.
	 * @param cache - the graphLayoutCache with this view.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void update(GraphLayoutCache cache) {
		super.update(cache);
		// Save the reference to the points so they can be changed
		// in-place by use of setPoint, setSource, setTarget methods.
		List controlPoints = GraphConstants.getPoints(allAttributes);
		if (controlPoints == null) {
			controlPoints = new ArrayList(4);
			controlPoints.add(allAttributes.createPoint(10, 10));
			controlPoints.add(allAttributes.createPoint(20, 20));
			GraphConstants.setPoints(allAttributes, controlPoints);
		}

		// Uses the manual control points while the edge is being routed.
		// Otherwise uses the cached points (eg. for preview).
		if (points == null)
			points = controlPoints;

		// Overrides manual point locations with the real port views
		if (points == controlPoints) {
			if (source != null)
				setSource(source);
			if (target != null)
				setTarget(target);
		}
		
		// If the only points are the source and target, add a middle point
		if ((source != null) && (points.size() == 2))
			this.addPoint(1, getMidPoint());
		
		// Clear cached shapes
		beginShape = null;
		endShape = null;
		lineShape = null;
		invalidate();
		
		// Realign outermost "real points" (i.e. not ports) if ports have moved
		//pointsCached = (cachedStartPoint != null) && (cachedEndPoint != null);
		//Point2D currentStartPoint = this.getPoint(0);
		//Point2D currentEndPoint = this.getPoint(this.getPointCount()-1);
		/*if (pointsCached && !cachedStartPoint.equals(this.getPoint(0))) {
			this.realignPointsAround(0,false,false);
		}
		if (pointsCached && !cachedEndPoint.equals(this.getPoint(this.getPointCount()-1))) {
			this.realignPointsAround(this.getPointCount()-1,false,false);
		}
		cachedStartPoint = (Point2D)this.getPoint(0).clone();
		cachedEndPoint = (Point2D)this.getPoint(this.getPointCount()-1).clone();*/
	}
	
	/**
	 * Protected method to realign all the points on either side of one
	 * that has been moved, inserted or removed. The aim is that every control
	 * point should be in the middle of the "edge" that it causes to be
	 * rendered.
	 * @param index - position of point in the edge.
	 * @param added - true if this point has just been added to the edge.
	 * @param removed - true if this point has just been removed from the edge.
	 */
	protected void realignPointsAround(int index, boolean added, boolean removed) {
		// Realign this point
		Point2D.Double thisPoint = this.getRealignedPoint(index);
		if (thisPoint != null)
			this.setPoint(index, thisPoint);
		
		// SPECIAL CASE - realigning after point added to edge
		/*
		 * Create a new point after the current one. Because we are using control
		 * points with a "horizontal/vertical - even/odd" system, adding a new
		 * point would normally change the orientation of all the control points
		 * after it. To stop this we create an extra point, effectively adding a
		 * "zig-zag" to the line.
		 */
		if (added) {
			// Special case - the point was added just before the target port
			int tempIndex = (index == this.getPointCount()-2) ? --index : index;
			Point2D.Double newPoint = this.getRealignedPoint(tempIndex+1);
			if (newPoint != null)
				this.addPoint(tempIndex+1, newPoint);
		}
		// SPECIAL CASE - realigning after point removed from edge
		/*
		 * Similarly if a point is removed we should remove the point after it as well.
		 */
		if (removed) {
			// Special case - if removing the last (non-port) point
			int tempIndex = (index == this.getPointCount()-1) ? --index : index;
			this.removePoint(tempIndex);
			// "index" now points to the next point, so must realign this
			Point2D.Double point = this.getRealignedPoint(tempIndex);
			if (point != null) 
				this.setPoint(tempIndex, point);
		}
		
		// Realign all points from this point to the source
		Point2D.Double prevPoint;
		for (int i=index-1; i>0; i--) {
			prevPoint = this.getRealignedPoint(i);
			if (prevPoint != null) 
				this.setPoint(i, prevPoint);
		}
		// Realign all points from this point to the target
		Point2D.Double nextPoint;
		for (int j=index+1; j<this.getPointCount()-1; j++) {
			nextPoint = this.getRealignedPoint(j);
			if (nextPoint != null)
				this.setPoint(j, nextPoint);
		}
	}
	
	/**
	 * Private method to realign an individual point based on the ones around it.
	 * @param index - position of point in the edge.
	 * @return the realigned point.
	 */
	private Point2D.Double getRealignedPoint(int index) {
		// Do not realign invalid ports
		if ((index <= 0) || (index >= this.getPointCount()-1))
			return null;
		// Find this point and the points on either side of it
		Point2D prevPoint = this.getPoint(index - 1);
		Point2D thisPoint = this.getPoint(index);
		Point2D nextPoint = this.getPoint(index + 1);
		// Initialise new position of this point to its current position
		double newX = thisPoint.getX();
		double newY = thisPoint.getY();
		// Even indexed points represent horizontal edges, odd vertical.
		if (index % 2 == 0) {
			// "Horizontal" points only have their x coordinate realigned
			double diffX = nextPoint.getX() - prevPoint.getX();
			newX = prevPoint.getX() + diffX/2;
		}
		else {
			// "Vertical" edges only have their y coordinate realigned
			double diffY = nextPoint.getY() - prevPoint.getY();
			newY = prevPoint.getY() + diffY/2;
		}
		return new Point2D.Double(newX,newY);
		//this.setPoint(index, new Point2D.Double(newX,newY));
	}
	
	/**
	 * Private method used to set the initial control point when the edge is
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
		if ((source != null) && (target != null)) {
			double diffX = target.getX() - source.getX();
			double newX = source.getX() + diffX/2;
			double diffY = target.getY() - source.getY();
			double newY = source.getY() + diffY/2;
			return new Point2D.Double(newX,newY);
		}
		return null;
	}
	
	/**
	 * Sets the <code>sourceView</code> of the edge.
	 */
	@SuppressWarnings("unchecked")
	public void setSource(CellView sourceView) {
		sourceParentView = null;
		source = sourceView;
		if (source != null)
			points.set(0, source);
		else
			points.set(0, getPoint(0));
		//if (!cachedStartPoint.equals(this.getPoint(0)))
			//this.realignPointsAround(0,false,false);
		// If the only points are the source and target, add a middle point
		//if ((target != null) && (points.size() == 2))
			//this.addPoint(1, getMidPoint());
		invalidate();
	}

	/**
	 * Sets the <code>targetView</code> of the edge.
	 */
	@SuppressWarnings("unchecked")
	public void setTarget(CellView targetView) {
		target = targetView;
		targetParentView = null;
		int n = points.size() - 1;
		if (target != null)
			points.set(n, target);
		else
			points.set(n, getPoint(n));
		//if (!cachedEndPoint.equals(this.getPoint(n)))
			//this.realignPointsAround(n,false,false);
		// If the only points are the source and target, add a middle point
		//if ((source != null) && (points.size() == 2))
			//this.addPoint(1, getMidPoint());
		invalidate();
		
	}

	/**
	 * @author Group Delta 2009
	 * Customised edge handle that uses double clicks to add/remove points.
	 */
	public class DeltaEdgeHandle extends EdgeView.EdgeHandle {

		/** Needed for correct serialization. */
		private static final long serialVersionUID = 1L;
		
		/** Indicates whether a point has been removed. Needed for point realignment. */
		private transient boolean pointRemoved = false;
		
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
				pointRemoved = true;
				reloadPoints(edge);
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
					realignPointsAround(index,true,false);
					reloadPoints(edge);
					paint(graph.getGraphics());
				}
			}
			if (isEditing())
				event.consume();
		}
		
		/**
		 * Handle mouse drag event. This is overritten to constrain the way
		 * edges (and particularly their control points) can be dragged.
		 * It also realigns neighbouring points after a point is moved.
		 * @param event - MouseEvent that triggered this method.
		 */
		@Override
		public void mouseDragged(MouseEvent event) {
			Point2D p = graph.fromScreen(new Point(event.getPoint()));
			// Move control point
			if (isEditing() && currentPoint != null) {
				boolean disconnectable = (!source && !target)
						|| (graph.isDisconnectable() && GraphConstants
								.isDisconnectable(orig.getAllAttributes()));
				if (source)
					disconnectable = disconnectable
							&& ((orig.getSource() == null && orig
									.getSourceParentView() == null)
									|| (orig.getSource() != null && GraphConstants
											.isDisconnectable(orig.getSource()
													.getParentView()
													.getAllAttributes())) || (orig
									.getSourceParentView() != null && GraphConstants
									.isDisconnectable(orig
											.getSourceParentView()
											.getAllAttributes())));
				if (target)
					disconnectable = disconnectable
							&& ((orig.getTarget() == null && orig
									.getTargetParentView() == null)
									|| (orig.getTarget() != null && GraphConstants
											.isDisconnectable(orig.getTarget()
													.getParentView()
													.getAllAttributes())) || (orig
									.getTargetParentView() != null && GraphConstants
									.isDisconnectable(orig
											.getTargetParentView()
											.getAllAttributes())));
				// Find Source/Target Port
				if (!((source && snap(true, event.getPoint())) || (target && snap(
						false, event.getPoint())))
						&& disconnectable) {
					// Else Use Point
					boolean acceptSource = source
							&& (graph.getModel().acceptsSource(edge.getCell(),
									null) || graph.isPreviewInvalidNullPorts());
					boolean acceptTarget = target
							&& (graph.getModel().acceptsTarget(edge.getCell(),
									null) || graph.isPreviewInvalidNullPorts());
					if (acceptSource || acceptTarget || !(source || target)) {
						edgeModified = true;
						Rectangle2D dirty = edge.getBounds();
						if (edge.getSource() != null) {
							dirty.add(edge.getSource().getBounds());
						}
						if (edge.getTarget() != null) {
							dirty.add(edge.getTarget().getBounds());
						}
						if (graph.isXorEnabled()) {
							overlay(graph.getGraphics());
						}
						p = graph.fromScreen(graph.snap(new Point(event
								.getPoint())));
						// Constrain movement to either horizontal/vertical
						// depending on control point being dragged
						if ((currentIndex != 0) && (currentIndex != edge.getPointCount()-1)) {
							// Reset Initial Positions
							EdgeView orig = (EdgeView) graph
									.getGraphLayoutCache().getMapping(
											edge.getCell(), false);
							Point2D origPoint = orig.getPoint(currentIndex);
							// Adjust new point based on horizontal/vertical
							if (currentIndex % 2 == 0)
								p.setLocation(origPoint.getX(), p.getY());
							else
								p.setLocation(p.getX(), origPoint.getY());
						}
						// Do not move into negative space
						p.setLocation(Math.max(0, p.getX()), Math.max(0, p
								.getY()));
						currentPoint.setLocation(p);
						if (source) {
							edge.setPoint(0, p);
							edge.setSource(null);
						} else if (target) {
							edge.setPoint(edge.getPointCount() - 1, p);
							edge.setTarget(null);
						}
						//realignPointsAround(currentIndex);
						edge.update(graph.getGraphLayoutCache());
						dirty.add(edge.getBounds());
						if (graph.isXorEnabled()) {
							overlay(graph.getGraphics());
						} else {
							if (edge.getSource() != null) {
								dirty.add(edge.getSource().getParentView()
										.getBounds());
							}
							if (edge.getTarget() != null) {
								dirty.add(edge.getTarget().getParentView()
										.getBounds());
							}
							graph.repaint((int) dirty.getX(), (int) dirty
									.getY(), (int) dirty.getWidth(),
									(int) dirty.getHeight());
						}
					}
				}
			}
		}
		
		/**
		 * Handle mouse released event.<br>
		 * NOTE: Overwritten to use DeltaEdgeView.update not EdgeView.update.
		 * @param e - MouseEvent that triggered this method.
		 */
		@SuppressWarnings("unchecked")
		public void mouseReleased(MouseEvent e) {
			boolean clone = e.isControlDown() && graph.isCloneable();
			GraphModel model = graph.getModel();
			Object source = (edge.getSource() != null) ? edge.getSource()
					.getCell() : null;
			Object target = (edge.getTarget() != null) ? edge.getTarget()
					.getCell() : null;
			// ADDED
			/*if (source instanceof DeltaOutputPort) {
				Object temp = source;
				source = target;
				target = temp;
			}
			// END*/
			if (edgeModified && model.acceptsSource(edge.getCell(), source)
					&& model.acceptsTarget(edge.getCell(), target)) {
				// If points have been moved or deleted, realign points
				if (edgeModified && (currentIndex > 0))
					realignPointsAround(currentIndex,false,pointRemoved);
				// Reset pointRemoved flag
				pointRemoved = false;

				// Creates the data required for the edit/insert call
				ConnectionSet cs = createConnectionSet(edge, clone);
				Map nested = GraphConstants.createAttributes(
						new CellView[] { edge }, null);

				// The cached points may be different from what's
				// in the attribute map if the edge is routed.
				Map tmp = (Map) nested.get(edge.getCell());
				List controlPoints = GraphConstants.getPoints(tmp);
				List currentPoints = edge.getPoints();

				// Checks if we're dealing with a routing algorithm
				// and if we are, replaces only the source and target
				// in the control point list.
				if (controlPoints != currentPoints) {
					controlPoints.set(0, edge.getPoint(0));
					controlPoints.set(controlPoints.size() - 1, edge
							.getPoint(edge.getPointCount() - 1));
				}

				if (clone) {
					Map cellMap = graph.cloneCells(graph
							.getDescendants(new Object[] { edge.getCell() }));
					processNestedMap(nested, true);
					nested = GraphConstants.replaceKeys(cellMap, nested);
					cs = cs.clone(cellMap);
					Object[] cells = cellMap.values().toArray();
					graph.getGraphLayoutCache().insert(cells, nested, cs, null,
							null);
				} else {
					processNestedMap(nested, false);
					graph.getGraphLayoutCache().edit(nested, cs, null, null);
				}
			} else {
				if (graph.isXorEnabled()) {
					overlay(graph.getGraphics());
				} else {
					Rectangle2D dirty = edge.getBounds();
					graph.repaint((int) dirty.getX(), (int) dirty.getY(),
							(int) dirty.getWidth(), (int) dirty.getHeight());
				}
				edge.refresh(graph.getGraphLayoutCache(), graph.getGraphLayoutCache(),
						false);
			}
			initialLabelLocation = null;
			currentPoint = null;
			this.edgeModified = false;
			this.label = false;
			this.source = false;
			this.target = false;
			currentLabel = -1;
			currentIndex = -1;
			firstOverlayCall = true;
			e.consume();
		}

	}
	
}
