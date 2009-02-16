package org.delta.gui;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.delta.circuit.ComponentGraph;
import org.delta.simulation.SimulationScheduler;
import org.delta.simulation.Simulator;

public class RunAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int timesRun = 0;

	public RunAction(String text, ImageIcon icon, String accelerator) {
		super(text);
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke
				.getKeyStroke(accelerator));
		this.putValue(Action.LARGE_ICON_KEY, icon);
	}

	public void actionPerformed(ActionEvent e) {
		if (timesRun++ == 0) {
			MainWindow.get().setCursor(
					Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			/*
			 * program the board with JOP
			 */
			String blaster_type = "USB-Blaster";
			String quartus_path = "jop/quartus/altde2sram/jop.sof";
			String jop_command = "quartus_pgm -c " + blaster_type
					+ " -m JTAG -o p;" + quartus_path;
			try {
				Process p = Runtime.getRuntime().exec(jop_command);

				BufferedReader input = new BufferedReader(
						new InputStreamReader(p.getInputStream()));
				String line;
				while ((line = input.readLine()) != null) {
					System.err.println(line);
				}
				input.close();
				System.err.println("programmed board");
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(MainWindow.get(),
						e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}

			/*
			 * program the board with the daemon
			 */

			String jop_runtime = "./jop/upload.exe jop/DE2_Daemon.jop";
			try {
				Process p = Runtime.getRuntime().exec(jop_runtime);

				BufferedReader input = new BufferedReader(
						new InputStreamReader(p.getInputStream()));
				String line;
				while ((line = input.readLine()) != null) {
					System.err.println(line);
				}
				input.close();
				System.err.println("loaded JOP Daemon");
			} catch (Exception e2) {
				JOptionPane.showMessageDialog(MainWindow.get(),
						e2.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
			MainWindow.get().setCursor(Cursor.getDefaultCursor());
		}
		// start simulation
		MainWindow.get().stop_action.setEnabled(true);
		MainWindow.get().run_action.setEnabled(false);
		
		/*
		ComponentGraph cg = null;
		
		Simulator s = new Simulator();
		s.setCircuit(cg.getCircuit());
		SimulationScheduler ss = new SimulationScheduler(s);
		ss.start();
		*/
		
	}
}