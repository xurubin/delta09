package org.delta.gui.diagram;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;

public class MemoryDialog extends JDialog {

	/**
	 * 
	 */
	private List<Integer> storeValues;
	public static final int WIDTH = 500;
	public static final int HEIGHT = 300;
	
	private static final long serialVersionUID = 1L;
	
	public MemoryDialog(Frame owner, String title) {
		super(owner, title);
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(new JLabel("Please set the contents of this ROM:", JLabel.CENTER), BorderLayout.NORTH);
	}
	
	public int showDialog() {
		this.setModal(true);
		this.pack();
		super.setVisible(true);
		return 0;
	}
	
	public List<Integer> getStoreValues() {
		return storeValues;
	}
	

}
