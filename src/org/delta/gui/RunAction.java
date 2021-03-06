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
import org.delta.transport.BoardInterface;

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
		if(!System.getProperty("os.name").contains("Windows")) {
			JOptionPane.showMessageDialog(MainWindow.get(), MainWindow.get().translator.getString("WINDOWS_REQUIRED_LONG"), MainWindow.get().translator.getString("WINDOWS_REQUIRED"), JOptionPane.ERROR_MESSAGE);
			return;
		}
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
					if(line.contains("Error")) {
						throw new Exception(MainWindow.get().translator.getString("TRANSFER_ERROR"));
					}
				}
				input.close();
				System.err.println("programmed board");
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(MainWindow.get(),
						e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				MainWindow.get().setCursor(Cursor.getDefaultCursor());
				timesRun = 0;
				return;
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
						e2.getMessage(), MainWindow.get().translator.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
				MainWindow.get().setCursor(Cursor.getDefaultCursor());
				timesRun = 0;
				return;
			}
			MainWindow.get().setCursor(Cursor.getDefaultCursor());
		}
		// start simulation
		MainWindow.get().stop_action.setEnabled(true);
		MainWindow.get().run_action.setEnabled(false);
		
		
		ComponentGraph cg = MainWindow.get().circuit_panel.getComponentGraph();
				
		// Set clock frequency from spinner.
		cg.setClockFrequency((int) MainWindow.get().getClockSpinnerValue());
		
	    Simulator s = new Simulator();
		s.setCircuit(cg.getCircuit());
		
		//erase old LED states
		BoardInterface.getInstance().reset();
		
		
		MainWindow.get().scheduler = new SimulationScheduler(s);
		MainWindow.get().scheduler.start();
		MainWindow.get().clock_updater = new ClockUpdater (MainWindow.get().clock_label);
		MainWindow.get().clock_updater.startClock();
		MainWindow.get().spinner.setEnabled(false);
	}
}