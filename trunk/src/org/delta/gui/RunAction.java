package org.delta.gui;
import java.awt.event.*;
import javax.swing.*;

public class RunAction extends AbstractAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RunAction(String text, ImageIcon icon, String accelerator)
	{
		super(text,icon);
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator));
	}
	
	public void actionPerformed(ActionEvent e)
	{
		// Simulate the circuit on the board
	}
}