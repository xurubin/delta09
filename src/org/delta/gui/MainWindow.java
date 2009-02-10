package org.delta.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
//import java.util.*;
import org.delta.gui.diagram.*;

public class MainWindow extends javax.swing.JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected CircuitPanel circuit_panel;
	protected ComponentPanel component_panel;
	protected JPanel status_panel;
	protected ClockPanel clock_panel;
	protected JToolBar toolbar;
	protected Action undo_action,redo_action;
	
	private static MainWindow mw;
	
	public MainWindow()
	{
		super ("Delta Circuit Simulation");
		Container cp = getContentPane();
		
		mw = this;
				addWindowListener ( new WindowAdapter() {
			public void windowClosing (WindowEvent evt) {
				quitApplication();
			}
		} );
		
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
		
//		circuit_panel.setBorder ( new javax.swing.border.LineBorder (Color.BLACK) );
/*		circuit_panel.addMouseMotionListener ( new MouseMotionAdapter() {
			public void mouseDragged (MouseEvent evt) {
				mainPanelMouseDragged (evt);
			}
		} );
		circuit_panel.addMouseListener (new MouseAdapter() {
			public void mousePressed (MouseEvent evt) {
				mainPanelMousePressed (evt);
			}
			public void mouseReleased (MouseEvent evt) {
				mainPanelMouseReleased (evt);
			}
			public void mouseClicked (MouseEvent evt) {
				mainPanelMouseClicked (evt);
			}
		} );
*/		
		JScrollPane sp = new JScrollPane (circuit_panel);
		cp.add (sp, BorderLayout.CENTER);
		sp.setBorder ( new javax.swing.border.LineBorder (Color.BLACK) ) ;
		sp.setPreferredSize ( new Dimension (640, 480) );
		
//		JPanel bottom_panel = new JPanel();
//		bottom_panel.setMinimumSize ( new Dimension (840, 100) );
//		cp.add (bottom_panel, BorderLayout.SOUTH);
		
		component_panel = new ComponentPanel();
		component_panel.setPreferredSize ( new Dimension (180, 500) );
		// TODO: Fully implement drag and drop - this is just a test
		ImageIcon test = new ImageIcon ("src/org/delta/gui/diagram/images/and.png");
		JLabel label = new JLabel(test);
		TransferHandler handler = new ComponentTransferHandler();
		label.setTransferHandler(handler);
		MouseListener listener = new MouseAdapter() {
		      public void mousePressed(MouseEvent me) {
		        JComponent comp = (JComponent) me.getSource();
		        TransferHandler handler = comp.getTransferHandler();
		        handler.exportAsDrag(comp, me, TransferHandler.COPY);
		      }
		    };
		label.addMouseListener(listener);
		component_panel.add(label);
		
		JScrollPane sb = new JScrollPane (component_panel);
//		cp.add (sb, BorderLayout.WEST);
		sb.setBorder ( new javax.swing.border.LineBorder (Color.BLACK) ) ;
		sb.setPreferredSize ( new Dimension (200, 480) );
		sb.setMinimumSize (new Dimension (200, 800) );
		
		clock_panel = new ClockPanel();
		clock_panel.setBorder ( new javax.swing.border.LineBorder (Color.BLACK) );
		clock_panel.setPreferredSize ( new Dimension (200, 100) );
		clock_panel.setMinimumSize   ( new Dimension (200, 100) );
//		bottom_panel.add (clock_panel, BorderLayout.WEST);
		
//		status_panel = new JPanel();
//		status_panel.setBorder ( new javax.swing.border.LineBorder (Color.BLACK) );
//		status_panel.setPreferredSize ( new Dimension (640, 100) );
//		bottom_panel.add (status_panel, BorderLayout.CENTER);
		
		JSplitPane left_panel = new JSplitPane (JSplitPane.VERTICAL_SPLIT, sb, clock_panel);
		left_panel.setResizeWeight (1.0);
		cp.add (left_panel, BorderLayout.WEST);
		
//		Create ImageIcons to use in Actions
//		TODO: Add small versions of the icons for use in the menus.
		ImageIcon new_icon     = new ImageIcon ( "src/org/delta/gui/icons/new.png",     "New"      );
		ImageIcon open_icon    = new ImageIcon ( "src/org/delta/gui/icons/open.png",    "Load"     );
		ImageIcon save_icon    = new ImageIcon ( "src/org/delta/gui/icons/save.png",    "Save"     );
		ImageIcon undo_icon    = new ImageIcon ( "src/org/delta/gui/icons/undo.png",    "Undo"     );
		ImageIcon redo_icon    = new ImageIcon ( "src/org/delta/gui/icons/redo.png",    "Redo"     );
		ImageIcon run_icon     = new ImageIcon ( "src/org/delta/gui/icons/run.png",     "Run"      );
		ImageIcon export_icon  = new ImageIcon ( "src/org/delta/gui/icons/export.png",  "Export"   );
		ImageIcon zoomin_icon  = new ImageIcon ( "src/org/delta/gui/icons/zoomin.png",  "Zoom In"  );
		ImageIcon zoomout_icon = new ImageIcon ( "src/org/delta/gui/icons/zoomout.png", "Zoom Out" );
		ImageIcon cut_icon     = new ImageIcon ( "src/org/delta/gui/icons/cut.png",     "Cut"      );
		ImageIcon copy_icon    = new ImageIcon ( "src/org/delta/gui/icons/copy.png",    "Copy"     );
		ImageIcon paste_icon   = new ImageIcon ( "src/org/delta/gui/icons/paste.png",   "Paste"    );
		
//		Create Actions to add to menus and the toolbar
//		TODO: Use text keys for the Action labels so we can have different language packs.
		Action new_action		= new NewAction		( "New",		new_icon	);
		Action open_action		= new OpenAction	( "Open",		open_icon	);
		Action save_action		= new SaveAction	( "Save",		save_icon	);
		undo_action				= new UndoAction	( "Undo",		undo_icon	);
		redo_action				= new RedoAction	( "Redo",		redo_icon	);
		Action run_action		= new RunAction		( "Run",		run_icon	);
		Action export_action	= new ExportAction	( "Export",		export_icon	);
		Action zoomin_action	= new ZoomAction	( "Zoom In",	zoomin_icon	, 2.0);
		Action zoomout_action	= new ZoomAction	( "Zoom Out",	zoomout_icon, 0.5);
		Action cut_action		= new CutAction		( "Cut",		cut_icon	);
		Action copy_action		= new CopyAction	( "Copy",		copy_icon	);
		Action paste_action		= new PasteAction	( "Paste",		paste_icon	);
		
//		Initialise stage of Actions
		undo_action.setEnabled(false);
		redo_action.setEnabled(false);
		
//		Create File menu
		JMenu file_menu = new JMenu ("File");
		file_menu.add(new_action);
		file_menu.add(open_action);
		file_menu.add(save_action);
		JMenuItem exit_menu_item = new JMenuItem ("Exit");
		exit_menu_item.addActionListener ( new ActionListener() {
			public void actionPerformed (ActionEvent evt) {
				quitApplication();
			}
		} );
		
//		Create Edit menu
		JMenu edit_menu = new JMenu ("Edit");
		edit_menu.add(undo_action);
		edit_menu.add(redo_action);
		edit_menu.add(cut_action);
		edit_menu.add(copy_action);
		edit_menu.add(paste_action);
		
//		Create View menu
		JMenu view_menu = new JMenu ("View");
		view_menu.add(zoomin_action);
		view_menu.add(zoomout_action);
		
//		Create Help menu
		JMenu help_menu = new JMenu("Help");
		JMenuItem contents_menu_item = new JMenuItem ("Contents");
		help_menu.add(contents_menu_item);
		JMenuItem about_menu_item    = new JMenuItem ("About");
		help_menu.add(about_menu_item);
		
//		Create menu bar for the window and add the menus
		JMenuBar menu_bar = new JMenuBar();
		menu_bar.add (file_menu);
		menu_bar.add (edit_menu);
		menu_bar.add (view_menu);
		menu_bar.add (help_menu);
		setJMenuBar (menu_bar);
		
//		Create toolbar, add buttons to it then add it to the window
		toolbar = new JToolBar();
		toolbar.setFloatable (false);        
		toolbar.setPreferredSize ( new Dimension (450, 50) );
		toolbar.add ( new_action	);
		toolbar.add ( open_action	);
		toolbar.add ( save_action	);
		toolbar.add ( undo_action	);
		toolbar.add ( redo_action	);
		toolbar.add ( run_action	);
		toolbar.add ( export_action	);
		toolbar.add ( zoomin_action	);
		toolbar.add ( zoomout_action);
		toolbar.add ( cut_action	);
		toolbar.add ( copy_action	);
		toolbar.add ( paste_action	);
		add (toolbar, BorderLayout.NORTH);
		
//		Pack components into the MainWindow
		pack();

	}
/*	
	private void mainPanelMouseDragged (MouseEvent evt)
	{
		
	}
	
	private void mainPanelMousePressed (MouseEvent evt)
	{
		
	}
	
	private void mainPanelMouseReleased (MouseEvent evt)
	{
		
	}
	
	private void mainPanelMouseClicked (MouseEvent evt) 
	{
		
	}
	*/
	private void quitApplication()
	{
		System.exit (0);
	}
	
	public Action getUndoAction()
	{
		return undo_action;
	}
	
	public Action getRedoAction()
	{
		return redo_action;
	}
	
	public static MainWindow get()
	{
		if (mw == null)
			mw = new MainWindow();
		return mw;
	}
	
	public static void main ( String args[] )
	{
		mw = get();
		mw.setVisible(true);
	}
}
