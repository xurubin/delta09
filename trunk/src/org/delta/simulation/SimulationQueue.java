package org.delta.simulation;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.delta.circuit.Gate;


public class SimulationQueue {
    private LinkedList<Set<Gate>> eventQueue;

    public SimulationQueue() {
        eventQueue = new LinkedList<Set<Gate>>();
    }

    public synchronized void addGate(Gate gate) {
        if (eventQueue.isEmpty()) {
            eventQueue.add(new HashSet<Gate>());
        }
        eventQueue.getLast().add(gate);
    }
    
    public synchronized void addAllGates(Collection<Gate> gateCollection) {
        if (eventQueue.isEmpty()) {
            eventQueue.add(new HashSet<Gate>(gateCollection));
        } else {
            eventQueue.getLast().addAll(gateCollection);
        }
    }

    synchronized Set<Gate> getFirstEventSet() {
        if (eventQueue.isEmpty()) return null;
        return eventQueue.removeFirst();
    }

    public synchronized void clear() {
        eventQueue.clear();
    }
}
