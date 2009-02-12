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
	
	public ZoomAction(String text, ImageIcon icon, String accelerator, double factor)
	{
		super(text);
		scaleFactor = factor;
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator));
		this.putValue(Action.LARGE_ICON_KEY, icon);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		double scale = MainWindow.get().circuit_panel.getGraph().getScale();
		scale = scale*scaleFactor;
		MainWindow.get().circuit_panel.getGraph().setScale(scale);
	}
}