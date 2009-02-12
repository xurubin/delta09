package org.delta.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Locale; //import java.util.*;
import org.delta.gui.diagram.*;
import org.delta.gui.i18n.Translator;

public class MainWindow extends javax.swing.JFrame {

	/**
	 * 
	 */

	// Create translator object
	private static final long serialVersionUID = 1L;
	protected CircuitPanel circuit_panel;
	protected ComponentPanel component_panel;
	protected JPanel status_panel;
	protected ClockPanel clock_panel;
	protected JToolBar toolbar;
	protected Action undo_toolbar_action, redo_toolbar_action, undo_action, redo_action;

	private static MainWindow mw;

	public MainWindow() {
		this(Locale.getDefault());
	}

	public MainWindow(Locale language) {
		super("Delta Circuit Simulation");
		Translator translator = new Translator(language);
		Container cp = getContentPane();

		mw = this;

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				quitApplication();
			}
		});

		/*
		 * addComponentListener ( new ComponentAdapter() { public void
		 * ComponentResized (ComponentEvent evt) { /// /
		 * 
		 * int width = getWidth(); int height = getHeight(); //we check if
		 * either the width //or the height are below minimum boolean resize =
		 * false; if (width < 400) { resize = true; width = 400; } if (height <
		 * 300) { resize = true; height = 300; } if (resize) { setSize(width,
		 * height); } }
		 * 
		 * ///
		 *//*
		 * } );
		 */
		circuit_panel = new CircuitPanel();
		circuit_panel.setPreferredSize(new Dimension(620, 460));

		JScrollPane sp = new JScrollPane(circuit_panel);
		cp.add(sp, BorderLayout.CENTER);
		sp.setBorder(new javax.swing.border.LineBorder(Color.BLACK));
		sp.setPreferredSize(new Dimension(640, 480));

		component_panel = new ComponentPanel(translator);
		component_panel.setPreferredSize(new Dimension(180, 500));
		// TODO: Fully implement drag and drop - this is just a test
		ImageIcon test = new ImageIcon(
		"src/org/delta/gui/diagram/images/and.png");
		ComponentPanelLabel label = new ComponentPanelLabel(test,
				ComponentPanel.AND);
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

		JScrollPane sb = new JScrollPane(component_panel);
		sb.setBorder(new javax.swing.border.LineBorder(Color.BLACK));
		sb.setPreferredSize(new Dimension(200, 480));
		sb.setMinimumSize(new Dimension(200, 800));

		clock_panel = new ClockPanel();
		clock_panel.setBorder(new javax.swing.border.LineBorder(Color.BLACK));
		clock_panel.setPreferredSize(new Dimension(200, 100));
		clock_panel.setMinimumSize(new Dimension(200, 100));

		JSplitPane left_panel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sb,
				clock_panel);
		left_panel.setResizeWeight(1.0);
		cp.add(left_panel, BorderLayout.WEST);

		// Create ImageIcons to use in Actions
		// TODO: Add small versions of the icons for use in the menus.
		ImageIcon new_icon = new ImageIcon("src/org/delta/gui/icons/new.png",
				translator.getString("NEW"));
		ImageIcon open_icon = new ImageIcon("src/org/delta/gui/icons/open.png",
				translator.getString("OPEN"));
		ImageIcon save_icon = new ImageIcon("src/org/delta/gui/icons/save.png",
				translator.getString("SAVE"));
		ImageIcon undo_icon = new ImageIcon("src/org/delta/gui/icons/undo.png",
				translator.getString("UNDO"));
		ImageIcon redo_icon = new ImageIcon("src/org/delta/gui/icons/redo.png",
				translator.getString("REDO"));
		ImageIcon run_icon = new ImageIcon("src/org/delta/gui/icons/run.png",
				translator.getString("RUN"));
		ImageIcon export_icon = new ImageIcon(
				"src/org/delta/gui/icons/export.png", translator
				.getString("EXPORT"));
		ImageIcon zoomin_icon = new ImageIcon(
				"src/org/delta/gui/icons/zoomin.png", translator
				.getString("ZOOM_IN"));
		ImageIcon zoomout_icon = new ImageIcon(
				"src/org/delta/gui/icons/zoomout.png", translator
				.getString("ZOOM_OUT"));
		ImageIcon cut_icon = new ImageIcon("src/org/delta/gui/icons/cut.png",
				translator.getString("CUT"));
		ImageIcon copy_icon = new ImageIcon("src/org/delta/gui/icons/copy.png",
				translator.getString("COPY"));
		ImageIcon paste_icon = new ImageIcon(
				"src/org/delta/gui/icons/paste.png", translator
				.getString("PASTE"));
		ImageIcon delete_icon = new ImageIcon(
				"src/org/delta/gui/icons/delete.png", translator
				.getString("DELETE"));

		// Create Actions to add to menus and the toolbar
		// TODO: Use text keys for the Action labels so we can have different
		// language packs.
		Action new_toolbar_action = new NewAction(translator.getString("NEW"), new_icon);
		Action open_toolbar_action = new OpenAction(translator.getString("OPEN"),
				open_icon);
		Action save_toolbar_action = new SaveAction(translator.getString("SAVE"),
				save_icon);
		
		undo_toolbar_action = new UndoAction(translator.getString("UNDO"), undo_icon);
		redo_toolbar_action = new RedoAction(translator.getString("REDO"), redo_icon);
		
		Action run_toolbar_action = new RunAction(translator.getString("RUN"), run_icon);
		Action export_toolbar_action = new ExportAction(translator.getString("EXPORT"),
				export_icon);
		Action zoomin_toolbar_action = new ZoomAction(translator.getString("ZOOM_IN"),
				zoomin_icon, 2.0);
		Action zoomout_toolbar_action = new ZoomAction(
				translator.getString("ZOOM_OUT"), zoomout_icon, 0.5);
		Action cut_toolbar_action = new CutAction(translator.getString("CUT"), cut_icon);
		Action copy_toolbar_action = new CopyAction(translator.getString("COPY"),
				copy_icon);
		Action paste_toolbar_action = new PasteAction(translator.getString("PASTE"),
				paste_icon);
		Action delete_toolbar_action = new DeleteAction(translator.getString("DELETE"), delete_icon);
		
		
		/*
		 * Add menu actions. NO ICONS. 
		 */
		Action new_action = new NewAction(translator.getString("NEW"));
		Action open_action = new OpenAction(translator.getString("OPEN"));
		Action save_action = new SaveAction(translator.getString("SAVE"));
		undo_action = new UndoAction(translator.getString("UNDO"));
		redo_action = new RedoAction(translator.getString("REDO"));
		Action run_action = new RunAction(translator.getString("RUN"));
		Action export_action = new ExportAction(translator.getString("EXPORT"));
		Action zoomin_action = new ZoomAction(translator.getString("ZOOM_IN"), 2.0);
		Action zoomout_action = new ZoomAction(translator.getString("ZOOM_OUT"), 0.5);
		Action cut_action = new CutAction(translator.getString("CUT"));
		Action copy_action = new CopyAction(translator.getString("COPY"));
		Action paste_action = new PasteAction(translator.getString("PASTE"));
		Action delete_action = new DeleteAction(translator.getString("DELETE"));
		
		/*
		 * Adding keyboard shortcuts
		 */
		

		// Initialise stage of Actions
		undo_toolbar_action.setEnabled(false);
		undo_action.setEnabled(false);
		redo_toolbar_action.setEnabled(false);
		redo_action.setEnabled(false);

		// Create File menu
		JMenu file_menu = new JMenu(translator.getString("FILE"));
		JMenuItem new_action_menu = new JMenuItem(new_action);
		new_action_menu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
			    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		file_menu.add(new_action_menu);
		
		JMenuItem open_action_menu = new JMenuItem(open_action);
		open_action_menu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
			    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		file_menu.add(open_action_menu);
		
		JMenuItem save_action_menu = new JMenuItem(save_action);
		save_action_menu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
			    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		file_menu.add(save_action_menu);
		
		file_menu.addSeparator();
		
		JMenuItem run_action_menu = new JMenuItem(run_action);
		run_action_menu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
			    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		file_menu.add(run_action_menu);
		
		JMenuItem export_action_menu = new JMenuItem(export_action);
		export_action_menu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
			    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		file_menu.add(export_action_menu);
		
		JMenuItem exit_menu_item = new JMenuItem(translator.getString("EXIT"));
		exit_menu_item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
			    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		file_menu.add(exit_menu_item);
		
		exit_menu_item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				quitApplication();
			}
		});

		// Create Edit menu
		JMenu edit_menu = new JMenu(translator.getString("EDIT"));
		
		JMenuItem undo_action_menu = new JMenuItem(undo_action);
		undo_action_menu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
			    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		edit_menu.add(undo_action_menu);
		
		JMenuItem redo_action_menu = new JMenuItem(redo_action);
		redo_action_menu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
			    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | Event.SHIFT_MASK));
		edit_menu.add(redo_action_menu);
		edit_menu.addSeparator();
		
		
		JMenuItem cut_action_menu = new JMenuItem(cut_action);
		cut_action_menu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
			    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		edit_menu.add(cut_action_menu);
		
		JMenuItem copy_action_menu = new JMenuItem(copy_action);
		copy_action_menu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
			    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		edit_menu.add(copy_action_menu);
		
		JMenuItem paste_action_menu = new JMenuItem(paste_action);
		paste_action_menu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
			    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		edit_menu.add(paste_action_menu);
		
		JMenuItem delete_action_menu = new JMenuItem(delete_action);
		delete_action_menu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,
			    0));
		edit_menu.add(delete_action_menu);

		// Create View menu
		JMenu view_menu = new JMenu(translator.getString("VIEW"));
		JMenuItem zoomin_action_menu = new JMenuItem(zoomin_action);
		zoomin_action_menu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS,
			    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		view_menu.add(zoomin_action_menu);
		
		JMenuItem zoomout_action_menu = new JMenuItem(zoomout_action);
		zoomout_action_menu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS,
			    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		view_menu.add(zoomout_action_menu);

		// Create Help menu
		JMenu help_menu = new JMenu(translator.getString("HELP"));
		JMenuItem contents_menu_item = new JMenuItem(translator.getString("CONTENTS"));
		contents_menu_item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,0));
		help_menu.add(contents_menu_item);
		
		JMenuItem about_menu_item = new JMenuItem(translator.getString("ABOUT"));
		help_menu.add(about_menu_item);

		// Create menu bar for the window and add the menus
		JMenuBar menu_bar = new JMenuBar();
		menu_bar.add(file_menu);
		menu_bar.add(edit_menu);
		menu_bar.add(view_menu);
		menu_bar.add(help_menu);
		setJMenuBar(menu_bar);

		// Create toolbar, add buttons to it then add it to the window
		toolbar = new JToolBar();
		toolbar.setFloatable(false);
		toolbar.setRollover(true);
		LayoutManager l = new FlowLayout(FlowLayout.CENTER, 5, 1);
		toolbar.setLayout(l);
		// toolbar.setPreferredSize ( new Dimension (450, 40) );
		toolbar.add(new_toolbar_action);
		toolbar.add(open_toolbar_action);
		toolbar.add(save_toolbar_action);
		toolbar.addSeparator();
		toolbar.add(cut_toolbar_action);
		toolbar.add(copy_toolbar_action);
		toolbar.add(paste_toolbar_action);
		toolbar.add(delete_toolbar_action);
		toolbar.addSeparator();
		toolbar.add(undo_toolbar_action);
		toolbar.add(redo_toolbar_action);
		toolbar.addSeparator();
		toolbar.add(zoomin_toolbar_action);
		toolbar.add(zoomout_toolbar_action);
		toolbar.addSeparator();
		toolbar.add(run_toolbar_action);
		toolbar.add(export_toolbar_action);
		add(toolbar, BorderLayout.NORTH);

		// Pack components into the MainWindow
		pack();

	}

	private void quitApplication() {
		System.exit(0);
	}

	public Action[] getUndoActions() {
		return new Action[] {undo_action, undo_toolbar_action};
	}

	public Action[] getRedoActions() {
		return new Action[] {redo_action, redo_toolbar_action};
	}

	public static MainWindow get() {
		if (mw == null)
			mw = new MainWindow();
		return mw;
	}

	public static void main(String args[]) {
		mw = get();
		mw.setVisible(true);
	}
}
