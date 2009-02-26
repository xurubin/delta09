package org.delta.gui.diagram;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;

import javax.swing.JComponent;
import javax.swing.UIManager;

import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.PortView;

/**
 * Customised renderer for input and output ports. The main alteration from the
 * JGraph PortRenderer is in the paint() method.
 * @author Group Delta 2009
 */
public class DeltaPortRenderer extends JComponent implements CellViewRenderer,Serializable {	
	
	private static final long serialVersionUID = 1L;
	
	/** Cache the current port color for drwaing. */
	protected transient Color color;
	
	/** Cache the current PortView for drawing. */
	protected transient PortView view;
	
	/** Cache the current graph background. */
	protected Color graphBackground = Color.white;
	
	/** Cached hasFocus and selected value. */
	protected transient boolean hasFocus, selected, preview, xorEnabled;
	
	/**
	 * Constructs a renderer that may be used to render ports.
	 */
	public DeltaPortRenderer() {
		setForeground(UIManager.getColor("MenuItem.selectionBackground"));
		setBackground(UIManager.getColor("Tree.selectionBorderColor"));
	}
	
	/**
	 * Configure and return the renderer based on the passed in components. The
	 * value is typically set from messaging the graph with
	 * <code>convertValueToString</code>.
	 * <br><br>
	 * NOTE: Adapted for Project Delta - now caches copy of the color as well.
	 * 
	 * @param graph
	 *            the graph that that defines the rendering context.
	 * @param view
	 *            the cell view that should be rendered.
	 * @param sel
	 *            whether the object is selected.
	 * @param focus
	 *            whether the object has the focus.
	 * @param preview
	 *            whether we are drawing a preview.
	 * @return the component used to render the value.
	 */
	public Component getRendererComponent(JGraph graph, CellView view,
			boolean sel, boolean focus, boolean preview) {
		// Check type
		if (view instanceof PortView && graph != null) {
			graphBackground = graph.getBackground();
			this.view = (PortView) view;
			this.hasFocus = focus;
			this.selected = sel;
			this.preview = preview;
			this.xorEnabled = graph.isXorEnabled();
			installAttributes(view);
			return this;
		}
		return null;
	}
	
	/**
	 * Install the attributes of specified cell in this renderer instance. This
	 * means, retrieve every published key from the cells hashtable and set
	 * global variables or superclass properties accordingly.
	 * 
	 * @param view
	 *            the cell view to retrieve the attribute values from.
	 */
	protected void installAttributes(CellView view) {
		AttributeMap map = view.getAllAttributes();
		setForeground(GraphConstants.getForeground(map));
		setBackground(GraphConstants.getBackground(map));
	}
	
	/**
	 * Hook for subclassers that is invoked when the installAttributes is not
	 * called to reset all attributes to the defaults. <br>
	 * Subclassers must invoke the superclass implementation.
	 * 
	 */
	protected void resetAttributes() {
		setForeground(null);
	}
	
	/**
	 * Paint the renderer. Overrides superclass paint to add specific painting.
	 * Note: The preview flag is interpreted as "highlight" in this context.
	 * (This is used to highlight the port if the mouse is over it.)
	 * <br><br>
	 * NOTE: Adapted for Project Delta - custom painting of ports.
	 * 
	 * @param g - Graphics context to paint the port onto.
	 */
	public void paint(Graphics g) {
		Dimension size = getSize();
		super.paint(g);
		if (preview)
			g.setColor(getBackground());
		else
			g.setColor(getForeground());
		g.fillOval(0, 0, (int)size.getWidth(), (int)size.getHeight());
	}
	
	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	public void validate() {
	}
	
	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	public void revalidate() {
	}
	
	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	public void repaint(long tm, int x, int y, int width, int height) {
	}
	
	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	public void repaint(Rectangle r) {
	}
	
	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	protected void firePropertyChange(String propertyName, Object oldValue,
			Object newValue) {
		// Strings get interned...
		if (propertyName == "text")
			super.firePropertyChange(propertyName, oldValue, newValue);
	}
	
	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	public void firePropertyChange(String propertyName, byte oldValue,
			byte newValue) {
	}
	
	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	public void firePropertyChange(String propertyName, char oldValue,
			char newValue) {
	}
	
	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	public void firePropertyChange(String propertyName, short oldValue,
			short newValue) {
	}
	
	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	public void firePropertyChange(String propertyName, int oldValue,
			int newValue) {
	}
	
	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	public void firePropertyChange(String propertyName, long oldValue,
			long newValue) {
	}
	
	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	public void firePropertyChange(String propertyName, float oldValue,
			float newValue) {
	}
	
	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	public void firePropertyChange(String propertyName, double oldValue,
			double newValue) {
	}
	
	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	public void firePropertyChange(String propertyName, boolean oldValue,
			boolean newValue) {
	}
	
}
