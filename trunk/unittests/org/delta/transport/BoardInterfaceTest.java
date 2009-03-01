package org.delta.transport;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class BoardInterfaceTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBoardInterface() {
		final int DATASET   = 20;
		Random rnd = new Random(System.currentTimeMillis());
		BoardInterface b = BoardInterface.getInstance();
		for(int i=0;i<DATASET;i++) {
			int d1 = rnd.nextInt()&((1<<26) - 1); //LED
			int d2 = rnd.nextInt()&((1<<28) - 1); //HEX3210
			int d3 = rnd.nextInt()&((1<<28) - 1); //HEX7654
			
			for(int j=0;j<26;j++)
				b.sendLEDEvent(j, (d1&(1<<j)) != 0);
			for(int j=0;j<28;j++)
				b.sendHEXEvent(j / 7, j % 7, (d2&(1<<j)) != 0);
			for(int j=0;j<28;j++)
				b.sendHEXEvent(j / 7 + 4, j % 7, (d3&(1<<j)) != 0);
			
			try{Thread.sleep(30L);}catch(Exception e){}
			int r = 0;
			for(int j=0;j<22;j++)
				if (b.getSwitchStatus(j))
					r |= (1 << j);
			
			assertEquals(i+"/"+DATASET+": Bad data from USB. Is DE2 running with DE2_UnitTest?",
					(d1^d2^d3)&((1<<22)-1), r);
		}
	}

}
