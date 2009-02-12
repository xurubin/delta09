package org.delta.gui.i18n;


import java.util.Locale;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.delta.gui.MainWindow;

public class TestTranslator {
	public static void main(String args[]) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		MainWindow mw = new MainWindow(new Locale("ar", "SA"));
		mw.setVisible(true);
	}
}