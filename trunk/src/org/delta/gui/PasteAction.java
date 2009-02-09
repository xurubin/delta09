package org.delta.gui;
import java.awt.event.*;

import javax.swing.*;


public class PasteAction extends AbstractAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Action action;

	public PasteAction(String text,ImageIcon icon)
	{
		super(text,icon);
		this.action = javax.swing.TransferHandler.getPasteAction();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		e = new ActionEvent(MainWindow.get().circuit_panel.getGraph(),
				e.getID(),e.getActionCommand(),e.getModifiers());
		action.actionPerformed(e);
	}
}