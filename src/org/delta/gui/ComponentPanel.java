package org.delta.gui;

import java.awt.*;
import java.util.*;

public class ComponentPanel extends javax.swing.JPanel
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
	
	public ComponentPanel()
	{
		il = ImageLoader.get();
		
		cats = new ArrayList <Category> (2);
		
		Category nandandand = new Category ( "'Nand' and 'And' gates", 2 );
		Category norandor   = new Category ( "'Nor' and 'Or' gates",   2 );
//		Category yourface   = new Category ( "YOUR FACE", 1);

		nandandand.add ("nand.png", "Nand Gate", "and.png", "And Gate");
	//	nandandand.add ("new.png", "Huh?");

		norandor.add ("nor.png", "Nor Gate");
		norandor.add ("or.png", "Or Gate");
		
//		yourface.add ("open.png", "Like?");
//		yourface.add ("paste.png", "Your face", "copy.png", "COPEH!!!11");
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
