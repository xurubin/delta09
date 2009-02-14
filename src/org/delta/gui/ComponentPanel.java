package org.delta.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import org.jdesktop.swingx.*;

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
	
	private JXCollapsiblePane p1;
	private JXCollapsiblePane p2;
	
	private Box.Filler box;
	private JButton button, button2;
	private JLabel label1, label2;
	
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
		
		/*JXCollapsiblePane*/ p1 = new JXCollapsiblePane();
		Container pane1 = p1.getContentPane();
		/*JXCollapsiblePane*/ p2 = new JXCollapsiblePane();
		Container pane2 = p2.getContentPane();
		
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
		
		pane1.setPreferredSize(new Dimension (175, 240));
		pane2.setPreferredSize(new Dimension (175, 360));
		
		pane1.setLayout(new GridLayout(0,2));
		pane1.add (or_gate);
		pane1.add (new ComponentPanelLabel(or_icon,ComponentPanel.OR));
		pane1.add (new ComponentPanelLabel(or_icon,ComponentPanel.OR));
		pane1.add (new ComponentPanelLabel(or_icon,ComponentPanel.OR));
		pane1.add (new ComponentPanelLabel(or_icon,ComponentPanel.OR));
		pane1.add (new ComponentPanelLabel(or_icon,ComponentPanel.OR));
		pane1.add (new ComponentPanelLabel(or_icon,ComponentPanel.OR));
		pane1.add (new ComponentPanelLabel(or_icon,ComponentPanel.OR));
		
		pane2.setLayout(new GridLayout(0,2));
		pane2.add (and_gate);
		pane2.add (new ComponentPanelLabel(and_icon,ComponentPanel.AND));
		pane2.add (new ComponentPanelLabel(and_icon,ComponentPanel.AND));
		pane2.add (new ComponentPanelLabel(and_icon,ComponentPanel.AND));
		pane2.add (new ComponentPanelLabel(and_icon,ComponentPanel.AND));
		pane2.add (new ComponentPanelLabel(and_icon,ComponentPanel.AND));
		pane2.add (new ComponentPanelLabel(and_icon,ComponentPanel.AND));
		pane2.add (new ComponentPanelLabel(and_icon,ComponentPanel.AND));
		pane2.add (new ComponentPanelLabel(and_icon,ComponentPanel.AND));
		pane2.add (new ComponentPanelLabel(and_icon,ComponentPanel.AND));
		pane2.add (new ComponentPanelLabel(and_icon,ComponentPanel.AND));
		pane2.add (new ComponentPanelLabel(and_icon,ComponentPanel.AND));
		
		setLayout (new BoxLayout(this, BoxLayout.Y_AXIS));
		
		label1 = new JLabel ("Or Gates", JLabel.LEFT);
//		label1.setBorder ( new javax.swing.border.LineBorder (Color.BLACK) );
		label1.setHorizontalTextPosition (JLabel.LEFT);
		label2 = new JLabel ("And Gates", JLabel.LEFT);
//		label1.setBorder ( new javax.swing.border.LineBorder (Color.BLACK) );
		label2.setHorizontalTextPosition (JLabel.LEFT);
		
//		p1.setBorder ( new javax.swing.border.LineBorder (Color.BLACK) );
//		p2.setBorder ( new javax.swing.border.LineBorder (Color.BLACK) );
		
		button = new JButton ( p1.getActionMap().get (JXCollapsiblePane.TOGGLE_ACTION) );
		button2 = new JButton ( p2.getActionMap().get (JXCollapsiblePane.TOGGLE_ACTION) );
		
		button.addActionListener ( new ActionListener() {
			public void actionPerformed (ActionEvent evt) {
				System.out.println("p1: " + p1.getHeight());
				System.out.println("p2: " + p2.getHeight());
				//box.setPreferredSize(getSize());
			}
		} );
		button2.addActionListener ( new ActionListener() {
			public void actionPerformed (ActionEvent evt) {
				System.out.println("p1: " + p1.getHeight());
				System.out.println("p2: " + p2.getHeight());
				//box.setPreferredSize(new Dimension (175, getHeight() - p1.getHeight() - p2.getHeight()));
			}
		} );
		
		p1.addComponentListener(new ComponentAdapter() {
			public void componentResized (ComponentEvent e) {
				int x = getParent().getHeight() - p1.getPreferredSize().height - p2.getPreferredSize().height
						- button.getHeight() - button2.getHeight() - label1.getHeight() - label2.getHeight();
				box.setPreferredSize ( new Dimension (175, x > 0 ? x : 0) );
				System.out.println(box.getHeight());
			}
		} );
		
		p2.addComponentListener(new ComponentAdapter() {
			public void componentResized (ComponentEvent e) {
				int x = getParent().getHeight() - p1.getPreferredSize().height - p2.getPreferredSize().height
						- button.getHeight() - button2.getHeight() - label1.getHeight() - label2.getHeight();
				box.setPreferredSize ( new Dimension (175, x > 0 ? x : 0) );
				System.out.println(box.getHeight());
			}
		} );
		
//		addComponentListener(new ComponentAdapter() {
//			public void componentResized (ComponentEvent e) {
//				int x = getParent().getHeight() - p1.getHeight() - p2.getHeight()
//						- button.getHeight() - button2.getHeight() - label1.getHeight()/* - label2.getHeight()*/;
//				box.setPreferredSize ( new Dimension (175, x > 0 ? x : 0) );
//				System.out.println(box.getHeight());
//			}
//		} );
		
		p1.setAnimated(false);
		p2.setAnimated(false);
		
		box = new Box.Filler(new Dimension(175,0),getSize(),new Dimension(175,0));
		
		add (button);
		add (label1);
		add (p1);
		add (button2);
		add (label2);
		add (p2);
		add (box);
		/*add (new JLabel (and_icon));add (new JLabel (and_icon));
		add (new JLabel (and_icon));add (new JLabel (and_icon));
		add (new JLabel (and_icon));add (new JLabel (and_icon));
		add (new JLabel (and_icon));add (new JLabel (and_icon));
		add (new JLabel (and_icon));add (new JLabel (and_icon));
		add (new JLabel (and_icon));add (new JLabel (and_icon));
		add (new JLabel (and_icon));add (new JLabel (and_icon));
		add (new JLabel (and_icon));add (new JLabel (and_icon));
		add (new JLabel (and_icon));add (new JLabel (and_icon));
		add (new JLabel (and_icon));add (new JLabel (and_icon));
		add (new JLabel (and_icon));add (new JLabel (and_icon));*/
		
		/*Category nandandand = new Category ( translator.getString("NANDAND"), 2 );
		Category norandor   = new Category ( translator.getString("NOROR"),   2 );

		nandandand.add ("nand.png", translator.getString("NANDGATE"), "and.png", translator.getString("ANDGATE"));

		norandor.add ("nor.png", translator.getString("NORGATE"));
		norandor.add ("or.png", translator.getString("ORGATE"));*/
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
