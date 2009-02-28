package org.delta.logic;


import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.delta.util.Pair;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public abstract class BinaryFunctionTestCase {
    final Map<Pair<State, State>, State> testMap =
        new HashMap<Pair<State,State>, State>();
    BinaryFunction function;
    
    final void addTestCase(State arg0, State arg1, State result) {
        testMap.put(new Pair<State, State>(arg0, arg1), result);
    }
    
    final void setFunction(BinaryFunction function) {
        this.function = function;
    }
    
    @Before
    public final void verifyTestMap() throws Exception {
        if (testMap.size() != 9) {
            throw new IllegalArgumentException("Results list is invalid.");
        }
    }

    @Test
    public final void evaluateTest() {
        for (Entry<Pair<State, State>, State> testCase: testMap.entrySet()) {
            function.setArgument(0, new Constant(testCase.getKey().first));
            function.setArgument(1, new Constant(testCase.getKey().second));
            assertEquals(testCase.getValue(), function.evaluate());
        }
    }

}
