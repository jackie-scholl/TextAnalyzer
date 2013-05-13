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
    
    public String toString() {
        return list.toString();
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
        Sample other = (Sample) obj;
        if (this.list == null) {
            if (other.list != null)
                return false;
        } else if (!this.list.equals(other.list))
            return false;
        return true;
    }
}
