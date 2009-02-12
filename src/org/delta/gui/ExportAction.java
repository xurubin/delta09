package org.delta.gui;
import java.awt.event.*;
import javax.swing.*;

import org.delta.circuit.ComponentGraph;
import org.delta.verilog.VerilogConverter;

public class ExportAction extends AbstractAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExportAction(String text, ImageIcon icon)
	{
		super(text,icon);
	}
	
	public ExportAction(String text) {
		super(text);
	}

	public void actionPerformed(ActionEvent e)
	{
		// Export the circuit to a Verilog file
		JFileChooser chooser = new JFileChooser(); 
		chooser.setDialogTitle("Select Folder");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (chooser.showSaveDialog(chooser) == JFileChooser.APPROVE_OPTION) { 
			/*
			 * TODO: Get ComponentGraph object from MainWindow. 
			 */
			ComponentGraph cg = null;
			VerilogConverter.saveVerilogProject(chooser.getSelectedFile(), cg);
		}
	}
}