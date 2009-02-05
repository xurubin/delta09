package org.delta.simulation;

import org.delta.circuit.Gate;

public class SimulationEvent {
    private Gate gate;

    public SimulationEvent(final Gate gate) {
        this.gate = gate;
    }

    public Gate getGate() {
        return gate;
    }
}
