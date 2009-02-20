package org.delta.gui;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

public class AboutAction extends AbstractAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AboutAction(String text, String accelerator, int mnemonic)
	{
		super(text);
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator));
		this.putValue(Action.MNEMONIC_KEY, mnemonic);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		// Display about dialog.
		JFrame jf = new JFrame();
		Container container = jf.getContentPane();
		container.setLayout(new BorderLayout());
		container.setPreferredSize(new Dimension(500,400));
		
		ImageIcon icon = new ImageIcon("src/org/delta/gui/icons/logo.png");
		JLabel imageLabel = new JLabel("", icon, JLabel.CENTER);
		JLabel text = new JLabel("<html><center><h1>Project Delta: An Interactive FPGA Circuit Simulator</h1><p>Developed for CST IB group project. </p><h2>Developed By:</h2>" +
				"<p>Robert Duncan<br>Justus Matthiesen<br>David Weston<br>Christopher Wilson<br>Rubin Xu</p></center></html>", JLabel.CENTER);
		
		container.add(imageLabel, BorderLayout.NORTH);
		container.add(text, BorderLayout.CENTER);
		
		jf.pack();
		jf.setVisible(true);
		
	}
}