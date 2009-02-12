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
	
	public ZoomAction(String text, ImageIcon icon, double factor)
	{
		super(text,icon);
		scaleFactor = factor;
	}
	
	public ZoomAction(String text, double factor) {
		super(text);
		scaleFactor = factor;
	}

	public void actionPerformed(ActionEvent e)
	{
		double scale = MainWindow.get().circuit_panel.getGraph().getScale();
		scale = scale*scaleFactor;
		MainWindow.get().circuit_panel.getGraph().setScale(scale);
	}
}