package scholl.both.analyzer.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.stat.StatUtils;

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
    private double[] arr; // Caches the array returned by list.toArray
    private double sum;
    
    public Sample() {
        this.list = TreeMultiset.create();
        this.arr = null;
        sum = 0.0;
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
    
    /**
     * Add the given number to the sample.
     * 
     * @param x number to add
     */
    public void add(double x) {
        list.add(x);
        sum += x;
        arr = null;
    }
    
    /**
     * Add all the given numbers to the sample.
     * 
     * @param x the array of numbers to add to the sample
     */
    public void addAll(double[] x) {
        for (double d : x) {
            add(d);
        }
    }
    
    /**
     * Add all the given numbers to the sample.
     * 
     * @param x the array of numbers to add to the sample
     */
    public void addAll(Double[] x) {
        for (Double d : x) {
            add(d);
        }
    }
    
    /**
     * Add all the given numbers to the sample.
     * 
     * @param iterable the array of numbers to add to the sample
     */
    public void addAll(Iterable<Double> iterable) {
        for (Double d : iterable) {
            add(d);
        }
    }
    
    /**
     * Get the number of members of the sample.
     * 
     * @return the size of the sample
     */
    public int size() {
        return list.size();
    }
    
    /**
     * Get the sum of all the members of the sample.
     * 
     * @return the sum of the sample
     */
    public double sum() {
        return sum;
    }
    
    /**
     * Get the mean of all the members of the sample.
     * 
     * @return the mean of the sample
     */
    public double mean() {
        return sum() / size();
    }
    
    /**
     * Get the greatest element in the sample.
     * 
     * @return the greatest element
     */
    public double max() {
        return list.lastEntry().getElement();
    }
    
    /**
     * Get the least element in the sample.
     * 
     * @return the least element
     */
    public double min() {
        return list.firstEntry().getElement();
    }
    
    /**
     * Get the range of the sample, that is, the difference between the greatest and least elements.
     * 
     * @return the full range of the sample
     */
    public double range() {
        return max() - min();
    }
    
    /**
     * Get the population variance of the sample.
     * 
     * @return the population variance
     */
    public double populationVariance() {
        return StatUtils.populationVariance(getArray(), mean());
    }
    
    /**
     * Returns the standard deviation of the sample.
     * 
     * @return the standard deviation
     */
    public double getStandardDeviation() {
        return Math.sqrt(populationVariance());
    }
    
    /**
     * Get the estimated element at the p'th percentile.
     *  
     * @param p percentile to estimate
     * @return the estimated p'th percentile
     */
    public double percentile(double p) {
        return StatUtils.percentile(getArray(), p);
    }
    
    private double[] getArray() {
        if (arr == null) { // This means there have been additions since last caching
            arr = new double[list.size()];
            int i = 0;
            for (double d : list) {
                arr[i++] = d;
            }
        }        
        return arr;
    }
    
    public double[] toArr() {
        double[] arr = new double[size()];
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
