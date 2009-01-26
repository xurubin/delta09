package org.delta.circuit;

import org.jgrapht.EdgeFactory;

public class ComponentWireFactory implements
        EdgeFactory<Component, ComponentWire> {

    private ComponentGraph componentGraph;

    @Override
    public ComponentWire createEdge(Component sourceVertex,
            Component targetVertex) {
        return null;
    }

    public ComponentGraph getComponentGraph() {
        return componentGraph;
    }
    
    public void setComponentGraph(ComponentGraph componentGraph) {
        this.componentGraph = componentGraph;
    }

    public ComponentWire createComponentWire(ComponentInputSelector in,
            ComponentOutputSelector out) {
        return null;
    }

}
