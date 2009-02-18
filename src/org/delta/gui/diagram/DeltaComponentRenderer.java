package org.delta.gui.diagram;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.VertexRenderer;

/**
 * This subclass implements custom painting of the selection border, most
 * of the painting is still done by the parent class.
 * @author Group Delta 2009
 */
public class DeltaComponentRenderer extends VertexRenderer {
	/** Needed for correct serialization. */
	private static final long serialVersionUID = 1L;
	
	/** Transient from super class that must be reproduced here. */
	protected transient Color gridColor = Color.black;
	
	/**
	 * Transient from super class that must be reproduced here. Is the colour
	 * of the box drawn around a component when selected and being dragged.
	 */
	protected transient Color highlightColor = new Color(255,153,0);
	
	/**
	 * Transient from super class that must be reproduced here. Is the colour
	 * of the box drawn around a component when selected but not being dragged.
	 */
	protected transient Color lockedHandleColor = new Color(255,153,0);
	
	/**
	 * Overwritten to provide custom component rendering, specifically the
	 * colour of selected cells.
	 * @param g - Graphics context to draw the component onto.
	 */
	protected void paintSelectionBorder(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Stroke previousStroke = g2.getStroke();
		g2.setStroke(GraphConstants.SELECTION_STROKE);
		if (childrenSelected || selected) {
			if (childrenSelected)
				g.setColor(gridColor);
			else if (hasFocus && selected)
				g.setColor(lockedHandleColor);
			else if (selected)
				g.setColor(highlightColor);
			Dimension d = getSize();
			g.drawRect(0, 0, d.width - 1, d.height - 1);
		}
		g2.setStroke(previousStroke);
	}
	
}
