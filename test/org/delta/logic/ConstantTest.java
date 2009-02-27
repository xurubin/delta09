package org.delta.logic;

import static org.junit.Assert.*;

import org.delta.circuit.Wire;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConstantTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }
    
    @Test
    public void ConstantTest() {
        Constant constantNullState = new Constant((State) null);
        assertEquals(State.SX, constantNullState.evaluate());

        Constant constantNullWire = new Constant((State) null);
        assertEquals(State.SX, constantNullWire.evaluate());
        
        State s1 = State.S1;
        Constant constantS1 = new Constant(State.S1);
        assertSame(s1, constantS1.evaluate());
        
        State s0 = State.S0;
        Wire wireS0 = new Wire();
        wireS0.setValue(s0);
        Constant constantS0Wire = new Constant(s0);
        assertSame(s0, constantS0Wire.evaluate());
    }

}
