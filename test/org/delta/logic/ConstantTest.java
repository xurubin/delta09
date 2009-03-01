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
    public void ConstantConstuctorTest() {
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
    
    @Test
    public final void equalsTest() {
        Constant constantS0a = new Constant(State.S0);
        Constant constantS0b = new Constant(State.S0);
        assertTrue(constantS0a.equals(constantS0b));
        
        Constant constantS1a = new Constant(State.S1);
        Constant constantS1b = new Constant(State.S1);
        assertTrue(constantS1a.equals(constantS1b));
        
        Constant constantSXa = new Constant(State.SX);
        Constant constantSXb = new Constant(State.SX);
        assertTrue(constantSXa.equals(constantSXb));
        
        assertFalse(constantS0a.equals(constantS1a));
        assertFalse(constantS1a.equals(constantS0a));
        
        assertFalse(constantS0a.equals(constantSXa));
        assertFalse(constantSXa.equals(constantS0a));
        
        assertFalse(constantS1a.equals(constantSXa));
        assertFalse(constantSXa.equals(constantS1a));
    }

}
