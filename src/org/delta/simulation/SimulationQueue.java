package org.delta.simulation;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


public class SimulationQueue {
    private LinkedList<LinkedList<SimulationEvent>> eventQueue;

    public SimulationQueue() {
        eventQueue = new LinkedList<LinkedList<SimulationEvent>>();
    }

    public synchronized void enqueue(SimulationEvent event) {
        eventQueue.getLast().add(event);
    }
    
    public synchronized void enqueue(Collection<SimulationEvent> eventList) {
        eventQueue.getLast().addAll(eventList);
    }

    synchronized List<SimulationEvent> dequeue() {
        return eventQueue.removeFirst();
    }

    synchronized void clockTick(final SimulationEvent tick) {
        LinkedList<SimulationEvent> list = new LinkedList<SimulationEvent>();
        list.add(tick);
        eventQueue.add(list);
    }

    synchronized void clear() {
        eventQueue.clear();
    }
}
