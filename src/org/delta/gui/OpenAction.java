package org.delta.gui;
import java.awt.event.*;
import javax.swing.*;

public class OpenAction extends AbstractAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OpenAction(String text, ImageIcon icon, String accelerator, String tooltip)
	{
		super(text);
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator));
		this.putValue(Action.LARGE_ICON_KEY, icon);
		this.putValue(Action.SHORT_DESCRIPTION, tooltip);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		// Display open file dialog then load this as the circuit
	}
}