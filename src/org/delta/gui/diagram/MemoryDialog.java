package org.delta.gui.diagram;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.delta.gui.MainWindow;

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
		cp.setLayout ( new GridLayout (0, 1) );
		cp.add ( new JLabel (MainWindow.getTranslatorString("ROM_CONTENTS"), JLabel.CENTER) );
		
		JComboBox combo = new JComboBox ( new String [] {"0","1"} );
		
//		Object [][] data = { {1,1,1,1},{0,0,0,0},{1,1,0,0},{0,0,1,1}};
//		String [] column_names = {"Your","face","is","silly"};
		
		TableModel dataModel = new AbstractTableModel() {
			private static final long serialVersionUID = 1L;
			public int getColumnCount() { return 4; }
			public int getRowCount() { return 4; }
			public Object getValueAt (int row, int col) { return new Integer (row * col); }
		};
		
		JTable table = new JTable (dataModel);
		
		for (int i = 0; i < 4; i++)
			table.getColumnModel().getColumn(i).setWidth (10);

		table.setCellEditor ( new DefaultCellEditor (combo) );
		
//		cp.add ( table.getTableHeader() );
		cp.add (table);
		
		storeValues = new ArrayList <Integer> (16);
		for (int i = 0; i < 16; i++)
			storeValues.add ( new Integer (1) );
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
