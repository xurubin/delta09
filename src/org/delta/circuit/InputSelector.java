package org.delta.circuit;

public class InputSelector<C> {
    private C component;
    private Integer inputNumber;
    
    public InputSelector(C component, int inputNumber) {
        this.component = component;
        this.inputNumber = inputNumber;
    }
    
    public C getComponent() {
        return component;
    }
    
    public Integer getInputNumber() {
        return inputNumber;
    }
}
