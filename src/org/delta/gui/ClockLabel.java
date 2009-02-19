package org.delta.gui;

import javax.swing.*;
import java.awt.*;

public class ClockLabel extends JLabel
{
	private static final long serialVersionUID = 1L;
	
	private JSpinner spinner;
	
	private double theta, phi;
	
	public ClockLabel (ImageIcon img, JSpinner sp)
	{
		super (img);
		
		theta = -Math.PI / 2;
		phi   = theta;
		
		spinner = sp;
	}
	
	public synchronized void paintComponent (Graphics g)
	{
		super.paintComponent (g);
		
		int x1 = getWidth()  / 2;
		int y1 = getHeight() / 2;
		int x2 = getWidth()  / 2 + (int) (Math.cos (theta) * 20);
		int y2 = getHeight() / 2 + (int) (Math.sin (theta) * 20);
		
		g.drawLine (x1, y1, x2, y2);
		g.drawLine (x1 + 1, y1, x2 + 1, y2);
		g.drawLine (x1 - 1, y1, x2 - 1, y2);
		g.drawLine (x1, y1 + 1, x2, y2 + 1);
		g.drawLine (x1, y1 - 1, x2, y2 - 1);
		
		x2 = getWidth()  / 2 + (int) (Math.cos (phi) * 14);
		y2 = getHeight() / 2 + (int) (Math.sin (phi) * 14);
		
		g.drawLine (x1, y1, x2, y2);
		g.drawLine (x1 + 1, y1, x2 + 1, y2);
		g.drawLine (x1 - 1, y1, x2 - 1, y2);
		g.drawLine (x1, y1 + 1, x2, y2 + 1);
		g.drawLine (x1, y1 - 1, x2, y2 - 1);
	}
	
	public void updateHands (long l)
	{
		int i = ( (Integer) spinner.getValue() ).intValue();
		
		theta -= i * ( ( (double) l ) / 10000  );
		phi   -= i * ( ( (double) l ) / 120000 );
	}
}
