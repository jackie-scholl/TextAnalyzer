package scholl.both.analyzer.util;

import java.util.Iterator;

import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;

/**
 * Stores a sorted sample of entries.
 * 
 * @author Jackson
 */
public class Sample<E extends Comparable<E>> implements Iterable<E> {
    protected SortedMultiset<E> list;
    
    /**
     * Create an empty sample.
     */
    public Sample() {
        this.list = TreeMultiset.create();
    }
    
    /**
     * Create a sample with the given entries.
     * 
     * @param entries initial entries
     */
    public Sample(E... entries) {
        this();
        addAll(entries);
    }
    
    /**
     * Create a sample with the given entries.
     * 
     * @param entries initial entries
     */
    public Sample(Iterable<E> entries) {
        this();
        addAll(entries);
    }
    
    /**
     * Add the given entry to the sample.
     * 
     * @param x entry to add
     */
    public void add(E x) {
        list.add(x);
    }
    
    /**
     * Add all the given entries to the sample.
     * 
     * @param arr the array of entries to add to the sample
     */
    public void addAll(E[] arr) {
        for (E x : arr) {
            add(x);
        }
    }
    
    /**
     * Add all the given entries to the sample.
     * 
     * @param iterable the array of entries to add to the sample
     */
    public void addAll(Iterable<E> iterable) {
        for (E x : iterable) {
            add(x);
        }
    }
    
    /**
     * Get the entry of members of the sample.
     * 
     * @return the size of the sample
     */
    public int size() {
        return list.size();
    }
    
    public E[] toArr() {
        @SuppressWarnings("unchecked")
        E[] arr = (E[]) new Object[list.size()];
        int i = 0;
        for (E x : list) {
            arr[i++] = x;
        }
        
        return arr;
    }
    
    public Iterator<E> iterator() {
        return list.iterator();
    }
    
    public String toString() {
        return list.toString();
    }
    
    public String toString2() {
        StringBuffer sb = new StringBuffer();
        for (E e : this) {
            sb.append(e.toString()+"\n");
        }
        return sb.toString();
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.list == null) ? 0 : this.list.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Sample))
            return false;
        Sample<?> other = (Sample<?>) obj;
        if (this.list == null) {
            if (other.list != null)
                return false;
        } else if (!this.list.equals(other.list))
            return false;
        return true;
    }
}
