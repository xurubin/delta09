package org.delta.circuit;

abstract public class OutputSelector<C> {
    private C component;
    private Integer outputNumber;
    
    public OutputSelector(C component, int outputNumber) {
        this.component = component;
        this.outputNumber = outputNumber;
    }
    
    public C getComponent() {
        return component;
    }
    
    public Integer getOutputNumber() {
        return outputNumber;
    }
}
