package org.delta.gui.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

public class Translator {
	private ResourceBundle resource;
	
	public Translator() {
		resource = UTF8ResourceBundle.getBundle("Strings", new Locale("en"));
	}
	
	public Translator(Locale l) {
		resource = UTF8ResourceBundle.getBundle("Strings", l);
	}
	
	
	public String getString(String key) {
		return resource.getString(key);
	}
}
