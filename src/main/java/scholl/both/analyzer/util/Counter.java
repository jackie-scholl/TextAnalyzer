package scholl.both.analyzer.util;

import java.util.*;

/**
 * For every key, keeps a count. The count can be zero or negative. Unset keys act as if they were
 * 0. Keys are either <b>defined</b> or <b>undefined</b>. An undefined key will act as if it has a
 * value of 0. A defined key may have a value of 0 and still be defined for the purposes of
 * enumerating the defined keys (see {@link #getKeys getKeys} and {@link #getSorted getSorted}).
 * This implementation is NOT thread-safe.
 * 
 * @author Jackson
 * 
 * @param <K> key type
 */
public class Counter<K> {
    private Map<K, Long> map;
    private long sum;
    
    /**
     * Sole constructor. Makes a new Counter object with no keys defined.
     */
    public Counter() {
        map = new HashMap<K, Long>();
        sum = 0;
    }
    
    /**
     * Add one to the key's count
     * 
     * @param key key to increment
     * @return the new count for key
     */
    public long add(K key) {
        return add(key, 1);
    }
    
    /**
     * Add {@code amount} to the key's count.
     * 
     * @param key key to increase the count of
     * @param amount amount to increase the count by
     * @return the new count for key
     */
    public long add(K key, long amount) {
        sum += amount;
        long count = get(key);
        count += amount;
        map.put(key, count);
        return count;
    }
    
    /**
     * Add all the counts from the given other counter.
     * 
     * @param other Counter to add from
     */
    public void addAll(Counter<K> other) {
        for (K key : other.getKeys()) {
            this.add(key, other.get(key));
        }
    }
    
    /**
     * Sets the counter for the given key to the given value.
     * 
     * @param key key to set value of
     * @param value value to set key ot
     * @return the previous value of the key
     */
    public long set(K key, long value) {
        long previousValue = get(key);
        map.put(key, value);
        return previousValue;
    }
    
    /**
     * Undefines {@code key}, effectively setting its value to 0.
     * 
     * <p>
     * The key is removed from the underlying map. Depending on the implementation, this may or may
     * not actually decrease space utilization.
     * 
     * @param key key to reset count of
     * @return the previous count
     */
    public long remove(K key) {
        long count = get(key);
        sum -= count;
        map.remove(key);
        return count;
    }
    
    /**
     * Returns the count associated with {@code key}.
     * 
     * @param key key to get count of
     * @return the count associated with the key
     */
    public long get(K key) {
        Long x = map.get(key);
        if (x == null) {
            x = 0L;
        }
        return x;
    }
    
    /**
     * Returns true if the Counter object has amapping for the given key, false otherwise. This is
     * the only way to tell between a mapped key with a value of 0 and an unmapped key.
     * 
     * @param key key to check for mapping
     * @return true if the key has a mapping; false otherwise
     */
    public boolean contains(K key) {
        return map.containsKey(key);
    }
    
    public long getSum() {
        long sum = 0;
        for (K key : getKeys()) {
            sum += get(key);
        }
        return sum;
    }
    
    /**
     * Get all the defined keys, sorted in decreasing order by count.
     * 
     * @return the sorted list of keys
     */
    public List<K> getSorted() {
        List<K> list = new ArrayList<K>();
        Comparator<Long> inverseComparator = new Comparator<Long>() {
            public int compare(Long a, Long b) {
                return new Long(b).compareTo(a);
            }
        };
        SortedMap<Long, Set<K>> inverse = new TreeMap<Long, Set<K>>(inverseComparator);
        for (K key : map.keySet()) {
            long count = map.get(key);
            Set<K> set = inverse.get(count);
            if (set == null) {
                set = new HashSet<K>();
            }
            set.add(key);
            inverse.put(count, set);
        }
        
        for (Set<K> set : inverse.values()) {
            for (K key : set) {
                list.add(key);
            }
        }
        
        return list;
    }
    
    /**
     * Return the set of all the defined keys.
     * 
     * @return the set of defined keys
     */
    public Set<K> getKeys() {
        return map.keySet();
    }
    
    /**
     * Make a copy of the internal map.
     * 
     * @return the copied map
     */
    public Map<K, Long> getMap() {
        Map<K, Long> map2 = new HashMap<K, Long>();
        map2.putAll(map);
        return map2;
    }
    
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        Counter<K> other = (Counter) obj;
        Set<K> keys = getKeys();
        if (!keys.equals(other.getKeys())) {
            return false;
        }
        for (K key : keys) {
            if (!(this.get(key)).equals(other.get(key))) {
                return false;
            }
        }
        return true;
    }
    
    public int hashCode() {
        int hash = 17;
        hash = 31*hash + map.hashCode();
        return hash;
    }
    
    /**
     * Returns a string representing the key/value pairs of this counter in the primary format,
     * largely applicable for a small number of pairs printed to the terminal. This method calls the
     * key objects' toString methods. Examples:
     * 
     * <pre>
     *      {hello:3, hi:2, hey:-1}
     * </pre>
     * 
     * <pre>
     *      {hello:3}
     * </pre>
     * 
     * <pre>
     * {}
     * </pre>
     */
    @Override
    public String toString() {
        List<K> list = getSorted();
        String s = "{";
        for (int i = 0; i < list.size(); i++) {
            s += String.format("%s%s:%d", (i > 0 ? ", " : ""), list.get(i), get(list.get(i)));
        }
        s += "}";
        return s;
    }
    
    /**
     * Returns a string representing the key/value pairs of this counter in the secondary format,
     * largely applicable for printing large numbers of pairs to files. This method calls the key
     * objects' toString methods. Examples:
     * 
     * <pre>
     *      hello : 3
     *      hi : 2
     *      hey : -1
     * </pre>
     * 
     * <pre>
     *      hello : 3
     * </pre>
     * 
     * <pre>
     *      (empty string)
     * </pre>
     */
    public String toString2() {
        List<K> list = getSorted();
        String s = "";
        for (int i = 0; i < list.size(); i++) {
            s += String.format("%s : %d%n", list.get(i), get(list.get(i)));
        }
        return s;
    }
}
