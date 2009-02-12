package org.delta.gui;
import java.awt.event.*;
import javax.swing.*;

public class NewAction extends AbstractAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NewAction(String text, ImageIcon icon, String accelerator)
	{
		super(text);
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator));
		this.putValue(Action.LARGE_ICON_KEY, icon);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		// Create a brand new circuit
		// Should check that existing circuit has been saved first
	}
}