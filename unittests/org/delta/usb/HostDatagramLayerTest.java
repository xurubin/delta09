/**
 * 
 */
package org.delta.usb;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.jopdesign.io.HostDatagramLayer;
import java.util.Random;

import junit.framework.Assert;
/**
 * @author Delta 09 
 * 
 */
public class HostDatagramLayerTest {

	protected static HostDatagramLayer datagram = null; 
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		datagram = new HostDatagramLayer();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		datagram.close();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}


	/**
	 * Test method for {@link com.jopdesign.io.HostDatagramLayer#readSwitchStates()}.
	 * Test method for {@link com.jopdesign.io.HostDatagramLayer#sendLEDHEXStates(int, long)}.
	 */
	@Test
	public void testDataTransmission() {
		Assert.assertTrue("Cannot find USB_Blaster cable. Is another program holding the device?",
				datagram.isLinkOK());
		final int MAX_RETRY = 3;
		final int DATASET   = 100;
		Random rnd = new Random(System.currentTimeMillis());
		for(int i=0;i<DATASET;i++) {
			int d1 = rnd.nextInt()&((1<<26) - 1);
			int d2 = rnd.nextInt()&((1<<28) - 1);
			int d3 = rnd.nextInt()&((1<<28) - 1);
			int tries = 0;
			datagram.readSwitchStates();
			while (tries<MAX_RETRY) {
				tries++;
				datagram.sendLEDHEXStates(d1, ((long)d2<<28) | (long)d3);
				int r = datagram.readSwitchStates();
				if (r == -1) continue;
				assertEquals("Bad data from USB. Is DE2 running with DE2_UnitTest?", (d1^d2^d3)&((1<<28)-1), r);
				break;
			}
			Assert.assertTrue("Too many retries for USB. Is DE2 running with DE2_UnitTest?", tries<MAX_RETRY);			
		}
	}

}
