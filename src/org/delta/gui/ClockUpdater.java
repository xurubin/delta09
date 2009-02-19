package org.delta.gui;

public class ClockUpdater extends Thread
{
	private ClockLabel clock_label;
	private long timer;
	private volatile boolean paused;
	
	public ClockUpdater (ClockLabel c)
	{
		clock_label = c;
		timer = System.currentTimeMillis();
		paused = true;
	}
	
	public void run()
	{
		while (paused == false)
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
	
	public void startClock()
	{
		paused = false;
		setPriority (Thread.MIN_PRIORITY);
		start();
	}
	
	public void stopClock()
	{
		paused = true;
	}
}
