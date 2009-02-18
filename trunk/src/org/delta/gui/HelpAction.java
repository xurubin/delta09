package org.delta.gui;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.net.URL;

import javax.help.HelpSet;
import javax.help.JHelp;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

public class HelpAction extends AbstractAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HelpAction(String text, String accelerator, int mnemonic)
	{
		super(text);
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator));
		this.putValue(Action.MNEMONIC_KEY, mnemonic);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		// Display help
		
		String pathToHS = "/master.hs";
		URL hsURL;
		HelpSet hs = new HelpSet();
		try {
			hsURL = MainWindow.class.getResource(pathToHS);
			hs = new HelpSet(null, hsURL);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		JHelp jHelp = new JHelp(hs);
		JDialog dialog = new JDialog(MainWindow.get(), "Help", true);
		dialog.add("Center", jHelp);
		dialog.setSize(new Dimension(950, 700));
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
		
	}
}