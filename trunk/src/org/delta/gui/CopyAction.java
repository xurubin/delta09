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

	public CopyAction(String text,ImageIcon icon)
	{
		super(text,icon);
		this.action = javax.swing.TransferHandler.getCopyAction();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		e = new ActionEvent(CircuitPanelTest.panel.getGraph(),
				e.getID(),e.getActionCommand(),e.getModifiers());
		action.actionPerformed(e);
	}
}