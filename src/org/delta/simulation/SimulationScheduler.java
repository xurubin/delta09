package org.delta.simulation;

import java.util.Timer;
import java.util.TimerTask;

public class SimulationScheduler {
    private Simulator simulator;
    private Timer timer = new Timer();
    /**
     * Time in milliseconds between two discrete times steps in the simulation.
     */
    private long simulationFrequency = 300L; // TODO: set default value.

    public SimulationScheduler(Simulator simulator) {
        this.simulator = simulator;
    }

    public void start() {
        timer.scheduleAtFixedRate((TimerTask) simulator, 0,
                simulationFrequency);
    }

    public void stop() {
        timer.cancel();
    }

    public void setSimulationFrequency(final long frequency) {
        this.simulationFrequency = frequency;
    }

    public long getSimulationFrequency() {
        return simulationFrequency;
    }

}
