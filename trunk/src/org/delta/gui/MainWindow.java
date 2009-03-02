package org.delta.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Locale;
import java.util.Properties;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.delta.gui.diagram.CircuitPanel;
import org.delta.gui.i18n.Translator;
import org.delta.simulation.SimulationScheduler;
import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel;

public class MainWindow extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;

	protected CircuitPanel circuit_panel;
	protected ComponentPanel component_panel;
	protected ClockUpdater clock_updater;
	protected ClockLabel clock_label;
	protected JSpinner spinner;
	protected JPanel status_panel;
	protected JPanel clock_panel;
	protected JSplitPane left_panel;
	protected JToolBar toolbar;
	protected Action undo_action, redo_action, stop_action, run_action, save_action;
	protected static Properties configFile;
	protected SimulationScheduler scheduler;
	protected JCheckBoxMenuItem change_language_action;
	protected Translator translator;
	public static final String SETTINGS_FILE = "Settings.properties";
	public static final String ICON_FOLDER = "org/delta/gui/icons/";

	private static MainWindow mw;

	public MainWindow(Locale language) {
		super("Delta Circuit Simulation");
		// Create translator object
		translator = new Translator(language);

		// Set button values for JOptionPane dialog boxes
		UIManager.put("OptionPane.cancelButtonText", translator.getString("CANCEL"));
		UIManager.put("OptionPane.noButtonText", translator.getString("NO"));
		UIManager.put("OptionPane.okButtonText", translator.getString("OK"));
		UIManager.put("OptionPane.yesButtonText", translator.getString("YES"));

		Container cp = getContentPane();
		cp.setComponentOrientation(ComponentOrientation.getOrientation(language));

		mw = this;

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				quitApplication(new ActionEvent(e.getSource(), e.getID(), e.paramString()));
			}
		});

		// Create ImageIcons to use in Actions
		ClassLoader cl = this.getClass().getClassLoader();
		ImageIcon new_icon = new ImageIcon(cl.getResource(ICON_FOLDER + "new.png"), translator.getString("NEW"));
		ImageIcon open_icon = new ImageIcon(cl.getResource(ICON_FOLDER + "open.png"), translator.getString("OPEN"));
		ImageIcon save_icon = new ImageIcon(cl.getResource(ICON_FOLDER + "save.png"), translator.getString("SAVE"));
		ImageIcon undo_icon = new ImageIcon(cl.getResource(ICON_FOLDER + "undo.png"), translator.getString("UNDO"));
		ImageIcon redo_icon = new ImageIcon(cl.getResource(ICON_FOLDER + "redo.png"), translator.getString("REDO"));
		ImageIcon run_icon = new ImageIcon(cl.getResource(ICON_FOLDER + "run.png"), translator.getString("RUN"));
		ImageIcon export_icon = new ImageIcon(cl.getResource(ICON_FOLDER + "export.png"), translator
				.getString("EXPORT"));
		ImageIcon zoom_in_icon = new ImageIcon(cl.getResource(ICON_FOLDER + "zoomin.png"), translator
				.getString("ZOOM_IN"));
		ImageIcon zoom_out_icon = new ImageIcon(cl.getResource(ICON_FOLDER + "zoomout.png"), translator
				.getString("ZOOM_OUT"));
		ImageIcon cut_icon = new ImageIcon(cl.getResource(ICON_FOLDER + "cut.png"), translator.getString("CUT"));
		ImageIcon copy_icon = new ImageIcon(cl.getResource(ICON_FOLDER + "copy.png"), translator.getString("COPY"));
		ImageIcon paste_icon = new ImageIcon(cl.getResource(ICON_FOLDER + "paste.png"), translator.getString("PASTE"));
		ImageIcon delete_icon = new ImageIcon(cl.getResource(ICON_FOLDER + "delete.png"), translator
				.getString("DELETE"));
		ImageIcon stop_icon = new ImageIcon(cl.getResource(ICON_FOLDER + "stop.png"), translator.getString("STOP"));

		// Create Actions to add to menus and the toolbar, and to be called from
		// KeyBindings
		Action new_action = new NewAction(translator.getString("NEW"), new_icon, "ctrl N", translator
				.getMnemonic("MNEMONIC_NEW"));
		Action open_action = new OpenAction(translator.getString("OPEN"), open_icon, "ctrl O", translator
				.getMnemonic("MNEMONIC_OPEN"));
		save_action = new SaveAction(translator.getString("SAVE"), save_icon, "ctrl S", translator
				.getMnemonic("MNEMONIC_SAVE"));
		undo_action = new UndoAction(translator.getString("UNDO"), undo_icon, "ctrl Z", translator
				.getMnemonic("MNEMONIC_UNDO"));
		redo_action = new RedoAction(translator.getString("REDO"), redo_icon, "ctrl Y", translator
				.getMnemonic("MNEMONIC_REDO"));
		run_action = new RunAction(translator.getString("RUN"), run_icon, "ctrl R");
		Action export_action = new ExportAction(translator.getString("EXPORT"), export_icon, "ctrl E");
		Action zoom_in_action = new ZoomAction(translator.getString("ZOOM_IN"), zoom_in_icon, "ctrl EQUALS", 2.0,
				translator.getMnemonic("MNEMONIC_ZOOM_IN"));
		Action zoom_out_action = new ZoomAction(translator.getString("ZOOM_OUT"), zoom_out_icon, "ctrl MINUS", 0.5,
				translator.getMnemonic("MNEMONIC_ZOOM_OUT"));
		Action cut_action = new CutAction(translator.getString("CUT"), cut_icon, "ctrl X", translator
				.getMnemonic("MNEMONIC_CUT"));
		Action copy_action = new CopyAction(translator.getString("COPY"), copy_icon, "ctrl C", translator
				.getMnemonic("MNEMONIC_COPY"));
		Action paste_action = new PasteAction(translator.getString("PASTE"), paste_icon, "ctrl V", translator
				.getMnemonic("MNEMONIC_PASTE"));
		Action delete_action = new DeleteAction(translator.getString("DELETE"), delete_icon, "DELETE", translator
				.getMnemonic("MNEMONIC_DELETE"));
		stop_action = new StopAction(translator.getString("STOP"), stop_icon, "ctrl T");
		Action help_action = new HelpAction(translator.getString("CONTENTS"), "F1", translator
				.getMnemonic("MNEMONIC_CONTENTS"));
		Action about_action = new AboutAction(translator.getString("ABOUT"), "", translator
				.getMnemonic("MNEMONIC_ABOUT"));
		Action print_action = new PrintAction(translator.getString("PRINT"), "ctrl P", translator
				.getMnemonic("MNEMONIC_PRINT"));
		// Initialise stage of Actions
		undo_action.setEnabled(false);
		redo_action.setEnabled(false);
		stop_action.setEnabled(false);

		// Stops the window being resized too small
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent evt) {
				int width = getWidth();
				int height = getHeight();
				boolean resize = false;

				if (width < 400) {
					resize = true;
					width = 400;
				}
				if (height < 300) {
					resize = true;
					height = 300;
				}
				if (resize)
					setSize(width, height);
			}
		});

		circuit_panel = new CircuitPanel();

		JScrollPane circuit_scroll_pane = new JScrollPane(circuit_panel);
		cp.add(circuit_scroll_pane, BorderLayout.CENTER);
		circuit_scroll_pane.setBorder(new javax.swing.border.LineBorder(Color.BLACK));
		circuit_scroll_pane.setPreferredSize(new Dimension(640, 480));

		component_panel = new ComponentPanel(translator);
		component_panel.setMinimumSize(new Dimension(ComponentPanel.WIDTH, 0));

		JScrollPane component_scroll_pane = new JScrollPane(component_panel);
		component_scroll_pane.setBorder(new javax.swing.border.LineBorder(Color.BLACK));
		component_scroll_pane.setPreferredSize(new Dimension(ComponentPanel.WIDTH + 35, 480));
		component_scroll_pane.setMinimumSize(new Dimension(ComponentPanel.WIDTH, 0));
		component_scroll_pane.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				left_panel.resetToPreferredSizes();
			}
		});

		clock_panel = new JPanel();
		clock_panel.setBorder(new javax.swing.border.LineBorder(Color.BLACK));

		ImageIcon clock_icon = new ImageIcon(cl.getResource("org/delta/gui/diagram/images/" + "clock.png"));

		spinner = new JSpinner(new SpinnerNumberModel(50, 1, 500, 5));

		clock_label = new ClockLabel(clock_icon, spinner);

		clock_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));

		clock_panel.add(clock_label);
		clock_panel.add(spinner);

		left_panel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, component_scroll_pane, clock_panel);
		left_panel.setResizeWeight(1.0);
		cp.add(left_panel, BorderLayout.LINE_START);

		// Create File menu
		JMenu file_menu = new JMenu(translator.getString("FILE"));
		file_menu.setMnemonic(translator.getMnemonic("MNEMONIC_FILE"));
		file_menu.add(new_action);
		file_menu.add(open_action);
		file_menu.add(save_action);
		file_menu.add(print_action);
		JMenuItem exit_menu_item = new JMenuItem(translator.getString("EXIT"));
		exit_menu_item.setMnemonic(translator.getMnemonic("MNEMONIC_EXIT"));
		file_menu.add(exit_menu_item);
		exit_menu_item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				quitApplication(evt);
			}
		});

		// Create Edit menu
		JMenu edit_menu = new JMenu(translator.getString("EDIT"));
		edit_menu.setMnemonic(translator.getMnemonic("MNEMONIC_EDIT"));
		edit_menu.add(undo_action);
		edit_menu.add(redo_action);
		edit_menu.addSeparator();
		edit_menu.add(cut_action);
		edit_menu.add(copy_action);
		edit_menu.add(paste_action);
		edit_menu.add(delete_action);

		// Create View menu
		JMenu view_menu = new JMenu(translator.getString("VIEW"));
		view_menu.setMnemonic(translator.getMnemonic("MNEMONIC_VIEW"));
		view_menu.add(zoom_in_action);
		view_menu.add(zoom_out_action);

		// Create Help menu
		JMenu help_menu = new JMenu(translator.getString("HELP"));
		help_menu.setMnemonic(translator.getMnemonic("MNEMONIC_HELP"));
		help_menu.add(help_action);
		help_menu.add(about_action);
		change_language_action = new JCheckBoxMenuItem(translator.getString("LANG_CHANGE"));
		help_menu.add(change_language_action);

		// Create menu bar for the window and add the menus
		JMenuBar menu_bar = new JMenuBar();
		menu_bar.setComponentOrientation(ComponentOrientation.getOrientation(language));
		menu_bar.add(file_menu);
		menu_bar.add(edit_menu);
		menu_bar.add(view_menu);
		menu_bar.add(help_menu);
		setJMenuBar(menu_bar);

		// Create toolbar, add buttons to it then add it to the window
		toolbar = new JToolBar();
		toolbar.setFloatable(false);
		LayoutManager manager = new FlowLayout(FlowLayout.LEADING, 3, 1);
		toolbar.setLayout(manager);
		toolbar.setComponentOrientation(ComponentOrientation.getOrientation(language));
		toolbar.add(new_action);
		toolbar.add(open_action);
		toolbar.add(save_action);
		toolbar.addSeparator();
		toolbar.add(cut_action);
		toolbar.add(copy_action);
		toolbar.add(paste_action);
		toolbar.add(delete_action);
		toolbar.addSeparator();
		toolbar.add(undo_action);
		toolbar.add(redo_action);
		toolbar.addSeparator();
		toolbar.add(zoom_in_action);
		toolbar.add(zoom_out_action);
		toolbar.addSeparator();
		toolbar.add(run_action);
		toolbar.add(stop_action);
		toolbar.add(export_action);
		add(toolbar, BorderLayout.NORTH);

		// Pack components into the MainWindow
		pack();

	}

	private void quitApplication(ActionEvent e) {
		int choice;
		// don't ask to save if we haven't created a circuit yet.
		if (this.circuit_panel.getComponentGraph().vertexSet().size() <= 1) {
			choice = JOptionPane.NO_OPTION;
		} else {
			choice = JOptionPane.showConfirmDialog(MainWindow.get(), MainWindow.get().translator.getString("ASK_SAVE"));
		}

		switch (choice) {
		case JOptionPane.YES_OPTION:
			// save circuit
			MainWindow.get().getSaveAction().actionPerformed(
					new ActionEvent(e.getSource(), e.getID(), e.getActionCommand()));
			// deliberately no "break;"
		case JOptionPane.NO_OPTION:
			if (change_language_action.isSelected()) {
				try {
					FileWriter newConfigFile = new FileWriter(SETTINGS_FILE);
					(new Properties()).store(newConfigFile, "erasing settings");
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			System.exit(0);
		default:
		}
	}

	public Action getUndoAction() {
		return undo_action;
	}

	public Action getRedoAction() {
		return redo_action;
	}

	public Action getSaveAction() {
		return save_action;
	}

	public long getClockSpinnerValue() {
		long value = ((Integer) spinner.getValue()).longValue();
		if(value < 5) {
			value = 5;
			spinner.setValue(new Integer(5));
		}
		if(value > 500) {
			value = 500;
			spinner.setValue(new Integer(500));
		}
		return 1000 / value;
	}

	public static String getTranslatorString(String s) {
		return get().translator.getString(s);
	}

	public static MainWindow get() {
		if (mw == null)
			mw = new MainWindow(Locale.getDefault());
		return mw;
	}

	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				configFile = new Properties();
				Locale locale;
				javax.swing.JFrame.setDefaultLookAndFeelDecorated(true);
				try {
					// Other predefined skinning located at org.jvnet.substance.skin.
					UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());
					/*
					 * deals with the settings file. We show a dialog to user if
					 * they have default language selected.
					 */
					Reader fileHandle = new FileReader(SETTINGS_FILE);
					configFile.load(fileHandle);
					String localeString = (String) configFile.get("LANGUAGE");

					if (localeString == null) {
						// show dialog
						Object[] possibleValues = Translator.languageCodeMap.keySet().toArray();
						ClassLoader cl = MainWindow.class.getClassLoader();
						ImageIcon globe_icon = new ImageIcon(cl.getResource(ICON_FOLDER + "globe.png"));
						localeString = (String) JOptionPane.showInputDialog(null, "Select a language",
								"Language Choice", JOptionPane.INFORMATION_MESSAGE, globe_icon, possibleValues,
								"English");
						// return if user clicks cancel
						if (localeString == null)
							return;

						locale = new Locale(Translator.languageCodeMap.get(localeString));

						// write settings to configFile
						Writer fileWriter = new FileWriter(SETTINGS_FILE);
						configFile.setProperty("LANGUAGE", Translator.languageCodeMap.get(localeString));
						configFile.store(fileWriter, "Language Settings");
					} else {
						locale = new Locale(localeString);
					}
				} catch (Exception e) {
					locale = Locale.getDefault();
					e.printStackTrace();
				}
				final Locale curLocale = locale;

				mw = new MainWindow(curLocale);
				mw.setVisible(true);
			}
		});
	}
}
