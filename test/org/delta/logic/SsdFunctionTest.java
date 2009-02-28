package org.delta.logic;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.delta.transport.BoardInterface;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.api.Expectation;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SsdFunctionTest {
    private Mockery context;
    private int ssdNumber;
    private BoardInterface boardInterface;
    private List<State> argumentList;
    
    public SsdFunctionTest() {
        context = new JUnit4Mockery() {
            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
    }

    @Before
    public void setUp() throws Exception {
        Random random = new Random();
        ssdNumber = random.nextInt(16);
        boardInterface = context.mock(BoardInterface.class);

        argumentList = new ArrayList<State>();
        State[] states = State.values();
        for (int i = 0; i < 7; ++i) {
            argumentList.add(states[random.nextInt(3)]);
        }
    }

    @Test
    public final void testEvaluate() {
        SsdFunction function = new InjectedSsdFunction();
        function.setSsdNumber(ssdNumber);
        
        for (int i = 0; i < 7; ++i) {
            function.setArgument(i, new Constant(argumentList.get(i)));
        }
        
        final Sequence ssdSequence = context.sequence("SSD Sequence");
        context.checking(new Expectations() {{
            for (int i = 0; i < argumentList.size(); ++i) {
                State arg = argumentList.get(i);
                if (arg == State.S1) {
                    oneOf(boardInterface).sendHEXEvent(ssdNumber, i, true);
                    inSequence(ssdSequence);
                } else if (arg == State.S0) {
                    oneOf(boardInterface).sendHEXEvent(ssdNumber, i, false);
                    inSequence(ssdSequence);
                }
            }
            never(boardInterface);
            inSequence(ssdSequence);
        }});
        
        assertEquals(State.SX, function.evaluate());
        context.assertIsSatisfied();
    }
    
    private class InjectedSsdFunction extends SsdFunction {
        
        @Override
        protected BoardInterface getBoardInterface() {
            return boardInterface;
        }
    }

}
