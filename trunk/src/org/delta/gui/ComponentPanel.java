package org.delta.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import org.delta.gui.i18n.Translator;

public class ComponentPanel extends JPanel
{
	static final long serialVersionUID = 1;
	
	public static final int NONE = 0;
	public static final int AND = 1;
	public static final int NAND = 2;
	public static final int OR = 3;
	public static final int NOR = 4;
	// TODO: Set up remaining component keys
	
	private Box.Filler box;
	
	private ImageIcon collapsed_icon, collapsible_icon;
	
	private ArrayList <Category> cats;
	
	private TransferHandler handler;
	private MouseListener listener;
	private GridLayout component_layout;

	private class Category
	{
		JLabel label;
		JPanel lpanel;
		JPanel panel;
		ArrayList <ListedComponent> comps;
		
		private class ListedComponent
		{
			String name;
			ImageIcon icon;
			ComponentPanelLabel label;
			
			ListedComponent (String n, ImageIcon i, int l)
			{
				name = n;
				icon = i;
				label = new ComponentPanelLabel (icon, n, l);
				label.setTransferHandler(handler);
				label.addMouseListener(listener);
			}
		}
		
		Category (String s, int i)
		{
			label = new JLabel (s, collapsed_icon, JLabel.LEFT);
			label.setPreferredSize ( new Dimension (175, label.getPreferredSize().height) );
			label.setHorizontalTextPosition (JLabel.RIGHT);
			label.setVerticalAlignment (JLabel.CENTER);
			
			lpanel = new JPanel();
			lpanel.add (label);
			
			panel = new JPanel();
			panel.setLayout (component_layout);
			panel.setVisible (false);
			
			comps = new ArrayList <ListedComponent> (i);
			ComponentPanel.this.cats.add (this);
			
			label.addMouseListener ( new MouseAdapter() {
				public void mouseClicked (MouseEvent e) {
					panel.setVisible ( !panel.isVisible() );
					resizePanel();
					label.setIcon (panel.isVisible() ? collapsible_icon : collapsed_icon);
					panel.repaint();
				}
			} );
		}
		
		void add (ImageIcon img, String desc, int l)
		{
			ListedComponent lc = new ListedComponent (desc, img, l);
			comps.add (lc);
			panel.add (lc.label);
		}
		
		int size()
		{
			return comps.size();
		}
	}
	
	public ComponentPanel (Translator translator)
	{
		addComponentListener ( new ComponentAdapter() {
			public void componentResized (ComponentEvent e) {
				resizePanel();
			}
		} );
		
		component_layout = new GridLayout(0,2);
		component_layout.setVgap(15);
		
		handler  = new ComponentTransferHandler();
		listener = new MouseAdapter() {
			public void mousePressed (MouseEvent me) {
				JComponent comp = (JComponent) me.getSource();
				TransferHandler handler = comp.getTransferHandler();
				handler.exportAsDrag (comp, me, TransferHandler.COPY);
			}
		};
		
		collapsed_icon   = new ImageIcon ("src/org/delta/gui/icons/collapsed.png");
		collapsible_icon = new ImageIcon ("src/org/delta/gui/icons/collapsible.png");
		
		setLayout ( new BoxLayout (this, BoxLayout.Y_AXIS) );
		
		box = new Box.Filler ( new Dimension (175,0), getSize(), new Dimension(175,0) );
		
		cats = new ArrayList <Category> (2);
		
		Category and_cat = new Category ("And gates", 1);
		Category or_cat  = new Category ("Or gates",  1);
		
		ImageIcon and_icon = new ImageIcon ("src/org/delta/gui/diagram/images/and.png");
		ImageIcon or_icon  = new ImageIcon ("src/org/delta/gui/diagram/images/or.png");
		
		and_cat.add (and_icon, translator.getString("ANDGATE"), ComponentPanel.AND);
		and_cat.add (and_icon, translator.getString("ANDGATE"), ComponentPanel.AND);
		and_cat.add (and_icon, translator.getString("ANDGATE"), ComponentPanel.AND);
		and_cat.add (and_icon, translator.getString("ANDGATE"), ComponentPanel.AND);
		and_cat.add (and_icon, translator.getString("ANDGATE"), ComponentPanel.AND);
		or_cat.add  (or_icon,  translator.getString("ORGATE"),  ComponentPanel.OR);
		or_cat.add  (or_icon,  translator.getString("ORGATE"),  ComponentPanel.OR);
		or_cat.add  (or_icon,  translator.getString("ORGATE"),  ComponentPanel.OR);
		or_cat.add  (or_icon,  translator.getString("ORGATE"),  ComponentPanel.OR);
		or_cat.add  (or_icon,  translator.getString("ORGATE"),  ComponentPanel.OR);
		or_cat.add  (or_icon,  translator.getString("ORGATE"),  ComponentPanel.OR);
		or_cat.add  (or_icon,  translator.getString("ORGATE"),  ComponentPanel.OR);
		
		for (Category c : cats)
		{
			add (c.lpanel);
			add (c.panel);
		}
		
		add (box);
	}
	
	void resizePanel()
	{
		int h = getParent().getHeight();
		
		for (Category c : cats)
			h -= c.panel.getPreferredSize().height * (c.panel.isVisible() ? 1 : 0) + c.lpanel.getPreferredSize().height;
		
		box.setPreferredSize ( new Dimension (175, h > 0 ? h : 0) );
		if (box.getPreferredSize().height > 0)
			setSize ( getParent().getSize() );
		if ( box.getPreferredSize() != getSize() )
			box.setSize ( getPreferredSize() );
		
		//System.out.println ( getSize().height + ", " + box.getPreferredSize().height + ", "
		//				     + box.getSize().height + ", " + getParent().getHeight() );
	}
	
}
