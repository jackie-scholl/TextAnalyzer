package scholl.both.analyzer.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores a sample of floating-point numbers from a distribution and supports statistical operations.
 * 
 * @author Jackson
 *
 */
public class Sample {
    private List<Double> list;
    
    public Sample() {
        this.list = new ArrayList<Double>();
    }
    
    public void add(Double d) {
        list.add(d);
    }
    
    public int getSize() {
        return list.size();
    }
}
