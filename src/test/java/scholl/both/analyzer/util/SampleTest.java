package scholl.both.analyzer.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeThat;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.pobox.cbarham.testhelpers.EqualsHashCodeTestCase;

/**
 * Class to test the class {@link Sample}.
 * 
 * @author Jackson
 * 
 */
@RunWith(Theories.class)
public class SampleTest extends EqualsHashCodeTestCase {
    private static final double delta = 0.0001;
    
    @DataPoint public static Sample ex1 = new Sample(3.0, 5.0, 7.0);
    
    static double[] bx = new double[]{1.0, 2.0, 1.0};
    static Double[] cx = new Double[]{1.0, 2.0, 1.0};
    static List<Double> dx = new ArrayList<Double>();
    static {
        dx.add(1.0);
        dx.add(2.0);
        dx.add(1.0);
    }
    
    @Test
    public void constructorsTest() {
        Sample a = new Sample(1.0, 2.0, 1.0);
        Sample b = new Sample(bx);
        Sample c = new Sample(cx);
        Sample d = new Sample(dx);
        
        assertEquals(a, b);
        assertEquals(a, c);
        assertEquals(a, d);
    }
    
    @Theory
    public void constructorEqualsTest(Sample a) {
        assumeThat(a, is(notNullValue()));
        Sample b = new Sample(a);
        assertThat(a, is(equalTo(b)));
        assertThat(a, is(not(sameInstance(b))));
    }
    
    @Test
    public void sizeTest() {
        Sample s = new Sample();
        assertEquals(0, s.size());
        s.add(3.4);
        assertEquals(1, s.size());
        
        for (int i=0; i<20; i++) {
            s.add(5.2);
            assertEquals(2+i, s.size());
        }
    }
    
    @Test
    public void sumTest() {
        assertThat(ex1.sum(), is(closeTo(15.0, delta)));
    }
    
    @Test
    public void meanTest() {
        assertThat(ex1.mean(), is(closeTo(5.0, delta)));
    }
    
    @Theory
    public void meanTheory(Sample s) {
        assumeThat(s.size(), is(greaterThan(0)));
        assertThat(s.mean(), is(closeTo(s.sum()/s.size(), delta)));
    }
    
    @Theory
    public void staysSame(Sample s) {
        assertThat(s, is(new Sample(s.toArr())));
    }
    
    @Test
    public void maxTest() {
        assertThat(ex1.max(), is(closeTo(7.0, delta)));
    }
    
    @Test
    public void minTest() {
        assertThat(ex1.min(), is(closeTo(3.0, delta)));
    }
    
    @Test
    public void rangeTest() {
        assertThat(ex1.range(), is(closeTo(4.0, delta)));
    }
    
    @Test
    public void populationVarianceTest() {
        Sample s = new Sample(1, 2, 3, 4, 5, 6);
        assertThat(s.populationVariance(), is(closeTo(35.0/12.0, delta)));
    }
    
    @Test
    public void varianceTest() {
        Sample s = new Sample(1, 2, 3, 4, 5, 6);
        assertThat(s.variance(), is(closeTo(7.0/2.0, delta)));
    }
    
    @Test
    public void standardDeviationTest() {
        Sample s = new Sample(1, 2, 3, 4, 5, 6);
        assertThat(s.standardDeviation(), is(closeTo(Math.sqrt(7.0/2.0), delta)));
    }
    
    @Test
    public void percentileTest() {
        assertThat(ex1.percentile(50), is(closeTo(5.0, delta)));
    }

    double[] arr1 = new double[] {3.2, 4.5, 6.7};
    double[] arr2 = new double[] {3.0, 4.7, 6.2};
        
    @Override
    protected Object createInstance() throws Exception {
        return new Sample(arr1);
    }

    @Override
    protected Object createNotEqualInstance() throws Exception {
        return new Sample(arr2);
    }
}
