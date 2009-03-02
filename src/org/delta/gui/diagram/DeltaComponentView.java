package org.delta.gui.diagram;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
	/** File name of icon for NotGate. */
	private static final String NOT_ICON = "not.svg";
	/** File name of icon for Ledr. */
	private static final String LEDR_ICON = "ledr.svg";
	/** File name of icon for Ledg. */
	private static final String LEDG_ICON = "ledg.svg";
	/** File name of icon for HighInput. */
	private static final String HIGH_ICON = "high.svg";
	/** File name of icon for LowInput. */
	private static final String LOW_ICON = "low.svg";
    /** File name of icon for DFlip (D flip-flop). */
	private static final String DFLIP_ICON = "d_flip.svg";
    /** File name of icon for Switch. */
	private static final String SWITCH_ICON = "switch.svg";
    /** File name of icon for PushButton. */
	private static final String PUSH_BUTTON_ICON = "push_button.svg";
    /** File name of icon for SevenSegment. */
	private static final String SEVENSEG_ICON = "7seg.svg";
    /** File name of icon for RsLatch. */
	private static final String RSLATCH_ICON = "rs_latch.svg";
    /** File name of icon for ROM. */
	private static final String ROM_ICON = "rom.svg";
    /** File name of icon for RAM. */
	private static final String RAM_ICON = "ram.svg";
    /** File name of icon for Nand3Gate (3 input Nand gate). */
	private static final String NAND3_ICON = "nand3.svg";
    /** File name of icon for a Nor3Gate (3 input Nor gate). */
	private static final String NOR3_ICON = "nor3.svg";
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
		this.setIcon();
	}
	
	/**
	 * Set the icon used to represent this component on the diagram.
	 * @param iconFileName - filename of image.
	 */
	public void setIcon() {
		// Choose which icon to use
		String iconFileName = "";
		if (cell instanceof AndGate)
			iconFileName = AND_ICON;
		else if (cell instanceof NandGate)
			iconFileName = NAND_ICON;
		else if (cell instanceof OrGate)
			iconFileName = OR_ICON;
		else if (cell instanceof NorGate)
			iconFileName = NOR_ICON;
		else if (cell instanceof XorGate)
			iconFileName = XOR_ICON;
		else if (cell instanceof XnorGate)
			iconFileName = XNOR_ICON;
		else if (cell instanceof NotGate)
			iconFileName = NOT_ICON;
		else if (cell instanceof Ledr)
			iconFileName = LEDR_ICON;
		else if (cell instanceof Ledg)
			iconFileName = LEDG_ICON;
		else if (cell instanceof HighInput)
			iconFileName = HIGH_ICON;
		else if (cell instanceof LowInput)
			iconFileName = LOW_ICON;
		else if (cell instanceof DFlip)
			iconFileName = DFLIP_ICON;
		else if (cell instanceof Switch)
			iconFileName = SWITCH_ICON;
		else if (cell instanceof PushButton)
			iconFileName = PUSH_BUTTON_ICON;
		else if (cell instanceof SevenSegment)
			iconFileName = SEVENSEG_ICON;
		else if (cell instanceof RSLatch)
			iconFileName = RSLATCH_ICON;
		else if (cell instanceof ROM)
			iconFileName = ROM_ICON;
		else if (cell instanceof RAM)
			iconFileName = RAM_ICON;
		else if (cell instanceof Nand3Gate)
			iconFileName = NAND3_ICON;
		else if (cell instanceof Nor3Gate)
			iconFileName = NOR3_ICON;
		// TODO: Add if statements to set icons for all the other types of component.
		
		// Attempt to create SVG icon
		String iconPath = ICONFOLDERPATH + iconFileName;
		SVGIcon icon = new DeltaSVGIcon(iconPath);
		try {
			URI svgURI = new URI(DeltaComponentView.class.getClassLoader().getResource(iconPath).toString());
			icon.setSvgURI(svgURI);
		} catch(Exception e) {}
		
		// Modify the view's attributes to use this icon
		GraphConstants.setIcon(this.getAttributes(),icon);
		GraphConstants.setAutoSize(this.getAttributes(),true);
	}
	
	/**
	 * Wrapper class for SVGIcon to implement custom serialization. This is necessary
	 * because the SVGIcon class from SVGSalamander doesn't support serialization.
	 * @author Group Delta 2009
	 */
	private class DeltaSVGIcon extends SVGIcon implements Serializable {

		/** Needed for correct serialization. */
		private static final long serialVersionUID = 2L;
        
        /** Relative path to icon file. */
        private String path;
		
		/** Custom serialization - only stores the path for the icon. */
		private void writeObject(ObjectOutputStream out) throws IOException {
			out.writeObject(path);
		}
		
		/** Custom serialization - read back the icon path and set default properties. */
		private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
			// Read in the stored path
            path = (String)in.readObject();
            // Set the icon
            try {
                URI svgURI = new URI(DeltaComponentView.class.getClassLoader().getResource(path).toString());
                this.setSvgURI(svgURI);
            } catch(Exception e) {
                e.printStackTrace();
            }
            // Set default properties
			this.setScaleToFit(true);
			this.setAntiAlias(true);
		}
		
		/**
		 * Creates a new SVGIcon by calling its super constructor and
		 * sets some default properties.
         * @param iconPath - the path the to the icon's SVG file.
		 */
		public DeltaSVGIcon(String iconPath){
			super();
			this.setScaleToFit(true);
			this.setAntiAlias(true);
            this.path = iconPath;
		}
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
