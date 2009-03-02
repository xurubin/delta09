package org.delta.gui.diagram;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.delta.gui.MainWindow;

/**
 * Customised dialogue that displays a window with a 4x4 table representing the
 * contents of a ROM component. These can the be edited using comboboxes.
 * @author Group Delta 2009
 */
/*
 * TODO: Extend for RAMs as well as ROMs.
 */
public class MemoryDialog extends JDialog {
	/** Needed for correct serialization. */
	private static final long serialVersionUID = 1L;
	
    /** Array storing the contents of the table. */
	private Object[][] data;

    /**
     * Creates a MemoryDialog for the specified ROM component.
     * @param owner - window the dialogue will be displayed in.
     * @param title - title for the dialogue box.
     * @param rom - ROM component whose contents are being edited.
     */
	public MemoryDialog(Frame owner, String title, ROM rom) {
		super(owner, title);

		Container cp = getContentPane();
		cp.setLayout(new GridLayout(0, 1));

		JPanel panel3 = new JPanel();
		panel3.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));
		panel3.add(new JLabel(MainWindow.getTranslatorString("ROM_CONTENTS"), JLabel.CENTER));
		cp.add(panel3);

		JComboBox combo = new JComboBox(new Integer[] { new Integer(0), new Integer(1) });
		Object[][] tempData = new Object[4][4];
        // If the ROM has just been created, initialise the contents to 0
        if (rom.getStore().isEmpty()) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j ++) {
                    tempData[i][j] = new Integer(0);
                }
            }
        }
        // Else if the ROM already exists, use the existing values
        else {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j ++) {
                    tempData[i][j] = rom.getStore().get((j * 4) + i);
                }
            }
        }
		data = tempData;

		TableModel dataModel = new AbstractTableModel() {
			private static final long serialVersionUID = 1L;

			public int getColumnCount() {
				return 4;
			}

			public int getRowCount() {
				return 4;
			}

			public Object getValueAt(int row, int col) {
				return data[row][col];
			}

			public void setValueAt(Object value, int row, int col) {
				data[row][col] = value;
				fireTableCellUpdated(row, col);
			}

			public boolean isCellEditable(int row, int col) {
				return true;
			}
		};

		JTable table = new JTable(dataModel);

		for (int i = 0; i < 4; i++)
			table.getColumnModel().getColumn(i).setCellEditor(new DefaultCellEditor(combo));

		table.setRowSelectionAllowed(false);
		table.setColumnSelectionAllowed(false);
		table.setCellSelectionEnabled(false);

		table.setBorder(new javax.swing.border.LineBorder(Color.BLACK));
		table.setPreferredSize(new Dimension(160, table.getPreferredSize().height));

		JPanel panel = new JPanel();

		panel.setBorder(BorderFactory.createEmptyBorder(0, 25, 15, 25));
		panel.add(table);

		cp.add(panel);

		JButton button = new JButton(MainWindow.getTranslatorString("OK"));

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});

		JPanel panel2 = new JPanel();

		panel2.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));
		panel2.add(button);

		cp.add(panel2);
	}

	public int showDialog() {
		setModal(true);
		pack();
		super.setVisible(true);
		return 0;
	}

	public List<Integer> getStoreValues() {
		List<Integer> storeValues = new ArrayList<Integer>(16);

		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				storeValues.add((Integer) data[j][i]);

		return storeValues;
	}

}
