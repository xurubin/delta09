package org.delta.circuit;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jgrapht.graph.DefaultDirectedGraph;

public class ComponentGraph extends
        DefaultDirectedGraph<Component, ComponentWire> {
    private Circuit circuit;
    private Map<ComponentWire, Wire> wireMap;

    public ComponentGraph() {
        super(ComponentWire.class);

        wireMap = new HashMap<ComponentWire, Wire>();
    }

    @Override
    public boolean addVertex(Component vertex) {
        if (!super.addVertex(vertex)) return false;

        Circuit c = vertex.getCircuit();
        for (Gate g: c.vertexSet()) {
            circuit.addVertex(g);
        }
        for (Wire w: c.edgeSet()) {
            circuit.addEdge(c.getEdgeSource(w), c.getEdgeTarget(w), w);
        }
        return true;
    }

    @Override
    public boolean removeAllEdges(Collection<? extends ComponentWire> edgeSet) {
        // TODO: Update circuit.
        return super.removeAllEdges(edgeSet);
    }

    @Override
    public Set<ComponentWire> removeAllEdges(Component source, Component target) {
        // TODO: Update circuit.
        return super.removeAllEdges(source, target);
    }

    @Override
    public boolean removeAllVertices(Collection<? extends Component> arg0) {
        // TODO: Update circuit.
        return super.removeAllVertices(arg0);
    }

    @Override
    public ComponentWire removeEdge(Component arg0, Component arg1) {
        // TODO: Update circuit.
        return super.removeEdge(arg0, arg1);
    }

    @Override
    public boolean removeEdge(ComponentWire arg0) {
        // TODO: Update circuit.
        return super.removeEdge(arg0);
    }

    @Override
    public boolean removeVertex(Component arg0) {
        // TODO: Update circuit.
        return super.removeVertex(arg0);
    }

    public boolean registerEdge(ComponentWire edge, int sourceOutputNumber,
            int targetInputNumber) {
        if (!edgeSet().contains(edge) || getEdgeSource(edge) == null
                                      || getEdgeTarget(edge) == null) {
            return false;
        }

        Component sourceComponent = getEdgeSource(edge);
        Component targetComponent = getEdgeTarget(edge);

        if(targetComponent.getInputWire(targetInputNumber) != null) {
            return false;
        }
        Wire w = circuit.addEdge(
            sourceComponent.getOutputGate(sourceOutputNumber),
            targetComponent.getInputGate(targetInputNumber)
        );

        wireMap.put(edge, w);
        return true;
    }

    public Circuit getCircuit() {
        if (!isValid()) return null;

        return (Circuit) circuit.clone();
    }

    public boolean isValid() {
        return false;
    }
}
