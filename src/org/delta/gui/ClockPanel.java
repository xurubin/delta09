package org.delta.gui;

import javax.swing.*;
import java.awt.*;

public class ClockPanel extends javax.swing.JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ImageLoader il;
	private Image clock_image;
	private JSpinner spinner;
	
	public ClockPanel()
	{
		il = ImageLoader.get();
		
		clock_image = il.loadImage ("clock.png");
		
		spinner = new JSpinner ( new SpinnerNumberModel (50, 1, 100, 1) );
		
		add (spinner, BorderLayout.CENTER);
	}
	
	public void paintComponent (Graphics g)
	{
		super.paintComponent (g);
		g.drawImage (clock_image, 20, 20, this);
	}
}
