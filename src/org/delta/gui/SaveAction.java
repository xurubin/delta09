package org.delta.gui;
import java.awt.event.*;
import javax.swing.*;

public class SaveAction extends AbstractAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SaveAction(String text, ImageIcon icon, String accelerator,int mnemonic)
	{
		super(text);
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator));
		this.putValue(Action.LARGE_ICON_KEY, icon);
		this.putValue(Action.MNEMONIC_KEY, mnemonic);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		// Display save dialog and then save the circuit to file
	}
}