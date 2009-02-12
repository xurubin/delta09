package org.delta.gui;
import java.awt.event.*;
import javax.swing.*;


public class CutAction extends AbstractAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Action action;

	public CutAction(String text, ImageIcon icon)
	{
		super(text,icon);
		this.action = javax.swing.TransferHandler.getCutAction();
	}
	
	public CutAction(String text) {
		super(text);
		this.action = javax.swing.TransferHandler.getCutAction();
	}

	public void actionPerformed(ActionEvent e)
	{
		e = new ActionEvent(MainWindow.get().circuit_panel.getGraph(),
				e.getID(),e.getActionCommand(),e.getModifiers());
		action.actionPerformed(e);
	}
}