package scholl.both.analyzer.util;

import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;

/**
 * Stores a sample of floating-point numbers from a distribution and supports statistical
 * operations.
 * 
 * @author Jackson
 * 
 */
public class Sample {
    private SortedMultiset<Double> list;
    
    public Sample() {
        this.list = TreeMultiset.create();
    }
    
    public Sample(double... entries) {
        this();
        addAll(entries);
    }
    
    public Sample(Double[] entries) {
        this();
        addAll(entries);
    }
    
    public Sample(Iterable<Double> entries) {
        this();
        addAll(entries);
    }
    
    public void add(Double d) {
        list.add(d);
    }
    
    public void addAll(double[] arr) {
        for (double d : arr) {
            add(d);
        }
    }
    
    public void addAll(Double[] arr) {
        for (Double d : arr) {
            add(d);
        }
    }
    
    public void addAll(Iterable<Double> iterable) {
        for (Double d : iterable) {
            add(d);
        }
    }
    
    public int getSize() {
        return list.size();
    }
    
    public double getSum() {
        double sum = 0.0;
        for (double d : list) {
            sum += d;
        }
        return sum;
    }
    
    public double getMean() {
        return getSum() / getSize();
    }
    
    public double getMax() {
        return list.lastEntry().getElement();
    }
    
    public double getMin() {
        return list.firstEntry().getElement();
    }
    
    public double getRange() {
        return getMax() - getMin();
    }
    
    public double getPopulationVariance() {
        if (getSize() == 0) {
            return Double.NaN;
        }
        
        double sum = 0.0;
        double mean = getMean();
        for (double d : list) {
            sum += Math.pow(d - mean, 2);
        }
        
        return sum / getSize();
    }
    
    /**
     * Get the estimated element at the p'th percentile.
     *  
     * Algoritm taken from Apache Commons Math library:
     * {@link http://commons.apache.org/proper/commons-math/javadocs/api-3.2/org/apache/commons/math3/stat/descriptive/rank/Percentile.html}
     *  
     * @param p
     * @return
     */
    public double getPercentile(double p) {
        int n = getSize();
        if (n == 1) {
            assert getMax() == getMin();
            return getMax();
        }
        
        double pos = p * (n+1)/100.0 - 1;
        int floor = (int) Math.floor(pos);
        double d = pos - floor;
        
        if (pos < 1) {
            return getMin();
        }
        if (pos >= n) {
            return getMax();
        }
        
        Double[] arr = list.toArray(new Double[0]);
        
        double lower = arr[floor];
        double upper = arr[floor+1];
        
        return lower + d * (upper - lower);
    }
    
    public double[] toArr() {
        double[] arr = new double[getSize()];
        int i = 0;
        for (double d : list) {
            arr[i++] = d;
        }
        return arr;
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
