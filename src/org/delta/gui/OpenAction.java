package org.delta.gui;

import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import org.jgraph.*;

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
					JGraph graph = (JGraph) ois.readObject();
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
	}
}