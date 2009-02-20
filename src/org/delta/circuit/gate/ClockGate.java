package org.delta.circuit.gate;

import java.util.ArrayList;

import org.delta.logic.Constant;
import org.delta.logic.Formula;
import org.delta.logic.Not;
import org.delta.logic.State;

public class ClockGate extends AbstractInputGate {
    /**
     * The clock frequency of the simulated circuit given as a multiple of the
     * simulation frequency.
     * @see SimulationScheduler#simulationFrequency
     * @see ClockGate#setClockFrequency(int)
     */
    // TODO: Default value.
    private int clockFrequency;
    /**
     * Counts up to the value of the clock frequency. When reached, the circuit
     * clock is updated .
     * @see ClockGate#clockFrequency
     */
    private int clockCounter;

    public ClockGate() {
        setState(State.S0);
    }
    
    /**
     * Sets the frequency at which the simulated clock runs and sets the clock
     * counter to zero. If the clock frequency is set to zero, the simulated
     * clock is stopped.
     * @param clockFrequency - integer multiple of the simulation frequency.
     * @see SimulationScheduler#setSimulationFrequency(long)
     * @see ClockGate#clockCounter
     */
    public void setClockFrequency(final int clockFrequency) {
        this.clockFrequency = clockFrequency;
        clockCounter = 0;
    }

    @Override
    public boolean update() {
        if (clockFrequency == 0) return false;

        clockCounter = (clockCounter + 1) % clockFrequency;
        if (clockCounter == 1) {
            Formula output = new Not(new Constant(getState()));
            setState(output.evaluate());
            return true;
        }
        return false;
    }
    
    public String getVerilogMethod(String name, String out, ArrayList<String> in) {
    	return "CLOCK";
    }

}
