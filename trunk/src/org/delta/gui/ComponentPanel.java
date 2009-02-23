package org.delta.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import org.delta.gui.i18n.Translator;

public class ComponentPanel extends JPanel
{
	static final long serialVersionUID = 1;
	
	public static final int WIDTH = 200;
	
	public static final int NONE = 0;
	public static final int AND = 1;
	public static final int NAND = 2;
	public static final int OR = 3;
	public static final int NOR = 4;
	public static final int XOR = 5;
	public static final int XNOR = 6;
	public static final int NOT = 7;
	public static final int LEDR = 8;
	public static final int LEDG = 9;
	public static final int HIGH = 10;
	public static final int LOW = 11;
	public static final int DFLIP = 12;
	public static final int SWITCH = 13;
	public static final int SEVENSEG = 14;
	public static final int RSLATCH = 15;	
	public static final int ROM = 16;	
	public static final int RAM = 17;	
	public static final int NAND3 = 18;
	public static final int NOR3 = 19;
	// TODO: Set up remaining component keys
	
	private Box.Filler box;
	
	private ImageIcon collapsed_icon [], collapsible_icon [];
	
	private ArrayList <Category> cats;
	
	private TransferHandler handler;
	private MouseListener listener;
	private GridLayout component_layout;
	
	private Font font;

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
				label = new ComponentPanelLabel (icon, name, l);
				label.setTransferHandler(handler);
				label.addMouseListener(listener);
			}
		}
		
		Category (String s, int i)
		{
			label = new JLabel (s, collapsed_icon[0], JLabel.LEFT);
			label.setFont (font);
			label.setIconTextGap (10);
			label.setPreferredSize ( new Dimension (WIDTH, label.getPreferredSize().height) );
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
					label.setIcon (panel.isVisible() ? collapsible_icon [1] : collapsed_icon [1]);
					panel.repaint();
				}
				
				public void mouseEntered (MouseEvent e)
				{
					label.setIcon (panel.isVisible() ? collapsible_icon [1] : collapsed_icon [1]);
				}
				
				public void mouseExited (MouseEvent e)
				{
					label.setIcon (panel.isVisible() ? collapsible_icon [0] : collapsed_icon [0]);
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
		
		font = new Font (getFont().getName(), getFont().getStyle(), getFont().getSize() + 4);
		
		collapsed_icon   = new ImageIcon [2];
		collapsible_icon = new ImageIcon [2];
		
		collapsed_icon   [0] = new ImageIcon ("src/org/delta/gui/icons/collapsed.png");
		collapsed_icon   [1] = new ImageIcon ("src/org/delta/gui/icons/collapsed2.png");
		collapsible_icon [0] = new ImageIcon ("src/org/delta/gui/icons/collapsible.png");
		collapsible_icon [1] = new ImageIcon ("src/org/delta/gui/icons/collapsible2.png");
		
		setLayout ( new BoxLayout (this, BoxLayout.Y_AXIS) );
		
		box = new Box.Filler ( new Dimension (WIDTH, 0), getSize(), new Dimension (WIDTH, 0) );
		
		cats = new ArrayList <Category> (4);
		
		Category gates_cat   = new Category ("Logic gates", 7);
		Category leds_cat    = new Category ("LEDs",  2);
		Category input_cat   = new Category ("Inputs", 3);
		Category add_cat = new Category ("Others", 1);
		Category memory_cat  = new Category ("Memory", 2);
		
		ImageIcon and_icon  = new ImageIcon ("src/org/delta/gui/diagram/images/and.png");
		ImageIcon or_icon   = new ImageIcon ("src/org/delta/gui/diagram/images/or.png");
		ImageIcon nand_icon = new ImageIcon ("src/org/delta/gui/diagram/images/nand.png");
		ImageIcon nand3_icon = new ImageIcon ("src/org/delta/gui/diagram/images/nand3.png");
		ImageIcon nor_icon  = new ImageIcon ("src/org/delta/gui/diagram/images/nor.png");
		ImageIcon nor3_icon  = new ImageIcon ("src/org/delta/gui/diagram/images/nor3.png");
		ImageIcon xor_icon  = new ImageIcon ("src/org/delta/gui/diagram/images/xor.png");
		ImageIcon xnor_icon = new ImageIcon ("src/org/delta/gui/diagram/images/xnor.png");
		ImageIcon not_icon  = new ImageIcon ("src/org/delta/gui/diagram/images/not.png");
		
		ImageIcon ledg_icon = new ImageIcon ("src/org/delta/gui/diagram/images/ledg.png");
		ImageIcon ledr_icon = new ImageIcon ("src/org/delta/gui/diagram/images/ledr.png");
		ImageIcon sevenseg_icon = new ImageIcon ("src/org/delta/gui/diagram/images/7seg.png");
		
		ImageIcon high_icon    = new ImageIcon ("src/org/delta/gui/diagram/images/high.png");
		ImageIcon low_icon     = new ImageIcon ("src/org/delta/gui/diagram/images/low.png");
		ImageIcon switch_icon  = new ImageIcon ("src/org/delta/gui/diagram/images/switch.png");
		
		ImageIcon rslatch_icon = new ImageIcon ("src/org/delta/gui/diagram/images/rs_latch.png");
		ImageIcon dflip_icon   = new ImageIcon ("src/org/delta/gui/diagram/images/d_flip.png");
		
		ImageIcon rom_icon   = new ImageIcon ("src/org/delta/gui/diagram/images/rom.png");
		ImageIcon ram_icon   = new ImageIcon ("src/org/delta/gui/diagram/images/ram.png");
		
		gates_cat.add (and_icon,  translator.getString ("ANDGATE") , AND);
		gates_cat.add (or_icon,   translator.getString ("ORGATE"),   OR);
		gates_cat.add (nand_icon, translator.getString ("NANDGATE"), NAND);
		gates_cat.add (nand3_icon,  translator.getString ("NANDGATE") + " (3)",  NAND3);
		gates_cat.add (nor_icon,  translator.getString ("NORGATE"),  NOR);
		gates_cat.add (nor_icon,  translator.getString ("NORGATE") + " (3)",  NOR3);
		gates_cat.add (xor_icon,  translator.getString ("XORGATE"),  XOR);
		gates_cat.add (xnor_icon, translator.getString ("XNORGATE"), XNOR);
		gates_cat.add (not_icon,  translator.getString ("NOTGATE"),  NOT);
		
		leds_cat.add (ledg_icon, translator.getString ("LEDG"), LEDG);
		leds_cat.add (ledr_icon, translator.getString ("LEDR"), LEDR);
		
		input_cat.add (high_icon,   translator.getString ("HIGH"),   HIGH);
		input_cat.add (low_icon,    translator.getString ("LOW"),    LOW);
		input_cat.add (switch_icon, translator.getString ("SWITCH"), SWITCH);
		leds_cat.add (sevenseg_icon, "SEVENSEGMENT", SEVENSEG);
		
		add_cat.add (dflip_icon, translator.getString ("DFLIP"), DFLIP);
		add_cat.add (rslatch_icon, "RSLATCH", RSLATCH);
		
		memory_cat.add(rom_icon, "ROM", ROM);
		memory_cat.add(ram_icon, "RAM", RAM);
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
		
		box.setPreferredSize ( new Dimension (WIDTH, h > 0 ? h : 0) );
		if (box.getPreferredSize().height > 0)
			setSize ( getParent().getSize() );
		if ( box.getPreferredSize() != getSize() )
			box.setSize ( getPreferredSize() );
	}
	
}
