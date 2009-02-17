package org.delta.gui;

import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class ComponentPanelLabel extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int componentKey;
	
	public ComponentPanelLabel(ImageIcon icon, String name, int key) {
		super(name, icon, JLabel.CENTER);
		setHorizontalTextPosition(JLabel.CENTER);
		setVerticalTextPosition(JLabel.BOTTOM);
		componentKey = key;
	}
	
	public int getComponentKey() {
		return componentKey;
	}
}
