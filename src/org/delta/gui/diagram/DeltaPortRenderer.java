package org.delta.gui.diagram;

import org.jgraph.graph.CellView;
import org.jgraph.graph.PortRenderer;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.PortView;
import org.jgraph.JGraph;

import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Component;

public class DeltaPortRenderer extends PortRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	transient protected Color color;

	public Component getRendererComponent(JGraph graph, CellView view,
			boolean sel, boolean focus, boolean preview) {
		// Check type
		if (view instanceof PortView && graph != null) {
			graphBackground = graph.getBackground();
			this.view = (PortView) view;
			this.color = DeltaInputPortView.color;
			this.hasFocus = focus;
			this.selected = sel;
			this.preview = preview;
			this.xorEnabled = graph.isXorEnabled();
			return this;
		}
		return null;
	}
	
	public void paint(Graphics g) {
		Dimension d = new Dimension(5,5);
		if (xorEnabled) {
			g.setColor(color);
			g.setXORMode(color);
		}
		super.paint(g);
		if (preview) {
			g.fill3DRect(0, 0, d.width, d.height, true);
		} else {
			g.fillOval(0, 0, d.width, d.height);
		}
		boolean offset = (GraphConstants.getOffset(view.getAllAttributes()) != null);
		g.setColor(getForeground());
		if (!offset)
			g.fillRect(1, 1, d.width - 2, d.height - 2);
		else if (!preview)
			g.drawRect(1, 1, d.width - 3, d.height - 3);
	}
}
