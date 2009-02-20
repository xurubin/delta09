package org.delta.gui;
import java.awt.event.*;
import javax.swing.*;

public class StopAction extends AbstractAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StopAction(String text, ImageIcon icon, String accelerator)
	{
		super(text);
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator));
		this.putValue(Action.LARGE_ICON_KEY, icon);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		// Stop simulation
		MainWindow.get().stop_action.setEnabled(false);
		MainWindow.get().run_action.setEnabled(true);
		
		MainWindow.get().scheduler.stop();
		
		MainWindow.get().clock_updater.stopClock();
	}
}