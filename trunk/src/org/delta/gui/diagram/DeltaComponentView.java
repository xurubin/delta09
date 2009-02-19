package org.delta.gui.diagram;

import java.net.URI;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.VertexView;

import com.kitfox.svg.app.beans.SVGIcon;

/**
 * Single class for all circuit diagram component views. The only difference between
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
	/** File name of icon for NandGate. */
	private static final String NAND_ICON = "nand.svg";
	/** File name of icon for OrGate. */
	private static final String OR_ICON = "or.svg";
	/** File name of icon for NorGate. */
	private static final String NOR_ICON = "nor.svg";
	/** File name of icon for XorGate. */
	private static final String XOR_ICON = "xor.svg";
	/** File name of icon for XnorGate. */
	private static final String XNOR_ICON = "xnor.svg";
	/** File name of icon for Ledr. */
	private static final String LEDR_ICON = "ledr.svg";
	/** File name of icon for Ledg. */
	private static final String LEDG_ICON = "ledg.svg";
	/** File name of icon for HighInput. */
	private static final String HIGH_ICON = "high.svg";
	/** File name of icon for LowInput. */
	private static final String LOW_ICON = "low.svg";
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
		else if (cell instanceof NandGate)
			this.setIcon(NAND_ICON);
		else if (cell instanceof OrGate)
			this.setIcon(OR_ICON);
		else if (cell instanceof NorGate)
			this.setIcon(NOR_ICON);
		else if (cell instanceof XorGate)
			this.setIcon(XOR_ICON);
		else if (cell instanceof XnorGate)
			this.setIcon(XNOR_ICON);
		else if (cell instanceof Ledr)
			this.setIcon(LEDR_ICON);
		else if (cell instanceof Ledg)
			this.setIcon(LEDG_ICON);
		else if (cell instanceof HighInput)
			this.setIcon(HIGH_ICON);
		else if (cell instanceof LowInput)
			this.setIcon(LOW_ICON);
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
	@Override
	public CellViewRenderer getRenderer() {
		return renderer;
	}
	
}
