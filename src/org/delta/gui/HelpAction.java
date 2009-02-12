package org.delta.gui;
import java.awt.event.*;
import javax.swing.*;

public class HelpAction extends AbstractAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HelpAction(String text, String accelerator)
	{
		super(text);
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator));
	}
	
	public void actionPerformed(ActionEvent e)
	{
		// Display help
	}
}