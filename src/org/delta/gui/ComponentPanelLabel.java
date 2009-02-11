package org.delta.gui;

import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class ComponentPanelLabel extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int componentKey;
	
	public ComponentPanelLabel(ImageIcon icon, int key) {
		super(icon);
		componentKey = key;
	}
	
	public int getComponentKey() {
		return componentKey;
	}
}
