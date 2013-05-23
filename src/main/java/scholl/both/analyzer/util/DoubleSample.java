package scholl.both.analyzer.util;

import java.util.Iterator;

import org.apache.commons.math3.stat.StatUtils;

import alg4lib.StdDraw;
import alg4lib.StdStats;

//import StdStats;

/**
 * Stores a sample of floating-point numbers from a distribution and supports statistical
 * operations.
 * 
 * @author Jackson
 */
public class DoubleSample extends Sample<Double> {
    private double sum;
    
    /**
     * Create an empty sample.
     */
    public DoubleSample() {
        super();
        sum = 0.0;
    }
    
    /**
     * Create a sample with the given entries.
     * 
     * @param entries initial entries
     */
    public DoubleSample(double... entries) {
        this();
        addAll(entries);
    }
    
    /**
     * Create a sample with the given entries.
     * 
     * @param entries initial entries
     */
    public DoubleSample(Double[] entries) {
        this();
        addAll(entries);
    }
    
    /**
     * Create a sample with the given entries.
     * 
     * @param entries initial entries
     */
    public DoubleSample(Iterable<Double> entries) {
        this();
        addAll(entries);
    }
    
    /**
     * Add the given number to the sample.
     * 
     * @param x number to add
     */
    public void add(double x) {
        super.add(x);
        sum += x;
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
     * Get the variance of the sample.
     * 
     * @return the variance
     */
    public double variance() {
        return StatUtils.variance(getArray(), mean());
    }
    
    /**
     * Returns the standard deviation of the sample.
     * 
     * @return the standard deviation
     */
    public double standardDeviation() {
        return Math.sqrt(variance());
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
    
    public void plot() {
        StdStats.plotPoints(getArray());
        StdDraw.show(5000);
    }
    
    /**
     * Get the list as an array.
     * 
     * @return the list as an array
     */
    private double[] getArray() {
        return toPrimitiveArr();
    }
    
    public double[] toPrimitiveArr() {
        double[] arr = new double[size()];
        int i = 0;
        for (double d : list) {
            arr[i++] = d;
        }
        return arr;
    }
}
