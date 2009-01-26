package org.delta.circuit;

import org.jgrapht.graph.DefaultDirectedGraph;

public class ComponentGraph extends
        DefaultDirectedGraph<Component, ComponentWire> {
    
    private ComponentWireFactory componentWireFactory;
    private Circuit circuit;

    public ComponentGraph() {
        super(new ComponentWireFactory());
        componentWireFactory = (ComponentWireFactory) getEdgeFactory();
        componentWireFactory.setComponentGraph(this);
        // TODO Auto-generated constructor stub
    }
    
    public ComponentWire addComponentWire(ComponentInputSelector in,
            ComponentOutputSelector out) {
        return componentWireFactory.createComponentWire(in, out);
    }
    
    @Override
    public ComponentWire addEdge(Component source, Component target) {
        throw new UnsupportedOperationException("Use addComponentWire method.");
    }
    
    @Override
    public boolean addEdge(Component source, Component target, ComponentWire e) {
        throw new UnsupportedOperationException("Use addComponentWire method.");
    }
    
    public boolean addComponent(Component component) {
        return addVertex(component);
    }
    
    @Override
    public boolean addVertex(Component v) {
        return super.addVertex(v);
    }
    
    public Circuit getCircuit() {
        return (Circuit) circuit.clone();
    }

    public boolean checkIntegrity() {
        return false;
    }
}
