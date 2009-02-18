package org.delta.gui;

import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class SaveAction extends AbstractAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SaveAction (String text, ImageIcon icon, String accelerator, int mnemonic)
	{
		super (text);
		this.putValue ( Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke (accelerator) );
		this.putValue ( Action.LARGE_ICON_KEY, icon );
		this.putValue ( Action.MNEMONIC_KEY, mnemonic );
	}
	
	public void actionPerformed (ActionEvent e)
	{
		// Display save dialog and then save the circuit to file
		
		JFileChooser chooser = new JFileChooser();
		if (chooser.showSaveDialog ( MainWindow.get() ) == JFileChooser.APPROVE_OPTION)
		{
			FileOutputStream fileOut = null;
			ObjectOutputStream outr  = null;
			
			try
			{
				fileOut = new FileOutputStream ( chooser.getSelectedFile() );
				outr    = new ObjectOutputStream (fileOut);
				
				class SObject implements Serializable{
	        		private static final long serialVersionUID = 1L;
	        		private String name = "NAME";
	        	}
	        	
	        	System.out.println ("Writing Circuit...");
	            outr.writeObject ( new SObject() );//MainWindow.get().circuit_panel.getGraph() );
	        }
			catch (FileNotFoundException ex)
			{
				System.out.println(ex);
			}
			catch (IOException ex)
			{
				System.out.println(ex);
			}
			finally
			{
				System.out.println ("Closing all output streams...\n");
				try
				{
					outr.close();
					fileOut.close();
					System.out.println ("Closed all output streams...\n");
				}
				catch (IOException ex)
				{
					System.out.println(ex);
				}
			}
	    }
	}
}