package org.delta.gui;

import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import org.delta.gui.diagram.DeltaGraph;

public class OpenAction extends AbstractAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OpenAction(String text, ImageIcon icon, String accelerator, int mnemonic)
	{
		super(text);
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator));
		this.putValue(Action.LARGE_ICON_KEY, icon);
		this.putValue(Action.MNEMONIC_KEY, mnemonic);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		// Checks whether the user wants to save the current circuit (or cancel)
		int choice = JOptionPane.showConfirmDialog (MainWindow.get(), "Save current circuit first?");
		
		switch (choice)
		{
			case JOptionPane.YES_OPTION:
				// save circuit
				MainWindow.get().getSaveAction().actionPerformed
					( new ActionEvent ( e.getSource(), e.getID(), e.getActionCommand() ) );
				// deliberately no "break;"
			case JOptionPane.NO_OPTION:
				// Display open file dialog then load this as the circuit
				JFileChooser chooser = new JFileChooser();
				if (chooser.showOpenDialog ( MainWindow.get() ) == JFileChooser.APPROVE_OPTION)
				{
					
					try
					{
						FileInputStream fis   = new FileInputStream ( chooser.getSelectedFile() );
						ObjectInputStream ois = new ObjectInputStream (fis);
						
						try
						{
							DeltaGraph graph = (DeltaGraph) ois.readObject();
							MainWindow.get().circuit_panel.setGraph (graph);
						}
						catch (ClassNotFoundException ex)
						{
							System.out.println(ex);
						}
						
						ois.close();
					}
					catch (FileNotFoundException ex)
					{
						System.out.println(ex);
					}
					catch (IOException ex)
					{
						System.out.println(ex);
					}
				}
			default:
		}
	}
}