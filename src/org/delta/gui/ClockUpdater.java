package org.delta.gui;

public class ClockUpdater extends Thread
{
	private ClockLabel clock_label;
	private long timer;
	
	public ClockUpdater (ClockLabel c)
	{
		clock_label = c;
		timer = System.currentTimeMillis();
	}
	
	public void run()
	{
		while (true)
		{
			long t = System.currentTimeMillis();
			clock_label.updateHands (timer - t);
			clock_label.repaint();
			timer = t;
			try
			{
				Thread.sleep (20);
			}
			catch (InterruptedException e) {}
		}
	}
}
