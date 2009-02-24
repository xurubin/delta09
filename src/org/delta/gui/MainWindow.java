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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.FileReader;
import java.io.Reader;
import java.util.Locale;
import java.util.Properties;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;

import org.delta.gui.diagram.CircuitPanel;
import org.delta.gui.i18n.Translator;
import org.delta.simulation.SimulationScheduler;

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
	
	private static MainWindow mw;
	
	public MainWindow(Locale language)
	{
		super ("Delta Circuit Simulation");
		// Create translator object
		Translator translator = new Translator(language);
		Container cp = getContentPane();
		cp.setComponentOrientation(ComponentOrientation.getOrientation(language));
		
		mw = this;
		
		addWindowListener ( new WindowAdapter() {
			public void windowClosing (WindowEvent evt) {
				quitApplication();
			}
		} );
		
		// Create ImageIcons to use in Actions
		ImageIcon new_icon		= new ImageIcon ( "src/org/delta/gui/icons/new.png",     translator.getString("NEW")      );
		ImageIcon open_icon		= new ImageIcon ( "src/org/delta/gui/icons/open.png",    translator.getString("OPEN")     );
		ImageIcon save_icon		= new ImageIcon ( "src/org/delta/gui/icons/save.png",    translator.getString("SAVE")     );
		ImageIcon undo_icon		= new ImageIcon ( "src/org/delta/gui/icons/undo.png",    translator.getString("UNDO")     );
		ImageIcon redo_icon		= new ImageIcon ( "src/org/delta/gui/icons/redo.png",    translator.getString("REDO")     );
		ImageIcon run_icon		= new ImageIcon ( "src/org/delta/gui/icons/run.png",     translator.getString("RUN")      );
		ImageIcon export_icon	= new ImageIcon ( "src/org/delta/gui/icons/export.png",  translator.getString("EXPORT")   );
		ImageIcon zoom_in_icon	= new ImageIcon ( "src/org/delta/gui/icons/zoomin.png",  translator.getString("ZOOM_IN")  );
		ImageIcon zoom_out_icon	= new ImageIcon ( "src/org/delta/gui/icons/zoomout.png", translator.getString("ZOOM_OUT") );
		ImageIcon cut_icon     	= new ImageIcon ( "src/org/delta/gui/icons/cut.png",     translator.getString("CUT")      );
		ImageIcon copy_icon		= new ImageIcon ( "src/org/delta/gui/icons/copy.png",    translator.getString("COPY")     );
		ImageIcon paste_icon	= new ImageIcon ( "src/org/delta/gui/icons/paste.png",   translator.getString("PASTE")    );
		ImageIcon delete_icon	= new ImageIcon ( "src/org/delta/gui/icons/delete.png",	 translator.getString("DELETE")   );
		ImageIcon stop_icon		= new ImageIcon ( "src/org/delta/gui/icons/stop.png",	 translator.getString("STOP")	  );
		
		// Create Actions to add to menus and the toolbar, and to be called from KeyBindings
		Action new_action		= new NewAction		( translator.getString("NEW"),		new_icon,		"ctrl N", translator.getMnemonic("MNEMONIC_NEW"));
		Action open_action		= new OpenAction	( translator.getString("OPEN"),		open_icon,		"ctrl O", translator.getMnemonic("MNEMONIC_OPEN"));
		save_action				= new SaveAction	( translator.getString("SAVE"),		save_icon,		"ctrl S", translator.getMnemonic("MNEMONIC_SAVE"));
		undo_action				= new UndoAction	( translator.getString("UNDO"),		undo_icon,		"ctrl Z", translator.getMnemonic("MNEMONIC_UNDO"));
		redo_action				= new RedoAction	( translator.getString("REDO"),		redo_icon,		"ctrl Y", translator.getMnemonic("MNEMONIC_REDO"));
		run_action				= new RunAction		( translator.getString("RUN"),		run_icon,		"ctrl R"	);
		Action export_action	= new ExportAction	( translator.getString("EXPORT"),	export_icon,	"ctrl E"	);
		Action zoom_in_action	= new ZoomAction	( translator.getString("ZOOM_IN"),	zoom_in_icon,	"ctrl EQUALS" , 2.0, translator.getMnemonic("MNEMONIC_ZOOM_IN"));
		Action zoom_out_action	= new ZoomAction	( translator.getString("ZOOM_OUT"),	zoom_out_icon,	"ctrl MINUS"  , 0.5, translator.getMnemonic("MNEMONIC_ZOOM_OUT"));
		Action cut_action		= new CutAction		( translator.getString("CUT"),		cut_icon,		"ctrl X", translator.getMnemonic("MNEMONIC_CUT"));
		Action copy_action		= new CopyAction	( translator.getString("COPY"),		copy_icon,		"ctrl C",translator.getMnemonic("MNEMONIC_COPY"));
		Action paste_action		= new PasteAction	( translator.getString("PASTE"),	paste_icon,		"ctrl V",translator.getMnemonic("MNEMONIC_PASTE"));
		Action delete_action	= new DeleteAction	( translator.getString("DELETE"),	delete_icon,	"DELETE",translator.getMnemonic("MNEMONIC_DELETE"));
		stop_action				= new StopAction	( translator.getString("STOP"),		stop_icon,		"ctrl T"	);
		Action help_action		= new HelpAction	( translator.getString("CONTENTS"),		"F1",	translator.getMnemonic("MNEMONIC_CONTENTS"));
		Action about_action		= new AboutAction	( translator.getString("ABOUT"),		"",		translator.getMnemonic("MNEMONIC_ABOUT"));
		Action print_action		= new PrintAction	( translator.getString("PRINT"),		"ctrl P",		translator.getMnemonic("MNEMONIC_PRINT"));		
		// Initialise stage of Actions
		undo_action.setEnabled(false);
		redo_action.setEnabled(false);
		stop_action.setEnabled(false);
		
		// Stops the window being resized too small
		addComponentListener ( new ComponentAdapter() {
			public void componentResized (ComponentEvent evt) {
				int width  = getWidth();
				int height = getHeight();
				boolean resize = false;
				
				if (width < 400)
				{
					resize = true;
					width = 400;
				}
				if (height < 300)
				{
					resize = true;
					height = 300;
				}
				if (resize)
					setSize(width, height);
			}
		} );
		
		circuit_panel = new CircuitPanel();
		
		JScrollPane circuit_scroll_pane = new JScrollPane (circuit_panel);
		cp.add (circuit_scroll_pane, BorderLayout.CENTER);
		circuit_scroll_pane.setBorder ( new javax.swing.border.LineBorder (Color.BLACK) ) ;
		circuit_scroll_pane.setPreferredSize ( new Dimension (640, 480) );
		
		component_panel = new ComponentPanel (translator);
		component_panel.setMinimumSize ( new Dimension (ComponentPanel.WIDTH, 0) );
		
		JScrollPane component_scroll_pane = new JScrollPane (component_panel);
		component_scroll_pane.setBorder ( new javax.swing.border.LineBorder (Color.BLACK) ) ;
		component_scroll_pane.setPreferredSize ( new Dimension (ComponentPanel.WIDTH + 35, 480) );
		component_scroll_pane.setMinimumSize ( new Dimension (ComponentPanel.WIDTH, 0) );
		component_scroll_pane.addComponentListener ( new ComponentAdapter() {
			public void componentResized (ComponentEvent e) {
				left_panel.resetToPreferredSizes();
			}
		} );
		
		
		clock_panel = new JPanel();
		clock_panel.setBorder ( new javax.swing.border.LineBorder (Color.BLACK) );
		
		ImageIcon clock_icon = new ImageIcon ("src/org/delta/gui/diagram/images/clock.png");
		
		spinner = new JSpinner ( new SpinnerNumberModel (50, 1, 100, 1) );
		
		clock_label = new ClockLabel (clock_icon, spinner);
		
		clock_panel.setLayout ( new FlowLayout (FlowLayout.CENTER, 30, 10) );
		
		clock_panel.add (clock_label);
		clock_panel.add (spinner);
		
		left_panel = new JSplitPane (JSplitPane.VERTICAL_SPLIT, component_scroll_pane, clock_panel);
		left_panel.setResizeWeight (1.0);
		cp.add (left_panel, BorderLayout.LINE_START);
		
		// Create File menu
		JMenu file_menu = new JMenu (translator.getString("FILE"));
		file_menu.setMnemonic(translator.getMnemonic("MNEMONIC_FILE"));
		file_menu.add (new_action);
		file_menu.add (open_action);
		file_menu.add (save_action);
		file_menu.add (print_action);
		JMenuItem exit_menu_item = new JMenuItem (translator.getString("EXIT"));
		exit_menu_item.setMnemonic(translator.getMnemonic("MNEMONIC_EXIT"));
		file_menu.add(exit_menu_item);
		exit_menu_item.addActionListener ( new ActionListener() {
			public void actionPerformed (ActionEvent evt) {
				quitApplication();
			}
		} );
		
		// Create Edit menu
		JMenu edit_menu = new JMenu (translator.getString("EDIT"));
		edit_menu.setMnemonic(translator.getMnemonic("MNEMONIC_EDIT"));
		edit_menu.add (undo_action);
		edit_menu.add (redo_action);
		edit_menu.addSeparator();
		edit_menu.add (cut_action);
		edit_menu.add (copy_action);
		edit_menu.add (paste_action);
		edit_menu.add(delete_action);
		
		// Create View menu
		JMenu view_menu = new JMenu (translator.getString("VIEW"));
		view_menu.setMnemonic(translator.getMnemonic("MNEMONIC_VIEW"));
		view_menu.add (zoom_in_action);
		view_menu.add (zoom_out_action);
		
		// Create Help menu
		JMenu help_menu = new JMenu(translator.getString("HELP"));
		help_menu.setMnemonic(translator.getMnemonic("MNEMONIC_HELP"));
		help_menu.add (help_action);
		help_menu.add (about_action);
		
		// Create menu bar for the window and add the menus
		JMenuBar menu_bar = new JMenuBar();
		menu_bar.setComponentOrientation(ComponentOrientation.getOrientation(language));
		menu_bar.add (file_menu);
		menu_bar.add (edit_menu);
		menu_bar.add (view_menu);
		menu_bar.add (help_menu);
		setJMenuBar (menu_bar);
		
		// Create toolbar, add buttons to it then add it to the window
		toolbar = new JToolBar();
		toolbar.setFloatable (false);
		LayoutManager manager = new FlowLayout(FlowLayout.LEADING,3,1);
		toolbar.setLayout(manager);
		toolbar.setComponentOrientation(ComponentOrientation.getOrientation(language));
		toolbar.add ( new_action	);
		toolbar.add ( open_action		);
		toolbar.add ( save_action		);
		toolbar.addSeparator();
		toolbar.add ( cut_action		);
		toolbar.add ( copy_action		);
		toolbar.add ( paste_action		);
		toolbar.add ( delete_action		);
		toolbar.addSeparator();
		toolbar.add ( undo_action		);
		toolbar.add ( redo_action		);
		toolbar.addSeparator();
		toolbar.add ( zoom_in_action	);
		toolbar.add ( zoom_out_action	);
		toolbar.addSeparator();
		toolbar.add ( run_action		);
		toolbar.add ( stop_action		);
		toolbar.add ( export_action		);
		add (toolbar, BorderLayout.NORTH);
		
		// Pack components into the MainWindow
		pack();

	}

	private void quitApplication()
	{
		System.exit (0);
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
		return ((Integer) spinner.getValue()).longValue();
	}
	
	public static MainWindow get()
	{
		if (mw == null)
			mw = new MainWindow(Locale.getDefault());
		return mw;
	}

	public static void main(String args[]) {
		configFile = new Properties();
		Locale locale;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			Reader fileHandle = new FileReader("src/org/delta/gui/Settings.properties");
			configFile.load(fileHandle);
			locale = new Locale((String) configFile.get("LANGUAGE"));
		} catch (Exception e) {	
			locale = Locale.getDefault();
			e.printStackTrace();
		}
		mw = new MainWindow(locale);
		mw.setVisible(true);
	}
}
