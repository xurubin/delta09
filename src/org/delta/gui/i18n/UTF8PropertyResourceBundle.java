package org.delta.gui.i18n;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class UTF8PropertyResourceBundle extends ResourceBundle {
	PropertyResourceBundle bundle;
	
	public UTF8PropertyResourceBundle(PropertyResourceBundle b) {
		bundle = b;
	}
	
	@Override
	public Enumeration<String> getKeys() {
		return bundle.getKeys();
	}

	@Override
	protected Object handleGetObject(String key) {
		String value = (String)bundle.handleGetObject(key);
		if(value == null) return null;
		try {
			return new String(value.getBytes("ISO-8859-1"),"UTF-8");
		}
		catch(UnsupportedEncodingException e) {
			return null;
		}
	}

}
