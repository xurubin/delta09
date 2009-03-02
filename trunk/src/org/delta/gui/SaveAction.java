package org.delta.gui;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

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
	    chooser.setFileFilter ( new CircuitFileFilter() );
	    chooser.setAcceptAllFileFilterUsed (false);
		
		if (chooser.showSaveDialog ( MainWindow.get() ) == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				File f = chooser.getSelectedFile();
				
				f = f.getName().toLowerCase().endsWith (".cir") ? f : new File (f.getPath() + ".cir");
				
				FileOutputStream fileOut = new FileOutputStream (f);
				ObjectOutputStream outr  = new ObjectOutputStream (fileOut);

	            outr.writeObject ( MainWindow.get().circuit_panel.getGraph() );
	            
	            outr.close();
				fileOut.close();
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