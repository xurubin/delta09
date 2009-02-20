package org.delta.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BidirectionalIntegerMap<Entry> implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<Entry, Integer> entryToIndexMap;
    private ArrayList<Entry> indexToEntryList;
    private int size;

    public BidirectionalIntegerMap(int size) {
        this.size = size;
        
        entryToIndexMap = new HashMap<Entry, Integer>(size);
        indexToEntryList = new ArrayList<Entry>(size);
        
        // Initialise ArrayList
        for (int i = 0; i < size; ++i) {
            indexToEntryList.add(null);
        }
    }
    
    public void set(int index, Entry entry) {
        if (entry == null) {
            throw new NullPointerException("The entry must be non-null.");
        }
        checkIndex(index);
        
        indexToEntryList.set(index, entry);
        entryToIndexMap.put(entry, index);
    }
    
    public Entry getEntry(int index) {
        checkIndex(index);
        return indexToEntryList.get(index);
    }
    
    public boolean containsEntry(Entry entry) {
        return entryToIndexMap.containsKey(entry);
    }
    
    public int getIndex(Entry entry) {
        if (entry == null) {
            throw new NullPointerException("The entry must be non-null.");
        }
        
        Integer index = entryToIndexMap.get(entry);
        if (index == null) {
            throw new IllegalArgumentException("Entry is not in the map.");
        }
        
        return index;
    }
    
    public Entry remove(int index) {
        checkIndex(index);
        Entry entry = getEntry(index);
        
        indexToEntryList.set(index, null);
        entryToIndexMap.remove(entry);
        
        return entry;
    }
    
    public int remove(Entry entry) {
        int index = getIndex(entry);
        
        indexToEntryList.set(index, null);
        entryToIndexMap.remove(entry);
        
        return index;
    }
    
    public int getSize() {
        return size;
    }
    
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException(
                "The specified index is not within the allowed range."
            );
        }
    }
}
