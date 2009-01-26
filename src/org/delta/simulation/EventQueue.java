package org.delta.simulation;

public class EventQueue {

    public synchronized void enqueue(StateChangeEvent e) {

    }
    
    public synchronized StateChangeEvent dequeue() {
        return null;
    }
}
