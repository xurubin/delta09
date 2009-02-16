package org.delta.gui;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;


public class RunAction extends AbstractAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int timesRun = 0;
	
	public RunAction(String text, ImageIcon icon, String accelerator)
	{
		super(text);
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator));
		this.putValue(Action.LARGE_ICON_KEY, icon);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(timesRun++ == 0) {
			//if this is the first time runs
			
			/*
			 * program the board with JOP
			 */
			String blaster_type = "USB-Blaster";
			String quartus_path = "jop/quartus/altde2sram/jop.sof";
			String jop_command = "quartus_pgm -c " + blaster_type + " -m JTAG -o p\\;" + quartus_path;
			try {
				Runtime.getRuntime().exec(jop_command);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			/*
			 * program the board with the daemon
			 */
			
			String jop_runtime = "./jop/upload.exe jop/DE2_Daemon.jop";
			try {
				Runtime.getRuntime().exec(jop_runtime);
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
		
		//start simulation
		
	}
}