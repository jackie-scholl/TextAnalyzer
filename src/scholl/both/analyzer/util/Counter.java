package scholl.both.analyzer.util;

import java.util.*;

//TODO: Add Javadocs

public class Counter<K> {
    private Map<K, Integer> map;
    private int size;
    
    public Counter() {
        map = new HashMap<K, Integer>();
        size = 0;
    }
    
    public void add(K key, int amount) {
        size += amount;
        Integer count = map.get(key);
        if (count == null) {
            count = 0;
        }
        count += amount;
        map.put(key, count);
    }
    
    public void add(K key) {
        add(key, 1);
    }
    
    public void addAll(Counter<K> other) {
        for (K key : other.getKeys()) {
            this.add(key, other.get(key));
        }
    }
    
    public int remove(K key) {
        Integer count = map.remove(key);
        if (count == null) {
            count = 0;
        }
        size -= count;
        return count;
    }
    
    public int get(K key) {
        return map.get(key);
    }
    
    public List<K> getSorted() {
        List<K> list = new ArrayList<K>();
        Comparator<Integer> inverseComparator = new Comparator<Integer>(){
            public int compare(Integer a, Integer b) {
                return Integer.compare(b, a);
            }
        };
        SortedMap<Integer, Set<K>> inverse = new TreeMap<>(inverseComparator);
        for (K key : map.keySet()) {
            int count = map.get(key);
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
    
    public Set<K> getKeys() {
        return map.keySet();
    }
    
    /**
     * Make a copy of the internal map.
     * 
     * @return the copied map
     */
    public Map<K, Integer> getMap() {
        Map<K, Integer> map2 = new HashMap<K, Integer>();
        map2.putAll(map);
        return map2;
    }
    
    public String toString() {
        List<K> list = getSorted();
        String s = "{\n";
        for (int i=0; i<list.size(); i++) {
            //s += String.format("%s%s:%d", (i>0? ", " : ""), list.get(i), get(list.get(i)));
            s += String.format("%s : %d%n", list.get(i), get(list.get(i)));
        }
        s += "}";
        return s;
    }
}
