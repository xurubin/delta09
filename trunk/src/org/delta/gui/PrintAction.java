package org.delta.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

public class PrintAction extends AbstractAction implements Printable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PrintAction (String text, String accelerator, int mnemonic)
	{
		super (text);
		this.putValue ( Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke (accelerator) );
		this.putValue ( Action.MNEMONIC_KEY, mnemonic );
	}
	
	public void actionPerformed (ActionEvent e)
	{
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPrintable (this);
		boolean ok = job.printDialog();
		if (ok)
		{
			try
			{
				job.print();
			}
			catch (PrinterException ex) {
				System.out.println (ex);
			}
		}
	}
	
	public int print (Graphics g, PageFormat pf, int page) throws PrinterException
	{
		if (page > 0)/* We have only one page, and 'page' is zero-based */
			return NO_SUCH_PAGE;
		
		/* User (0,0) is typically outside the imageable area, so we must
		 * translate by the X and Y values in the PageFormat to avoid clipping
		 */
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate ( pf.getImageableX(), pf.getImageableY() );
		
		/* Now we perform our rendering */
		MainWindow.get().circuit_panel.printAll (g2d);
		
		/* tell the caller that this page is part of the printed document */
		return PAGE_EXISTS;
	}

}
