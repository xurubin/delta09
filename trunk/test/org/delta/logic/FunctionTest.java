package org.delta.logic;

import java.util.Random;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FunctionTest {
    private Function function;
    private static int argumentCount;
    private final static int argumentCountMax = 1023;

    @BeforeClass
    public static void randomise() throws Exception {
        Random random = new Random();
        argumentCount = Math.abs(random.nextInt(argumentCountMax));
    }

    @Before
    public void setUp() throws Exception {
        function = new Function(argumentCount) {
            private static final long serialVersionUID = 1L;

            @Override
            public State evaluate() {
                return State.SX;
            }         
        };
    }
    
    @Test
    public final void testGetArgumentCount() {
        assertEquals(argumentCount, function.getArgumentCount());
    }
    
    @Test
    public final void testGetArgument() {
        for (int i = 0; i < argumentCount; ++i) {
            assertEquals(new Constant(State.SX), function.getArgument(i));
        }
    }
    
    @Test(expected=IllegalArgumentException.class)
    public final void testGetArgumentOutOfBoundaries() {
        function.getArgument(argumentCount);
    }
    
    @Test
    public final void testSetArgumentToNull() {
        Formula arg0 = function.getArgument(0);
        function.setArgument(0, null);
        assertSame(arg0, function.getArgument(0));
    }

}
