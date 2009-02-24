package org.delta.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.delta.circuit.Gate;
import org.delta.circuit.gate.GateFactory;

/**
 * Abstract function class which defines methods to set, retrieve and evaluate
 * arguments. If an implementation has a constructor that does not require any
 * arguments, the {@link GateFactory} class can be used to created a
 * {@link Gate} object based on the function.
 */
public abstract class Function implements Serializable, Formula {
    /**
     * UID for serialisation.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Holds all arguments of the function.
     */
    private final List<Formula> argumentList;

    /**
     * Creates a new function.
     * @param argumentCount - number of arguments.
     */
    public Function(int argumentCount) {
        argumentList = new ArrayList<Formula>(argumentCount);
        for (int i = 0; i < argumentCount; ++i) {
            argumentList.add(new Constant(State.SX));
        }
    }
    
    /**
     * Returns an argument.
     * @param argumentNumber - number of the argument to return.
     * @return the argument associated with the given number.
     * @throws IllegalArgumentException if the argument number is out of range.
     */
    public final Formula getArgument(int argumentNumber) {
        checkIndex(argumentNumber);
        return argumentList.get(argumentNumber);
    }
    
    /**
     * Replaces an old argument with the given number by a new one.
     * @param argumentNumber - the number of the argument.
     * @param argument - the new argument value.
     * @throws IllegalArgumentException if the argument number is out of range.
     * @see Function#checkIndex(int)
     */
    public final void setArgument(int argumentNumber, Formula argument) {
        checkIndex(argumentNumber);
        if (argument != null) {
            argumentList.set(argumentNumber, argument);
        }
    }
    
    /**
     * Gives the number of arguments of the function.
     * @return the number of arguments.
     */
    public final int getArgumentCount() {
        return argumentList.size();
    }
    
    /**
     * Evaluates an argument.
     * @param argumentNumber - number of the argument to be evaluated.
     * @return the evaluation result.
     * @throws IllegalArgumentException if the argument number is out of range.
     * @see Function#checkIndex(int)
     */
    public final State evaluateArgument(int argumentNumber) {
        checkIndex(argumentNumber);
        return getArgument(argumentNumber).evaluate();
    }
    
    /**
     * Checks whether an index to the argument list is out of bounds. 
     * @param index - index into the argument list.
     * @see Function#argumentList
     * @throws IllegalArgumentException if the argument number is out of range.
     */
    private void checkIndex(int index) {
        if (index < 0 || index >= argumentList.size()) {
            throw new IllegalArgumentException("Argument number out of range.");
        }
    }
}
