package org.delta.simulation;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SimulationScheduler {
    private Simulator simulator;
    private Timer timer;
    /**
     * Time in milliseconds between two discrete times steps in the simulation.
     */
    private long simulationFrequency; // TODO: set default value.

    public SimulationScheduler(Simulator simulator) {
        this.simulator = simulator;
    }

    public void start() {
        timer.scheduleAtFixedRate((TimerTask) simulator, new Date(),
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
