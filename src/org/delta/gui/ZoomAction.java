package org.delta.gui;
import java.awt.event.*;
import javax.swing.*;


public class ZoomAction extends AbstractAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double scaleFactor;
	
	public ZoomAction(String text,ImageIcon icon,double factor)
	{
		super(text,icon);
		scaleFactor = factor;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		double scale = CircuitPanelTest.panel.getGraph().getScale();
		scale = scale*scaleFactor;
		CircuitPanelTest.panel.getGraph().setScale(scale);
	}
}