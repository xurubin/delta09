package org.delta.gui.diagram;

import java.awt.BasicStroke;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import org.jgraph.JGraph;
import org.jgraph.graph.EdgeRenderer;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;

/**
 * Extension of EdgeRenderer that overrides createShape so that the path
 * drawn along the Edge follows right angles from one control point to
 * another.
 * @author Group Delta 2009
 */
public class DeltaEdgeRenderer extends EdgeRenderer {
	/** Needed for correct serialization. */
	private static final long serialVersionUID = 1L;
	
	/** Constant to make line drawing code clearer. */
	private static final int HORIZ = 0;
	
	/** Constant to make line drawing code clearer. */
	private static final int VERT = 1;
	
	/**
	 * Returns the shape that represents the current edge in the context of the
	 * current graph. This method sets the global beginShape, lineShape and
	 * endShape variables as a side-effect.
	 * <br><br>
	 * Note: Altered by Group Delta to implement moveable orthogonal lines.
	 * Between each pair of control points a right-angled line is drawn, with
	 * the orientation depending upon whether the control point is an odd or
	 * even number from the start of the edge. The result is that each control
	 * point appears to control one horizontal or vertical "segment" of the wire.
	 */
	@Override
	protected Shape createShape() {
		int n = view.getPointCount();
		if (n > 1) {
			// Following block may modify static vars as side effect (Flyweight Design)
			EdgeView tmp = view;
			Point2D[] p = null;
			p = new Point2D[n];
			for (int i = 0; i < n; i++) {
				Point2D pt = tmp.getPoint(i);
				if (pt == null)
					return null; // exit
				p[i] = new Point2D.Double(pt.getX(), pt.getY());
			}

			// End of Side-Effect Block
			// Undo Possible MT-Side Effects
			if (view != tmp) {
				view = tmp;
				installAttributes(view);
			}
			// End of Undo
			if (view.sharedPath == null) {
				view.sharedPath = new GeneralPath(GeneralPath.WIND_NON_ZERO, n);
			} else {
				view.sharedPath.reset();
			}
			view.beginShape = view.lineShape = view.endShape = null;
			Point2D p0 = p[0];
			Point2D pe = p[n - 1];
			view.sharedPath.moveTo((float) p0.getX(), (float) p0.getY());
			// START OF GROUP DELTA CODE
			for (int i = 0; i < n - 1; i++) {
				int orientation = (i % 2 == 0) ? HORIZ : VERT;
				double newX, newY;
				if (orientation == HORIZ) {
					newX = p[i+1].getX();
					newY = p[i].getY();
				}
				else {
					newX = p[i].getX();
					newY = p[i+1].getY();
				}
				view.sharedPath.lineTo(newX,newY);
				view.sharedPath.lineTo(p[i+1].getX(),p[i+1].getY());
			}
			// END OF GROUP DELTA CODE
			view.sharedPath.moveTo((float) pe.getX(), (float) pe.getY());
			if (view.endShape == null && view.beginShape == null) {
				// With no end decorations the line shape is the same as the
				// shared path and memory
				view.lineShape = view.sharedPath;
			} else {
				view.lineShape = (GeneralPath) view.sharedPath.clone();
				if (view.endShape != null)
					view.sharedPath.append(view.endShape, true);
				if (view.beginShape != null)
					view.sharedPath.append(view.beginShape, true);
			}
			return view.sharedPath;
		}
		return null;
	}
	
	/**
	 * Paint the renderer.
	 * 
	 * Note: Overridden to use custom RenderingHints. Now uses a "normalized" stroke rather
	 * than a pure one and is anti-aliased.
	 */
	@Override
	public void paint(Graphics g) {
		if (view.isLeaf()) {
			Shape edgeShape = view.getShape();
			// Sideeffect: beginShape, lineShape, endShape
			if (edgeShape != null) {
				Graphics2D g2 = (Graphics2D) g;
				// Rendering hints altered from original
				RenderingHints hintsMap = new RenderingHints(
						RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
				hintsMap.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setRenderingHints(hintsMap);
				int c = BasicStroke.CAP_BUTT;
				int j = BasicStroke.JOIN_MITER;
				setOpaque(false);
				translateGraphics(g);
				g.setColor(getForeground());
				if (lineWidth > 0) {
					g2.setStroke(new BasicStroke(lineWidth, c, j));
					if (gradientColor != null && !preview) {
						g2.setPaint(new GradientPaint(0, 0, getBackground(),
								getWidth(), getHeight(), gradientColor, true));
					}
					if (view.beginShape != null) {
						if (beginFill)
							g2.fill(view.beginShape);
						g2.draw(view.beginShape);
					}
					if (view.endShape != null) {
						if (endFill)
							g2.fill(view.endShape);
						g2.draw(view.endShape);
					}
					if (lineDash != null) // Dash For Line Only
						g2.setStroke(new BasicStroke(lineWidth, c, j, 10.0f,
								lineDash, dashOffset));
					if (view.lineShape != null)
						g2.draw(view.lineShape);
				}

				if (selected) { // Paint Selected
					g2.setStroke(GraphConstants.SELECTION_STROKE);
					g2.setColor(highlightColor);
					if (view.beginShape != null)
						g2.draw(view.beginShape);
					if (view.lineShape != null)
						g2.draw(view.lineShape);
					if (view.endShape != null)
						g2.draw(view.endShape);
				}
				g2.setStroke(new BasicStroke(1));
				g
						.setFont((extraLabelFont != null) ? extraLabelFont
								: getFont());
				Object[] labels = GraphConstants.getExtraLabels(view
						.getAllAttributes());
				JGraph graph = (JGraph)this.graph.get();
				if (labels != null) {
					for (int i = 0; i < labels.length; i++)
						paintLabel(g, graph.convertValueToString(labels[i]),
								getExtraLabelPosition(view, i),
								false || !simpleExtraLabels);
				}
				if (graph.getEditingCell() != view.getCell()) {
					g.setFont(getFont());
					Object label = graph.convertValueToString(view);
					if (label != null) {
						paintLabel(g, label.toString(), getLabelPosition(view),
								true);
					}
				}
			}
		} else {
			paintSelectionBorder(g);
		}
	}
	
}
