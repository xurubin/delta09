package org.delta.util;

import java.io.Serializable;

/**
 * Immutable pair.
 *
 * @param <T1> - type of the first element in the pair.
 * @param <T2> - type of the second element in the pair.
 */
public class Pair<T1, T2> implements Serializable {
    /**
     * UID for serialization. 
     */
    private static final long serialVersionUID = 1L;
    /**
     * First element of the pair.
     */
    public final T1 first;
    /**
     * Second element of the pair.
     */
    public final T2 second;

    /**
     * Creates a new a pair.
     * @param first - first element of the new pair.
     * @param second - second element of the new pair.
     */
    public Pair(final T1 first, final T2 second) {
        this.first = first;
        this.second = second;
    }
}
