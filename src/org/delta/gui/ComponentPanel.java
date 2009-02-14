package org.delta.gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
		
		JXCollapsiblePane pane1 = new JXCollapsiblePane();
		pane1.setBorder (new javax.swing.border.LineBorder (Color.BLACK));
		JXCollapsiblePane pane2 = new JXCollapsiblePane();
		//pane2.setBorder (new javax.swing.border.LineBorder (Color.BLACK));
		
		TransferHandler handler = new ComponentTransferHandler();
		MouseListener listener = new MouseAdapter() {
		      public void mousePressed(MouseEvent me) {
		        JComponent comp = (JComponent) me.getSource();
		        TransferHandler handler = comp.getTransferHandler();
		        handler.exportAsDrag(comp, me, TransferHandler.COPY);
		      }
		    };
		ImageIcon test = new ImageIcon ("src/org/delta/gui/diagram/images/and.png");
		ComponentPanelLabel and_gate = new ComponentPanelLabel(test,ComponentPanel.AND);
		ComponentPanelLabel and_gate2 = new ComponentPanelLabel(test,ComponentPanel.AND);
		and_gate.setTransferHandler(handler);
		and_gate.addMouseListener(listener);
		and_gate2.setTransferHandler(handler);
		and_gate2.addMouseListener(listener);
		ImageIcon test2 = new ImageIcon ("src/org/delta/gui/diagram/images/or.png");
		ComponentPanelLabel or_gate = new ComponentPanelLabel(test2,ComponentPanel.OR);
		ComponentPanelLabel or_gate2 = new ComponentPanelLabel(test2,ComponentPanel.OR);
		or_gate.setTransferHandler(handler);
		or_gate.addMouseListener(listener);
		or_gate2.setTransferHandler(handler);
		or_gate2.addMouseListener(listener);
		
		pane1.setPreferredSize(new Dimension (175, 250));
		pane2.setPreferredSize(new Dimension (175, 350));
		
		pane1.getContentPane().setLayout(new GridLayout(0,2));
		pane1.getContentPane().add (and_gate);
		pane1.getContentPane().add (new ComponentPanelLabel(test,ComponentPanel.AND));
		pane1.getContentPane().add (new ComponentPanelLabel(test,ComponentPanel.AND));
		pane1.getContentPane().add (new ComponentPanelLabel(test,ComponentPanel.AND));
		pane1.getContentPane().add (new ComponentPanelLabel(test,ComponentPanel.AND));
		pane1.getContentPane().add (or_gate);
		pane1.getContentPane().add (and_gate2);
		
		pane2.getContentPane().setLayout(new GridLayout(0,2));
		pane2.getContentPane().add (new ComponentPanelLabel(test,ComponentPanel.AND));
		pane2.getContentPane().add (new ComponentPanelLabel(test,ComponentPanel.AND));
		pane2.getContentPane().add (new ComponentPanelLabel(test,ComponentPanel.AND));
		pane2.getContentPane().add (new ComponentPanelLabel(test,ComponentPanel.AND));
		pane2.getContentPane().add (new ComponentPanelLabel(test,ComponentPanel.AND));
		pane2.getContentPane().add (new ComponentPanelLabel(test,ComponentPanel.AND));
		pane2.getContentPane().add (new ComponentPanelLabel(test,ComponentPanel.AND));
		pane2.getContentPane().add (new ComponentPanelLabel(test,ComponentPanel.AND));
		pane2.getContentPane().add (new ComponentPanelLabel(test,ComponentPanel.AND));
		pane2.getContentPane().add (new ComponentPanelLabel(test,ComponentPanel.AND));
		pane2.getContentPane().add (new ComponentPanelLabel(test,ComponentPanel.AND));
		pane2.getContentPane().add (new ComponentPanelLabel(test,ComponentPanel.AND));
		
		setLayout (new GridLayout(0, 1));
		
		add (pane1);
		add (pane2);
		
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
