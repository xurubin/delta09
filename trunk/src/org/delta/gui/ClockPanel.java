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
		
		ImageIcon clock_icon = new ImageIcon ("src/org/delta/gui/diagram/images/clock.png");
		
		JLabel clock_label = new JLabel(clock_icon);
		
		setLayout (new GridLayout (1, 2) );
		
		spinner = new JSpinner ( new SpinnerNumberModel (50, 1, 100, 1) );
		
		add (clock_label);
		add (spinner);
	}
	
	public void paintComponent (Graphics g)
	{
		super.paintComponent (g);
//		g.drawImage (clock_image, 20, 20, this);
	}
}
