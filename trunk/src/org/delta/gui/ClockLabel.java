package org.delta.gui;

import javax.swing.*;
import java.awt.*;

public class ClockLabel extends JLabel
{
	private static final long serialVersionUID = 1L;
	
	private double theta;
	
	public ClockLabel (ImageIcon img)
	{
		super (img);
		theta = Math.PI / 4;
	}
	
	public void paintComponent (Graphics g)
	{
		super.paintComponent (g);
		g.drawLine ( getWidth()  / 2, getHeight() / 2,
					 getWidth()  / 2 + (int) (Math.cos(theta) * 20),
					 getHeight() / 2 + (int) (Math.sin(theta) * 20)  );
	}
}
