package org.delta.circuit.gate;

import java.util.ArrayList;
import java.util.List;

import org.delta.circuit.Gate;
import org.delta.logic.BinaryFunction;
import org.delta.logic.Constant;
import org.delta.logic.Formula;
import org.delta.logic.Function;

public class GateFactory {
    
    /**
     * Hidden constructor.
     */
    private GateFactory() {}
    
    private static Function createInstance(Class<? extends Function> f) {
        try {
            return f.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static Gate createGate(Class<? extends BinaryFunction> funClass,
            final int inputCount) {
        if (funClass == null) {
            throw new NullPointerException("Argument must be non-null.");
        }

        if (inputCount <= 1) {
            throw new IllegalArgumentException(
                "Input count must greater than one."
            );
        }
        
        final int functionCount = inputCount - 1;
        
        /* Do not create an array for the simple case, where the number of
         * inputs of the gate equals the number of function arguments.
         */
        if (inputCount == 2) {
            return createGate(funClass);
        }
        
        final List<BinaryFunction> funList =
            new ArrayList<BinaryFunction>(functionCount);
        
        // Initialise function array.
        for (int i = 0; i < functionCount; ++i) {
            final BinaryFunction f = (BinaryFunction) createInstance(funClass);
            funList.add(f);
        }
        
        // Set function arguments.
        for (int i = 0; i < functionCount - 1; ++i) {
            final BinaryFunction next = funList.get(i + 1);
            funList.get(i).setArgument(0, next);
        }
        
        return new Gate(inputCount) {
            private static final long serialVersionUID = 1L;

            @Override
            public Formula getFormula() {
                for (int i = 0; i < funList.size(); ++i) {
                    final BinaryFunction f = funList.get(i);

                    f.setArgument(1, new Constant(getWire(i)));
                    if (i == funList.size() - 1) {
                        f.setArgument(0, new Constant(getWire(i + 1)));
                    }
                }
                
                return funList.get(0);
            }

            @Override
            public String getVerilogMethod(String name, ArrayList<String> out,
                    ArrayList<String> in) {
                Class<?> formulaClass = funList.get(0).getClass();
                String type = formulaClass.getSimpleName().toLowerCase();

                return Gate.constructDefaultVerilogMethod(type, name, out, in);
            }
            
            @Override
            public String toString() {
                return "Gate: " + funList.get(0).getClass().getSimpleName();
            }
            
        };
    }

    public static Gate createGate(final Class<? extends Function> funClass) {
        final Function function = createInstance(funClass);
        
        return new Gate(function.getArgumentCount()) {
            private static final long serialVersionUID = 1L;

            @Override
            public Formula getFormula() {
                for (int i = 0; i < function.getArgumentCount(); ++i) {
                    function.setArgument(i, new Constant(getWire(i)));
                }
                return function;
            }
            
            @Override
            public String getVerilogMethod(String name, ArrayList<String> out,
                    ArrayList<String> in) {
                Class<?> formulaClass = function.getClass();
                String type = formulaClass.getSimpleName().toLowerCase();
                
            	return Gate.constructDefaultVerilogMethod(type, name, out, in);
            }
            
            @Override
            public String toString() {
                return "Gate: " + function.getClass().getSimpleName();
            }
            
        };
    }

}
