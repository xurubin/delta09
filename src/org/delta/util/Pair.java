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
     * UID for serialisation. 
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

    /**
     * Two pairs with the same elements have the same hash code (generated by
     * eclipse).
     * @return a hash code.
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((first == null) ? 0 : first.hashCode());
        result = prime * result + ((second == null) ? 0 : second.hashCode());
        return result;
    }

    /**
     * Two pairs are equal if they have the same elements (generated by
     * eclipse).
     * @return true if equal, false otherwise.
     * @see Object#equals(Object) 
     */
    @SuppressWarnings("unchecked")
	@Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Pair other = (Pair) obj;
        if (first == null) {
            if (other.first != null) return false;
        } else if (!first.equals(other.first)) return false;
        if (second == null) {
            if (other.second != null) return false;
        } else if (!second.equals(other.second)) return false;
        return true;
    }
}
