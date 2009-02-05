package org.delta.simulation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;

import org.delta.circuit.Circuit;
import org.delta.circuit.Gate;
import org.delta.circuit.Wire;
import org.delta.logic.State;

public class Simulator extends TimerTask {
    /**
     * The clock frequency of the simulated circuit given as a multiple of the
     * simulation frequency.
     * @see SimulationScheduler#simulationFrequency
     */
    private int clockFrequency; // TODO: Default value.
    private int clockCounter;
    private SimulationQueue simulationQueue;
    private Circuit circuit;

    public Simulator(Circuit circuit) {
        simulationQueue = new SimulationQueue();
    }

    public int getClockFrequency() {
        return clockFrequency;
    }

     public void setClockFrequency(final int clockFrequency) {
        this.clockFrequency = clockFrequency;
    }

    @Override
    public void run() {
        if ((clockCounter = (++clockCounter) % clockFrequency) == 0) {
            SimulationEvent clockEvent = new SimulationEvent(
                circuit.getClock()
            );
            simulationQueue.clockTick(clockEvent);
        }

        List<SimulationEvent> eventList = simulationQueue.dequeue();
        List<SimulationEvent> updateList = new LinkedList<SimulationEvent>();

        Set<Gate> nextGateList = new HashSet<Gate>();
        HashMap<Wire, State> wireToValueMap = new HashMap<Wire, State>();

        for (Iterator<SimulationEvent> i = eventList.iterator(); i.hasNext();) {
            SimulationEvent event = i.next();

            Gate gate = event.getGate();
            State gateOutput = gate.getFormula().evaluate();

            Set<Wire> outgoingWires = circuit.outgoingEdgesOf(gate);
            if (outgoingWires.isEmpty()
             || outgoingWires.iterator().next().getState() == gateOutput) {
                i.remove();
                continue;
            }

            for (Wire wire: outgoingWires) {
                wireToValueMap.put(wire, gateOutput);

                Gate targetGate = circuit.getEdgeTarget(wire);
                if (!nextGateList.contains(targetGate)) {
                    nextGateList.add(targetGate);
                    updateList.add(
                        new SimulationEvent(targetGate)
                    );
                }
            }
        }

        for (Wire wire: wireToValueMap.keySet()) {
            wire.setValue(wireToValueMap.get(wire));
        }
        simulationQueue.enqueue(updateList);
    }

}
