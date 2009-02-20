package org.delta.circuit.gate;

import java.util.ArrayList;

import org.delta.circuit.Gate;
import org.delta.logic.BinaryFunction;
import org.delta.logic.Constant;
import org.delta.logic.Formula;

public class GateFactory {
    
    /**
     * Hidden constructor.
     */
    private GateFactory() {}

    public static Gate createGate(Class<? extends BinaryFunction> functionClass,
            final int inputCount) {
        if (functionClass == null) {
            throw new NullPointerException("Argument must be non-null.");
        }

        if (inputCount <= 1) {
            throw new IllegalArgumentException(
                "Input count must greater than one."
            );
        }
        
        try {
        if (inputCount == 2) {
            BinaryFunction f = (BinaryFunction) (functionClass.newInstance());
            return newBinaryFunction(f);
        }
        
        ArrayList<BinaryFunction> functionArray =
            new ArrayList<BinaryFunction>(inputCount);
        
        for (int i = inputCount - 1; i >= 0 ; --i) {
            BinaryFunction f = (BinaryFunction) (functionClass.newInstance());
            functionArray.add(f);
            if (i != inputCount - 1)
                f.setArg1(functionArray.get(i+1));
        }
        
        return newBinaryFunction(functionArray);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    
    private static Gate newBinaryFunction(
            final ArrayList<BinaryFunction> functionArray) {
        return new Gate(functionArray.size()) {

			@Override
            public Formula getFormula() {
                for (int i = 0; i < functionArray.size(); ++i) {
                    BinaryFunction f = functionArray.get(i);
                    f.setArg0(new Constant(getWire(i)));
                }
                
                return functionArray.get(0);
            }
            @Override
            public String getVerilogMethod(String name, String out,
                    ArrayList<String> in) {
            	return Gate.constructDefaultVerilogMethod(functionArray.get(0).getClass().getSimpleName(), name, out, in);
            }
            
        };
    }

    private static Gate newBinaryFunction(final BinaryFunction function) {
        return new Gate(2) {

            @Override
            public Formula getFormula() {
                function.setArg0(new Constant(getWire(0)));
                function.setArg1(new Constant(getWire(1)));
                return function;
            }
            
            @Override
            public String getVerilogMethod(String name, String out,
                    ArrayList<String> in) {
            	return Gate.constructDefaultVerilogMethod(function.getClass().getSimpleName().toLowerCase(), name, out, in);
            }
            
        };
    }

}
