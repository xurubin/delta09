package org.delta.gui;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;


public class NewAction extends AbstractAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NewAction(String text, ImageIcon icon, String accelerator, int mnemonic)
	{
		super(text);
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator));
		this.putValue(Action.LARGE_ICON_KEY, icon);
		this.putValue(Action.MNEMONIC_KEY, mnemonic);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		// Create a brand new circuit
		// But gives the user the option of saving the current circuit (or cancelling)
		
		int choice;
		//don't ask to save if we haven't created a circuit yet.
		if(MainWindow.get().circuit_panel.getComponentGraph().vertexSet().size() <= 1) {
			choice = JOptionPane.NO_OPTION;
		}
		else {
			choice = JOptionPane.showConfirmDialog
			( MainWindow.get(), MainWindow.get().translator.getString ("ASK_SAVE") );
		}
		
		switch (choice)
		{
			case JOptionPane.YES_OPTION:
				// save circuit
				MainWindow.get().getSaveAction().actionPerformed
					( new ActionEvent ( e.getSource(), e.getID(), e.getActionCommand() ) );
				// deliberately no "break;"
			case JOptionPane.NO_OPTION:
				MainWindow.get().circuit_panel.setGraph();
			default:
		}
	}
}