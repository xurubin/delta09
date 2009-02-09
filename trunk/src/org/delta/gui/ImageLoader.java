package org.delta.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ImageLoader
{
	private static ImageLoader instance;
	
	private MediaTracker mt;
	private Toolkit tk;
	
	private ImageLoader()
	{
		MainWindow mw = MainWindow.get();
		mt = new MediaTracker (mw);
		tk = mw.getToolkit();
	}
	
	public Image loadImage (String name)
	{
		Image img = tk.createImage ("src/org/delta/gui/diagram/images/" + name);

		mt.addImage (img, 0);
		
		try
		{
			 mt.waitForAll();
		}
		catch (Exception e)
		{
			if ( mt.isErrorAny() )
			{
				System.out.println ("Image did not load!");		// Pop up a window in real thing
				return null;
			}
		}
		
		return img;
	}
	
	public static ImageLoader get()
	{
		if (instance == null)
			instance = new ImageLoader();
		return instance;
	}
}
