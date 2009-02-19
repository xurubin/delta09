package org.delta.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class ComponentPanelLabel extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int componentKey;
	
	private Font f1, f2;
	
	public ComponentPanelLabel(ImageIcon icon, String name, int key) {
		super(name, icon, JLabel.CENTER);
		setHorizontalTextPosition(JLabel.CENTER);
		setVerticalTextPosition(JLabel.BOTTOM);
		componentKey = key;
		
		f1 = getFont();
		f2 = new Font ( f1.getName(), Font.BOLD, f1.getSize() );
		
		addMouseListener ( new MouseAdapter() {
			public void mouseEntered (MouseEvent e)
			{
				setFont (f2);
			}
			
			public void mouseExited (MouseEvent e)
			{
				setFont (f1);
			}
			
			public void mousePressed (MouseEvent e)
			{
				setFont (f1);
			}
		} );
	}
	
	public int getComponentKey() {
		return componentKey;
	}
}
