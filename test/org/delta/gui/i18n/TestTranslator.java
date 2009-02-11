package org.delta.gui.i18n;


import java.util.Locale;

import org.delta.gui.MainWindow;

public class TestTranslator {
	public static void main(String args[]) {
		MainWindow mw = new MainWindow(new Locale("fr"));
		mw.setVisible(true);
	}
}