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
	
	private JButton new_button;
	private JButton open_button;
	private JButton save_button;
	private JButton undo_button;
	private JButton redo_button;
	private JButton run_button;
	private JButton export_button;
	private JButton zoomin_button;
	private JButton zoomout_button;
	private JButton cut_button;
	private JButton copy_button;
	private JButton paste_button;
	
	public DeltaToolBar()
	{
		setFloatable (false);        
		setPreferredSize ( new Dimension (450, 50) );
		
		new_icon     = new ImageIcon ( "org/delta/gui/new.png",     "New"      );
		open_icon    = new ImageIcon ( "org/delta/gui/open.png",    "Load"     );
		save_icon    = new ImageIcon ( "org/delta/gui/save.png",    "Save"     );
		undo_icon    = new ImageIcon ( "org/delta/gui/undo.png",    "Undo"     );
		redo_icon    = new ImageIcon ( "org/delta/gui/redo.png",    "Redo"     );
		run_icon     = new ImageIcon ( "org/delta/gui/run.png",     "Run"      );
		export_icon  = new ImageIcon ( "org/delta/gui/export.png",  "Export"   );
		zoomin_icon  = new ImageIcon ( "org/delta/gui/zoomin.png",  "Zoom In"  );
		zoomout_icon = new ImageIcon ( "org/delta/gui/zoomout.png", "Zoom Out" );
		cut_icon     = new ImageIcon ( "org/delta/gui/cut.png",     "Cut"      );
		copy_icon    = new ImageIcon ( "org/delta/gui/copy.png",    "Copy"     );
		paste_icon   = new ImageIcon ( "org/delta/gui/paste.png",   "Paste"    );
		
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
