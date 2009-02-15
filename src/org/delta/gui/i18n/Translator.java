package org.delta.gui.i18n;

import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.ResourceBundle;

public class Translator {
	private ResourceBundle resource;
	enum Key {
		A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,NOVALUE
	}
	
	public Translator() {
		resource = UTF8ResourceBundle.getBundle("Strings", new Locale("en"));
	}
	
	public Translator(Locale l) {
		resource = UTF8ResourceBundle.getBundle("Strings", l);
	}
	
	
	public String getString(String key) {
		return resource.getString(key);
	}
	
	public int getMnemonic(String key) {
		return Translator.getKeyEvent(this.getString(key));
	}
	
	public static int getKeyEvent(String s) {
		Key keyValue = Key.NOVALUE;
		try {
			keyValue = Key.valueOf(s);
		}
		catch(Exception e) {
		}
		
		switch(keyValue) {
		case A: return KeyEvent.VK_A;
		case B: return KeyEvent.VK_B;
		case C: return KeyEvent.VK_C;
		case D: return KeyEvent.VK_D;
		case E: return KeyEvent.VK_E;
		case F: return KeyEvent.VK_F;
		case G: return KeyEvent.VK_G;
		case H: return KeyEvent.VK_H;
		case I: return KeyEvent.VK_I;
		case J: return KeyEvent.VK_J;
		case K: return KeyEvent.VK_K;
		case L: return KeyEvent.VK_L;
		case M: return KeyEvent.VK_M;
		case N: return KeyEvent.VK_N;
		case O: return KeyEvent.VK_O;
		case P: return KeyEvent.VK_P;
		case Q: return KeyEvent.VK_Q;
		case R: return KeyEvent.VK_R;
		case S: return KeyEvent.VK_S;
		case T: return KeyEvent.VK_T;
		case U: return KeyEvent.VK_U;
		case V: return KeyEvent.VK_V;
		case W: return KeyEvent.VK_W;
		case X: return KeyEvent.VK_X;
		case Y: return KeyEvent.VK_Y;
		case Z: return KeyEvent.VK_Z;
		default: return 0;
		}
	}
}
