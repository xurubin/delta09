package org.delta.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import org.delta.gui.diagram.*;

public class MainWindow extends javax.swing.JFrame {
	
	protected CircuitPanel circuit_panel;
	protected ComponentPanel component_panel;
	protected JPanel status_panel;
	protected ClockPanel clock_panel;
	protected DeltaToolBar toolbar;
	
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
		
		JMenuBar menu_bar = new JMenuBar();
		JMenu file_menu = new JMenu ("File");
		JMenuItem new_menu_item  = new JMenuItem ("New");
		JMenuItem open_menu_item = new JMenuItem ("Open");
		JMenuItem save_menu_item = new JMenuItem ("Save");
		JMenuItem exit_menu_item = new JMenuItem ("Exit");
		exit_menu_item.addActionListener ( new ActionListener() {
			public void actionPerformed (ActionEvent evt) {
				quitApplication();
			}
		} );

		JMenu edit_menu = new JMenu ("Edit");
		JMenuItem undo_menu_item   = new JMenuItem ("Undo");
		JMenuItem redo_menu_item   = new JMenuItem ("Redo");
		JMenuItem cut_menu_item    = new JMenuItem ("Cut");
		JMenuItem copy_menu_item   = new JMenuItem ("Copy");
		JMenuItem paste_menu_item  = new JMenuItem ("Paste");
		JMenuItem delete_menu_item = new JMenuItem ("Delete");

		JMenu view_menu = new JMenu ("View");
		JMenuItem zoomin_menu_item  = new JMenuItem ("Zoom in");
		JMenuItem zoomout_menu_item = new JMenuItem ("Zoom out");

		JMenu help_menu = new JMenu("Help");
		JMenuItem contents_menu_item = new JMenuItem ("Contents");
		JMenuItem about_menu_item    = new JMenuItem ("About");
		
		file_menu.add (new_menu_item);
		file_menu.add (open_menu_item);
		file_menu.add (save_menu_item);
		file_menu.add (exit_menu_item);

		edit_menu.add (undo_menu_item);
		edit_menu.add (redo_menu_item);
		edit_menu.add (cut_menu_item);
		edit_menu.add (copy_menu_item);
		edit_menu.add (paste_menu_item);
		edit_menu.add (delete_menu_item);
		
		view_menu.add (zoomin_menu_item);
		view_menu.add (zoomout_menu_item);
		
		help_menu.add (contents_menu_item);
		help_menu.add (about_menu_item);
		
		menu_bar.add (file_menu);
		menu_bar.add (edit_menu);
		menu_bar.add (view_menu);
		menu_bar.add (help_menu);
		
		setJMenuBar (menu_bar);
		
		toolbar = new DeltaToolBar();
		
		add (toolbar, BorderLayout.PAGE_START);
		
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
	
	public static MainWindow get()
	{
		if (mw == null)
			mw = new MainWindow();
		return mw;
	}
	
	public static void main ( String args[] )
	{
		mw = get();
		mw.show();
	}
}
