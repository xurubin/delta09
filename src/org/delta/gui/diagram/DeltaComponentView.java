package org.delta.gui.diagram;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.VertexView;
import java.net.URI;

import org.jgraph.graph.GraphConstants;

import com.kitfox.svg.app.beans.SVGIcon;

/**
 * Single lass for all circuit diagram component views. The only difference between
 * the different views is the icon used to represent them, so this the icon for
 * each view is set based on the cell it is referencing.
 * @author Group Delta 2009
 */
public class DeltaComponentView extends VertexView {
	/** Needed for correct serialization. */
	private static final long serialVersionUID = 1L;

	/** Static reference to the renderer for all AndGateViews. */
	private static DeltaComponentRenderer renderer = new DeltaComponentRenderer();
	
	/** Static String representing the path to the folder where all icons are stored. */
	private static final String ICONFOLDERPATH = "org/delta/gui/diagram/images/";
	
	/** File name of icon for AndGate. */
	private static final String AND_ICON = "and.svg";
	/** File name of icon for OrGate. */
	private static final String OR_ICON = "or.svg";
	// TODO: Add file names for all the other component icons.
	
	/**
	 * Create a "blank" DeltaComponentView (should never be needed).
	 */
	public DeltaComponentView() {
		super();
	}
	
	/**
	 * Create a new view with a reference to the given cell.
	 * @param cell - component the view is being created for.
	 */
	public DeltaComponentView(Object cell) {
		super(cell);
		if (cell instanceof AndGate)
			this.setIcon(AND_ICON);
		else if (cell instanceof OrGate)
			this.setIcon(OR_ICON);
		// TODO: Add if statements to set icons for all the other types of component.
	}
	
	/**
	 * Set the icon used to represent this component on the diagram.
	 * @param iconFileName - filename of image.
	 */
	public void setIcon(String iconFileName) {
		// Attempt to create SVG icon
		String iconPath = ICONFOLDERPATH + iconFileName;
		SVGIcon icon = new SVGIcon();
		try {
			URI svgURI = new URI(DeltaComponentView.class.getClassLoader().getResource(iconPath).toString());
			icon.setSvgURI(svgURI);
			icon.setScaleToFit(true);
			icon.setAntiAlias(true);
		} catch(Exception e) {}
		
		// Modify the view's attributes to use this icon
		GraphConstants.setIcon(this.getAttributes(),icon);
		GraphConstants.setAutoSize(this.getAttributes(),true);
	}
	
	/**
	 * Get the renderer for all DeltaComponentViews.
	 * @return the renderer.
	 */
	public CellViewRenderer getRenderer() {
		return renderer;
	}
	
}
