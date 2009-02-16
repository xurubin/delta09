package org.delta.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Locale;
import org.delta.gui.diagram.*;
import org.delta.gui.i18n.Translator;

public class MainWindow extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;
	protected CircuitPanel circuit_panel;
	protected ComponentPanel component_panel;
	protected JPanel status_panel;
	protected JPanel/*ClockPanel*/ clock_panel;
	protected JToolBar toolbar;
	protected Action undo_action, redo_action;
	
	private static MainWindow mw;

	public MainWindow()
	{
		this(Locale.getDefault());
	}
	
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
		ImageIcon stop_icon		= new ImageIcon ( "src/org/delta/gui/icons/stop.png",	 "STOP"/*translator.getString("STOP")*/   );
		
		// Create Actions to add to menus and the toolbar, and to be called from KeyBindings
		Action new_action		= new NewAction		( translator.getString("NEW"),		new_icon,		"ctrl N", 0/*translator.getMnemonic("MNEMONIC_NEW")*/);
		Action open_action		= new OpenAction	( translator.getString("OPEN"),		open_icon,		"ctrl O", 0/*translator.getMnemonic("MNEMONIC_OPEN")*/);
		Action save_action		= new SaveAction	( translator.getString("SAVE"),		save_icon,		"ctrl S", 0/*translator.getMnemonic("MNEMONIC_SAVE")*/);
		undo_action				= new UndoAction	( translator.getString("UNDO"),		undo_icon,		"ctrl Z"	);
		redo_action				= new RedoAction	( translator.getString("REDO"),		redo_icon,		"ctrl Y"	);
		Action run_action		= new RunAction		( translator.getString("RUN"),		run_icon,		"ctrl R"	);
		Action export_action	= new ExportAction	( translator.getString("EXPORT"),	export_icon,	"ctrl E"	);
		Action zoom_in_action	= new ZoomAction	( translator.getString("ZOOM_IN"),	zoom_in_icon,	"ctrl EQUALS" , 2.0);
		Action zoom_out_action	= new ZoomAction	( translator.getString("ZOOM_OUT"),	zoom_out_icon,	"ctrl MINUS"  , 0.5);
		Action cut_action		= new CutAction		( translator.getString("CUT"),		cut_icon,		"ctrl X"	);
		Action copy_action		= new CopyAction	( translator.getString("COPY"),		copy_icon,		"ctrl C"	);
		Action paste_action		= new PasteAction	( translator.getString("PASTE"),	paste_icon,		"ctrl V"	);
		Action delete_action	= new DeleteAction	( translator.getString("DELETE"),	delete_icon,	"DELETE"	);
		Action stop_action		= new StopAction	( "STOP"/*translator.getString("STOP")*/,	stop_icon,	"ctrl T"	);
		Action help_action		= new HelpAction(translator.getString("CONTENTS"), "F1");
		
		// Initialise stage of Actions
		undo_action.setEnabled(false);
		redo_action.setEnabled(false);
		
		//addKeyListener(this);
		
/*		addComponentListener ( new ComponentAdapter() {
			public void ComponentResized (ComponentEvent evt) {
			/// /*
			
				 int width = getWidth();
           int height = getHeight();
         //we check if either the width
         //or the height are below minimum
         boolean resize = false;
           if (width < 400) {
                resize = true;
                width = 400;
         }
           if (height < 300) {
                resize = true;
                height = 300;
           }
         if (resize) {
               setSize(width, height);
         }
			}
			
			/// *//*
		} );
	*/	
		circuit_panel = new CircuitPanel();
		circuit_panel.setPreferredSize ( new Dimension (620, 460) );
		
		JScrollPane sp = new JScrollPane (circuit_panel);
		cp.add (sp, BorderLayout.CENTER);
		sp.setBorder ( new javax.swing.border.LineBorder (Color.BLACK) ) ;
		sp.setPreferredSize ( new Dimension (640, 480) );
		
		component_panel = new ComponentPanel(translator);
		component_panel.setMinimumSize ( new Dimension (175, 0) );
		//component_panel.setMaximumSize ( new Dimension (175, 800));
		
		/********************************************
		
		JXCollapsiblePane cpn = new JXCollapsiblePane();
		 // JXCollapsiblePane can be used like any other container
		 cpn.setLayout(new BorderLayout());
		 // the Controls panel with a textfield to filter the tree
		 JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
		 controls.add(new JLabel("Search:"));
		 controls.add(new JTextField(10));
		 controls.add(new JButton("Refresh"));
		 controls.setBorder(new javax.swing.border.LineBorder (Color.BLACK));
		 cpn.add("Center", controls);
//		 JXFrame frame = new JXFrame();
//		 frame.setLayout(new BorderLayout());
		 // Put the "Controls" first
//		 frame.add("North", cpn);
		 // Then the tree - we assume the Controls would somehow filter the tree
//		 JScrollPane scroll = new JScrollPane(new JTree());
//		 frame.add("Center", scroll);
		 // Show/hide the "Controls"
//		 JButton toggle = new JButton(cpn.getActionMap().get(JXCollapsiblePane.TOGGLE_ACTION));
//		 toggle.setText("Show/Hide Search Panel");
//		 frame.add("South", toggle);
//		 frame.pack();
//		 frame.setVisible(true);
		 
		 ********************************************/
		
		JScrollPane sb = new JScrollPane (component_panel);
		sb.setBorder ( new javax.swing.border.LineBorder (Color.BLACK) ) ;
		sb.setPreferredSize ( new Dimension (200, 480) );
		//sb.setMinimumSize (new Dimension (200, 800) );
		sb.setMinimumSize ( new Dimension (175, 0) );
		
		
		clock_panel = new JPanel();
		clock_panel.setBorder ( new javax.swing.border.LineBorder (Color.BLACK) );
//		clock_panel.setPreferredSize ( new Dimension (200, 100) );
//		clock_panel.setMinimumSize   ( new Dimension (200, 100) );
		
		ImageIcon clock_icon = new ImageIcon ("src/org/delta/gui/diagram/images/clock.png");
		
		JLabel clock_label = new JLabel(clock_icon);
		
		clock_panel.setLayout ( new FlowLayout (FlowLayout.CENTER, 30, 10) );
		
		JSpinner spinner = new JSpinner ( new SpinnerNumberModel (50, 1, 100, 1) );
		
		clock_panel.add (clock_label);
		clock_panel.add (spinner);
		
		JSplitPane left_panel = new JSplitPane (JSplitPane.VERTICAL_SPLIT, sb, clock_panel);
		left_panel.setResizeWeight (1.0);
		cp.add (left_panel, BorderLayout.LINE_START);
		
		// Create File menu
		JMenu file_menu = new JMenu (translator.getString("FILE"));
		//file_menu.setMnemonic(translator.getMnemonic("MNEMONIC_FILE"));
		file_menu.add (new_action);
		file_menu.add (open_action);
		file_menu.add (save_action);
		JMenuItem exit_menu_item = new JMenuItem (translator.getString("EXIT"));
		file_menu.add(exit_menu_item);
		exit_menu_item.addActionListener ( new ActionListener() {
			public void actionPerformed (ActionEvent evt) {
				quitApplication();
			}
		} );
		
		// Create Edit menu
		JMenu edit_menu = new JMenu (translator.getString("EDIT"));
		//edit_menu.setMnemonic(translator.getMnemonic("MNEMONIC_EDIT"));
		edit_menu.setDisplayedMnemonicIndex(0);
		edit_menu.add (undo_action);
		edit_menu.add (redo_action);
		edit_menu.addSeparator();
		edit_menu.add (cut_action);
		edit_menu.add (copy_action);
		edit_menu.add (paste_action);
		edit_menu.add(delete_action);
		
		// Create View menu
		JMenu view_menu = new JMenu (translator.getString("VIEW"));
		//view_menu.setMnemonic(translator.getMnemonic("MNEMONIC_VIEW"));
		view_menu.add (zoom_in_action);
		view_menu.add (zoom_out_action);
		
		// Create Help menu
		JMenu help_menu = new JMenu(translator.getString("HELP"));
		//help_menu.setMnemonic(translator.getMnemonic("MNEMONIC_HELP"));
		help_menu.add (help_action);
		JMenuItem about_menu_item    = new JMenuItem (translator.getString("ABOUT"));
		help_menu.add (about_menu_item);
		
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
	
	public static MainWindow get()
	{
		if (mw == null)
			mw = new MainWindow();
		return mw;
	}

	public static void main(String args[]) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		get();
		mw.setVisible(true);
	}
}
