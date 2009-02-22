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

	public ExportAction(String text, ImageIcon icon, String accelerator)
	{
		super(text);
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator));
		this.putValue(Action.LARGE_ICON_KEY, icon);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		// Export the circuit to a Verilog file
		try {
			JFileChooser chooser = new JFileChooser(); 
			chooser.setDialogTitle("Select Folder");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if (chooser.showSaveDialog(chooser) == JFileChooser.APPROVE_OPTION) { 
				ComponentGraph cg = MainWindow.get().circuit_panel.getComponentGraph();
				if(cg.edgeSet().size() == 0) throw new Exception();
				VerilogConverter.saveVerilogProject(chooser.getSelectedFile(), cg);
				//System.out.println(VerilogConverter.convertToVerilog(cg));
			}
		}
		catch(Exception ex) {
			JOptionPane.showMessageDialog(MainWindow.get(),
				    "Your circuit isn't fully connected.\nVerilog output requires a full circuit description.",
				    "Circuit Error",
				    JOptionPane.ERROR_MESSAGE);
		}
		
	}
}