package org.delta.circuit;

import org.jgrapht.graph.ListenableDirectedGraph;

public class ListenableComponentGraph
        extends ListenableDirectedGraph<Component, ComponentWire> {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ComponentGraph componentGraph;

    public ListenableComponentGraph(ComponentGraph graph) {
        super(graph);
        
        this.componentGraph = graph;
    }
    
    public ComponentGraph getComponentGraph() {
        return componentGraph;
    }
}
