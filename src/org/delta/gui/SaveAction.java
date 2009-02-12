package org.delta.gui;
import java.awt.event.*;
import javax.swing.*;

public class SaveAction extends AbstractAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SaveAction(String text, ImageIcon icon, String accelerator)
	{
		super(text,icon);
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator));
	}
	
	public void actionPerformed(ActionEvent e)
	{
		// Display save dialog and then save the circuit to file
	}
}