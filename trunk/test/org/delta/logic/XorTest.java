package org.delta.logic;

public class XorTest extends BinaryFunctionTestCase {
    
    public XorTest() {
        setFunction(new Xor());
        
        addTestCase(State.S0, State.S0, State.S0);
        addTestCase(State.S0, State.S1, State.S1);
        addTestCase(State.S1, State.S0, State.S1);
        addTestCase(State.S1, State.S1, State.S0);
        addTestCase(State.SX, State.SX, State.SX);
        addTestCase(State.S0, State.SX, State.SX);
        addTestCase(State.SX, State.S0, State.SX);
        addTestCase(State.S1, State.SX, State.SX);
        addTestCase(State.SX, State.S1, State.SX);
    }
}
