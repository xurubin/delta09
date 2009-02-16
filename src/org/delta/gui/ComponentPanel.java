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
	
	private JPanel p1;
	private JPanel p2;
	
	private Box.Filler box;
	private JLabel label1, label2;
	
	private ImageIcon collapsed_icon, collapsible_icon;
	
	private ImageLoader il;
	
	private ArrayList <Category> cats;

	private class Category
	{
		String name;
		ArrayList <ListedComponent> comps;
		
		private class ListedComponent
		{
			String name;
			Image image;
			
			ListedComponent (String n, Image i)
			{
				name  = n;
				image = i;
			}
			
			void draw (Graphics g, int y)
			{
				g.drawImage (image, 20, y, ComponentPanel.this);
				g.drawString (name, 20, y + 60);
			}
		}
		
		private class ComponentPair extends ListedComponent
		{
			String name2;
			Image image2;
			
			ComponentPair (String n, Image i, String n2, Image i2)
			{
				super (n, i);
				name2  = n2;
				image2 = i2;
			}
			
			void draw (Graphics g, int y)
			{
				super.draw (g, y);
				g.drawImage (image2, 110, y, ComponentPanel.this);
				g.drawString (name2, 110, y + 60);
			}
		}
		
		Category (String s, int i)
		{
			name = s;
			comps = new ArrayList <ListedComponent> (i);
			ComponentPanel.this.cats.add (this);
		}
		
		void add (String imgname, String desc)
		{
			Image img = il.loadImage (imgname);
			comps.add ( new ListedComponent (desc, img) );
		}
		
		void add (String imgname, String desc, String imgname2, String desc2)
		{
			Image img  = il.loadImage (imgname);
			Image img2 = il.loadImage (imgname2);
			comps.add ( new ComponentPair (desc, img, desc2, img2) );
		}
		
		void draw (Graphics g, int y)
		{
			g.drawString (name, 20, y);
			
			int i = 0;
			
			for (ListedComponent comp : comps)
			{
				comp.draw (g, y + 20 + i++ * 70);
			}
		}
		
		int size()
		{
			return comps.size();
		}
	}
	
	public ComponentPanel(Translator translator)
	{
		il = ImageLoader.get();
		
		cats = new ArrayList <Category> (2);
		
		p1 = new JPanel();
		p2 = new JPanel();
		
		TransferHandler handler = new ComponentTransferHandler();
		MouseListener listener = new MouseAdapter() {
		      public void mousePressed(MouseEvent me) {
		        JComponent comp = (JComponent) me.getSource();
		        TransferHandler handler = comp.getTransferHandler();
		        handler.exportAsDrag(comp, me, TransferHandler.COPY);
		      }
		    };
		ImageIcon and_icon = new ImageIcon ("src/org/delta/gui/diagram/images/and.png");
		ComponentPanelLabel and_gate = new ComponentPanelLabel(and_icon,ComponentPanel.AND);
		and_gate.setTransferHandler(handler);
		and_gate.addMouseListener(listener);
		ImageIcon or_icon = new ImageIcon ("src/org/delta/gui/diagram/images/or.png");
		ComponentPanelLabel or_gate = new ComponentPanelLabel(or_icon,ComponentPanel.OR);
		or_gate.setTransferHandler(handler);
		or_gate.addMouseListener(listener);
		
		p1.setPreferredSize(new Dimension (175, 240));
		p2.setPreferredSize(new Dimension (175, 360));
		
		p1.setLayout(new GridLayout(0,2));
		p1.add (or_gate);
		p1.add (new ComponentPanelLabel(or_icon,ComponentPanel.OR));
		p1.add (new ComponentPanelLabel(or_icon,ComponentPanel.OR));
		p1.add (new ComponentPanelLabel(or_icon,ComponentPanel.OR));
		p1.add (new ComponentPanelLabel(or_icon,ComponentPanel.OR));
		p1.add (new ComponentPanelLabel(or_icon,ComponentPanel.OR));
		p1.add (new ComponentPanelLabel(or_icon,ComponentPanel.OR));
		p1.add (new ComponentPanelLabel(or_icon,ComponentPanel.OR));
		
		p2.setLayout(new GridLayout(0,2));
		p2.add (and_gate);
		p2.add (new ComponentPanelLabel(and_icon,ComponentPanel.AND));
		p2.add (new ComponentPanelLabel(and_icon,ComponentPanel.AND));
		p2.add (new ComponentPanelLabel(and_icon,ComponentPanel.AND));
		p2.add (new ComponentPanelLabel(and_icon,ComponentPanel.AND));
		p2.add (new ComponentPanelLabel(and_icon,ComponentPanel.AND));
		p2.add (new ComponentPanelLabel(and_icon,ComponentPanel.AND));
		p2.add (new ComponentPanelLabel(and_icon,ComponentPanel.AND));
		p2.add (new ComponentPanelLabel(and_icon,ComponentPanel.AND));
		p2.add (new ComponentPanelLabel(and_icon,ComponentPanel.AND));
		p2.add (new ComponentPanelLabel(and_icon,ComponentPanel.AND));
		p2.add (new ComponentPanelLabel(and_icon,ComponentPanel.AND));
		
		setLayout (new BoxLayout(this, BoxLayout.Y_AXIS));
		
		collapsed_icon   = new ImageIcon ("src/org/delta/gui/icons/collapsed.png");
		collapsible_icon = new ImageIcon ("src/org/delta/gui/icons/collapsible.png");
		
		label1 = new JLabel ("Or Gates", collapsible_icon, JLabel.LEFT);
//		label1.setBorder ( new javax.swing.border.LineBorder (Color.BLACK) );
		label1.setHorizontalTextPosition (JLabel.LEFT);
		label2 = new JLabel ("And Gates", collapsible_icon, JLabel.LEFT);
//		label1.setBorder ( new javax.swing.border.LineBorder (Color.BLACK) );
		label2.setHorizontalTextPosition (JLabel.LEFT);
		
//		p1.setBorder ( new javax.swing.border.LineBorder (Color.BLACK) );
//		p2.setBorder ( new javax.swing.border.LineBorder (Color.BLACK) );
		
		label1.addMouseListener (new MouseAdapter() {
			private boolean visible = true;
			public void mouseClicked (MouseEvent e) {
				p1.setVisible (!visible);
				resizePanel();
				visible = !visible;
				label1.setIcon(visible ? collapsible_icon : collapsed_icon);
				p1.repaint();
			}
		} );
		
		label2.addMouseListener (new MouseAdapter() {
			private boolean visible = true;
			public void mouseClicked (MouseEvent e) {
				p2.setVisible (!visible);
				resizePanel();
				visible = !visible;
				label2.setIcon(visible ? collapsible_icon : collapsed_icon);
				p2.repaint();
			}
		} );
		
/*		p1.addComponentListener(new ComponentAdapter() {
			public void componentResized (ComponentEvent e) {
				resizePanel();
			}
		} );
		
		p2.addComponentListener(new ComponentAdapter() {
			public void componentResized (ComponentEvent e) {
				resizePanel();
			}
		} );*/
		
		box = new Box.Filler(new Dimension(175,0),getSize(),new Dimension(175,0));
		
		add (label1);
		add (p1);
		add (label2);
		add (p2);
		add (box);
		
		
		/*Category nandandand = new Category ( translator.getString("NANDAND"), 2 );
		Category norandor   = new Category ( translator.getString("NOROR"),   2 );

		nandandand.add ("nand.png", translator.getString("NANDGATE"), "and.png", translator.getString("ANDGATE"));

		norandor.add ("nor.png", translator.getString("NORGATE"));
		norandor.add ("or.png", translator.getString("ORGATE"));*/
	}
	
	private void resizePanel()
	{
		int x = getParent().getHeight() - p1.getPreferredSize().height * (p1.isVisible()?1:0)
			- p2.getPreferredSize().height * (p2.isVisible()?1:0)
			- label1.getHeight() - label2.getHeight();
		box.setPreferredSize ( new Dimension (175, x > 0 ? x : 0) );
		//System.out.println(box.getHeight());
	}

	public void paintComponent (Graphics g)
	{
		super.paintComponent (g);
		
		int i = 0;
		int prevsize = 0;
		
		for (Category cat : cats)
		{
			cat.draw (g, 20 + i++ * 50 + 70 * prevsize);
			prevsize += cat.size();
		}
	}
}
