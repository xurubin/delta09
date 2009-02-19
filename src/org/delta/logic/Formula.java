package org.delta.logic;

import java.io.Serializable;

public abstract class Formula implements Serializable {
    abstract public State evaluate();
}
