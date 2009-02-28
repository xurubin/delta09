package org.delta.logic;


import static org.junit.Assert.*;

import org.junit.Test;

public class NotTest {

    @Test
    public final void evaluateTest() {
        Not function = new Not();

        Constant s0 = new Constant(State.S0);
        Constant s1 = new Constant(State.S1);
        Constant sX = new Constant(State.SX);

        function.setArgument(0, s0);
        assertEquals(State.S1, function.evaluate());
        
        function.setArgument(0, s1);
        assertEquals(State.S0, function.evaluate());

        function.setArgument(0, sX);
        assertSame(State.SX, function.evaluate());
    }

}
