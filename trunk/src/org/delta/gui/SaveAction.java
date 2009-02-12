package org.delta.gui;
import java.awt.event.*;
import javax.swing.*;

public class SaveAction extends AbstractAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SaveAction(String text, ImageIcon icon)
	{
		super(text,icon);
	}
	
	public SaveAction(String text) {
		super(text);
	}

	public void actionPerformed(ActionEvent e)
	{
		// Display save dialog and then save the circuit to file
	}
}