package test;

import com.jopdesign.io.JoPDatagramLayer;
/**
 * @author Rubin
 *
 * DE2 Unit Test
 */
public class DE2_UnitTest {

	public static void main(String[] args) {

		JoPDatagramLayer d = new JoPDatagramLayer();
		int n;
		while (true) {
			d.readLEDHEXStates();
			n = d.getLEDStates();
			n ^= d.getHEX1States();
			n ^= d.getHEX2States();
			d.sendSwitchStates(n);
		}

	}
}
