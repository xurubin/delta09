package org.delta.gui;

import org.delta.gui.diagram.CircuitPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.*;

public class CircuitPanelTest
{
	protected static CircuitPanel panel;
	protected static Action zoomIn,zoomOut,delete,cut,copy,paste,undo,redo;
	
	public static void main(String[] args)
	{
		JToolBar toolbar = new JToolBar();
		zoomIn = new ZoomAction("Zoom In",null,2);
		toolbar.add(zoomIn);
		zoomOut = new ZoomAction("Zoom Out",null,0.5);
		toolbar.add(zoomOut);
		delete = new DeleteAction("Delete",null);
		toolbar.add(delete);
		cut = new CutAction("Cut",null);
		toolbar.add(cut);
		copy = new CopyAction("Copy",null);
		toolbar.add(copy);
		paste = new PasteAction("Paste",null);
		toolbar.add(paste);
		undo = new UndoAction("Undo",null);
		toolbar.add(undo);
		redo = new RedoAction("Redo",null);
		toolbar.add(redo);
		
		panel = new CircuitPanel();
		
		JFrame frame = new JFrame("Circuit Panel Test");
		frame.setPreferredSize(new Dimension(600,400));
		frame.getContentPane().add(toolbar,BorderLayout.NORTH);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static Action getUndo()
	{
		return undo;
	}
	
	public static Action getRedo()
	{
		return redo;
	}
}
