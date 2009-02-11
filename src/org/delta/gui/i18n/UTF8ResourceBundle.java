package org.delta.gui.i18n;

import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

abstract public class UTF8ResourceBundle {
	public static final ResourceBundle getBundle(String baseName) {
		ResourceBundle bundle = ResourceBundle.getBundle(baseName);
		return createUTF8PropertyResourceBundle(bundle);
	}

	public static final ResourceBundle getBundle(String baseName, Locale locale) {
		ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale);
		return createUTF8PropertyResourceBundle(bundle);
	}

	public static ResourceBundle getBundle(String baseName, Locale locale, ClassLoader loader) {
		ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale, loader);
		return createUTF8PropertyResourceBundle(bundle);
	}

	private static ResourceBundle createUTF8PropertyResourceBundle(
			ResourceBundle bundle) {
		if (!(bundle instanceof PropertyResourceBundle))
			return bundle;
		return new UTF8PropertyResourceBundle((PropertyResourceBundle) bundle);
	}
}
