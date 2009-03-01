package org.delta.logic;

import static org.junit.Assert.*;

import java.util.Random;

import org.delta.transport.BoardInterface;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;

public class LedConstantTest {
    private Mockery context;
    private BoardInterface boardInterface;
    private int ledNumber;
    
    public LedConstantTest() {
        context = new JUnit4Mockery() {
            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
    }

    @Before
    public void setUp() throws Exception {
        Random random = new Random();
        ledNumber = random.nextInt(25);
        boardInterface = context.mock(BoardInterface.class); 
    }
    
    @Test
    public final void evaluateTest() {
        
        LedConstant ledS0 = new InjectedLedConstant(State.S0);
        LedConstant ledS1 = new InjectedLedConstant(State.S1);
        LedConstant ledSX = new InjectedLedConstant(State.SX);
        
        ledS0.setLedNumber(ledNumber);
        ledS1.setLedNumber(ledNumber);
        ledSX.setLedNumber(ledNumber);
        
        final Sequence ledEvent = context.sequence("LED Event");
        context.checking(new Expectations() {{
            oneOf(boardInterface).sendLEDEvent(ledNumber, false);
            inSequence(ledEvent);
            oneOf(boardInterface).sendLEDEvent(ledNumber, true);
            inSequence(ledEvent);
            never(boardInterface);
            inSequence(ledEvent);
        }});
        assertEquals(State.S0, ledS0.evaluate());
        assertEquals(State.S1, ledS1.evaluate());
        assertEquals(State.SX, ledSX.evaluate());

        context.assertIsSatisfied();
    }

    private class InjectedLedConstant extends LedConstant {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public InjectedLedConstant(State state) {
            super(state);
        }
        
        @Override
        protected BoardInterface getBoardInterface() {
            return boardInterface;
        }
    }
}
