package org.delta.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DeltaToolBar extends javax.swing.JToolBar
{
	private ImageIcon new_icon;
	private ImageIcon open_icon;
	private ImageIcon save_icon;
	private ImageIcon undo_icon;
	private ImageIcon redo_icon;
	private ImageIcon run_icon;
	private ImageIcon export_icon;
	private ImageIcon zoomin_icon;
	private ImageIcon zoomout_icon;
	private ImageIcon cut_icon;
	private ImageIcon copy_icon;
	private ImageIcon paste_icon;
	
	protected JButton new_button;
	protected JButton open_button;
	protected JButton save_button;
	protected JButton undo_button;
	protected JButton redo_button;
	protected JButton run_button;
	protected JButton export_button;
	protected JButton zoomin_button;
	protected JButton zoomout_button;
	protected JButton cut_button;
	protected JButton copy_button;
	protected JButton paste_button;
	/*
	private Action new_button;
	private Action open_button;
	private Action save_button;
	private Action undo_button;
	private Action redo_button;
	private Action run_button;
	private Action export_button;
	private Action zoomin_button;
	private Action zoomout_button;
	private Action cut_button;
	private Action copy_button;
	private Action paste_button;
	*/
	public DeltaToolBar()
	{
		setFloatable (false);        
		setPreferredSize ( new Dimension (450, 50) );
		
		new_icon     = new ImageIcon ( "src/org/delta/gui/diagram/images/new.png",     "New"      );
		open_icon    = new ImageIcon ( "src/org/delta/gui/diagram/images/open.png",    "Load"     );
		save_icon    = new ImageIcon ( "src/org/delta/gui/diagram/images/save.png",    "Save"     );
		undo_icon    = new ImageIcon ( "src/org/delta/gui/diagram/images/undo.png",    "Undo"     );
		redo_icon    = new ImageIcon ( "src/org/delta/gui/diagram/images/redo.png",    "Redo"     );
		run_icon     = new ImageIcon ( "src/org/delta/gui/diagram/images/run.png",     "Run"      );
		export_icon  = new ImageIcon ( "src/org/delta/gui/diagram/images/export.png",  "Export"   );
		zoomin_icon  = new ImageIcon ( "src/org/delta/gui/diagram/images/zoomin.png",  "Zoom In"  );
		zoomout_icon = new ImageIcon ( "src/org/delta/gui/diagram/images/zoomout.png", "Zoom Out" );
		cut_icon     = new ImageIcon ( "src/org/delta/gui/diagram/images/cut.png",     "Cut"      );
		copy_icon    = new ImageIcon ( "src/org/delta/gui/diagram/images/copy.png",    "Copy"     );
		paste_icon   = new ImageIcon ( "src/org/delta/gui/diagram/images/paste.png",   "Paste"    );
		
		new_button     = new JButton ( new_icon     );
		open_button    = new JButton ( open_icon    );
		save_button    = new JButton ( save_icon    );
		undo_button    = new JButton ( undo_icon    );
		redo_button    = new JButton ( redo_icon    );
		run_button     = new JButton ( run_icon     );
		export_button  = new JButton ( export_icon  );
		zoomin_button  = new JButton ( zoomin_icon  );
		zoomout_button = new JButton ( zoomout_icon );
		cut_button     = new JButton ( cut_icon     );
		copy_button    = new JButton ( copy_icon    );
		paste_button   = new JButton ( paste_icon   );
		
/*		new_button     = new NewAction ( "New", new_icon     );
		open_button    = new OpenAction ( "Open", open_icon    );
		save_button    = new SaveAction ( "Save", save_icon    );
		undo_button    = new UndoAction ( "Undo", undo_icon    );
		redo_button    = new RedoAction ( "Redo", redo_icon    );
		//run_button     = new NewAction ( null, run_icon     );
		//export_button  = new NewAction ( null, export_icon  );
		zoomin_button  = new ZoomAction ( "Zoom In", zoomin_icon, 2  );
		zoomout_button = new ZoomAction ( "Zoom Out", zoomout_icon, 0.5 );
		cut_button     = new CutAction ( "Cut", cut_icon     );
		copy_button    = new CopyAction ( "Copy", copy_icon    );
		paste_button   = new PasteAction ( "Paste", paste_icon   );
*/
		
		new_button.setAction     ( new NewAction   (null, new_icon)   );
		open_button.setAction    ( new OpenAction  (null, open_icon)  );
		save_button.setAction    ( new SaveAction  (null, save_icon)  );
		undo_button.setAction    ( new UndoAction  (null, undo_icon)  );
		redo_button.setAction    ( new RedoAction  (null, redo_icon)  );
		zoomin_button.setAction  ( new ZoomAction  (null, zoomin_icon,  2)   );
		zoomout_button.setAction ( new ZoomAction  (null, zoomout_icon, 0.5) );
		copy_button.setAction    ( new CopyAction  (null, copy_icon)  );
		cut_button.setAction     ( new CutAction   (null, cut_icon)   );
		paste_button.setAction   ( new PasteAction (null, paste_icon) );
		
		add (new_button);
		add (open_button);
		add (save_button);
		add (undo_button);
		add (redo_button);
		add (run_button);
		add (export_button);
		add (zoomin_button);
		add (zoomout_button);
		add (cut_button);
		add (copy_button);
		add (paste_button);
	}
}
