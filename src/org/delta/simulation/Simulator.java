package org.delta.simulation;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

import org.delta.circuit.Circuit;
import org.delta.circuit.Gate;
import org.delta.circuit.Wire;
import org.delta.logic.State;

/**
 * An event-driven discrete time simulation algorithm for digital logic.
 * @author Group Delta 2009
 *
 */
public final class Simulator extends TimerTask {
    /**
     * The clock frequency of the simulated circuit given as a multiple of the
     * simulation frequency.
     * @see SimulationScheduler#simulationFrequency
     * @see Simulator#setClockFrequency(int)
     */
    // TODO: Default value.
    private int clockFrequency;
    /**
     * Counts up to the value of the clock frequency. When reached, the circuit
     * clock is updated .
     * @see Simulator#clockTick()
     * @see Simulator#clockFrequency
     */
    private int clockCounter;
    /**
     * Keeps a queue of lists of gates whose output has to be re-calculated.
     * Each list of gates corresponds to one time step in the discrete time
     * simulation.
     */
    private SimulationQueue simulationQueue = new SimulationQueue();
    /**
     * The circuit to simulate.
     * @see Simulator#setCircuit(Circuit)
     */
    private Circuit circuit;

    /**
     * Empty constructor.
     */
    public Simulator() {
    }
    
    /**
     * Sets the circuit which the simulator should simulate.
     * @param circuit - the circuit to simulate.
     */
    public void setCircuit(final Circuit circuit) {
        this.circuit = circuit;
    }

    /**
     * Sets the frequency at which the simulated clock runs and sets the clock
     * counter to zero. If the clock frequency is set to zero, the simulated
     * clock is stopped.
     * @param clockFrequency - integer multiple of the simulation frequency.
     * @see SimulationScheduler#setSimulationFrequency(long)
     * @see Simulator#clockCounter
     */
    public void setClockFrequency(final int clockFrequency) {
        this.clockFrequency = clockFrequency;
        clockCounter = 0;
    }

    /**
     * Advance the internal (counter) clock by one step. When it reaches the
     * value of the clock frequency, output of the simulated circuit clock is
     * changed.
     * @see Simulator#clockFrequency
     */
    private void clockTick() {
        if (clockFrequency == 0) return;

        clockCounter = (clockCounter + 1) % clockFrequency;
        if (clockCounter == 0) {
            simulationQueue.addClock(circuit.getClock());
        }
    }

    /**
     * Calculate the next state.
     * @see TimerTask#run()
     */
    @Override
    public void run() {
        clockTick();

        // Retrieve gates whose output has to be re-evaluated for the time step.
        final Set<Gate> gateSet = simulationQueue.getFirstEventSet();
        
        // If the set is empty, we are done.
        if (gateSet == null || gateSet.isEmpty()) return;

        final List<Gate> nextGateList = new LinkedList<Gate>();
        
        /* Temporary store for wires whose state value is altered in this
         * simulation step. Maps from the new state value to a list of wires.
         */
        final Map<State, LinkedList<Wire>> nextWireState
            = new EnumMap<State, LinkedList<Wire>>(State.class);
        // Initialise map for all three possible state values.
        for (State state: State.values()) {
            nextWireState.put(state, new LinkedList<Wire>());
        }
        
        /* Iterate over all gates, re-evaluate their output and, if it has
         * changed, propagate new value, i.e. create a list (nextGateList) of
         * gates connected to the output of one of those gates.
         */
        for (Gate gate: gateSet) {
            final State gateOutput = gate.getFormula().evaluate();

            final Set<Wire> outgoingWires = circuit.outgoingEdgesOf(gate);
            for (Wire wire: outgoingWires) {
                /* If gate output has not changed, there is no need to update
                 * gates connected to it.
                 */
                if (wire.getState() == gateOutput) break;
                
                // Add wire to the map which is later used to update its value.
                nextWireState.get(gateOutput).add(wire);

                /* Add gate at the other end of the wire to the list of gates
                 * which will be updated in the next time step of the
                 * simulation.
                 */
                final Gate targetGate = circuit.getEdgeTarget(wire);
                if (!nextGateList.contains(targetGate)) {
                    nextGateList.add(targetGate);
                }
            }
        }

        // Update wire values.
        for (State state: State.values()) {
            for (Wire wire: nextWireState.get(state)) {
                wire.setValue(state);
            }
        }
        
        // Add gates which need re-evaluation to the queue.
        simulationQueue.addAllGates(nextGateList);
    }

    // FIXME: Only for debug purposes.
    @Deprecated
    public void addGate(final Gate gate) {
        simulationQueue.addGate(gate);
    }

}