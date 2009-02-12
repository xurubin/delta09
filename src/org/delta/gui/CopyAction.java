package org.delta.gui;
import java.awt.event.*;

import javax.swing.*;


public class CopyAction extends AbstractAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Action action;

	public CopyAction(String text, ImageIcon icon, String accelerator)
	{
		super(text);
		this.action = javax.swing.TransferHandler.getCopyAction();
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator));
		this.putValue(Action.LARGE_ICON_KEY, icon);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		e = new ActionEvent(MainWindow.get().circuit_panel.getGraph(),
				e.getID(),e.getActionCommand(),e.getModifiers());
		action.actionPerformed(e);
	}
}