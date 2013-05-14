package scholl.both.analyzer.util;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.experimental.theories.*;

import com.pobox.cbarham.testhelpers.EqualsHashCodeTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;

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
    
    @Test
    public void sizeTest() {
        Sample s = new Sample();
        assertEquals(0, s.getSize());
        s.add(3.4);
        assertEquals(1, s.getSize());
        
        for (int i=0; i<20; i++) {
            s.add(5.2);
            assertEquals(2+i, s.getSize());
        }
    }
    
    @Test
    public void sumTest() {
        assertThat(ex1.getSum(), is(closeTo(15.0, delta)));
    }
    
    @Test
    public void meanTest() {
        assertThat(ex1.getMean(), is(closeTo(5.0, delta)));
    }
    
    @Theory
    public void meanTheory(Sample s) {
        assumeThat(s.getSize(), is(greaterThan(0)));
        assertThat(s.getMean(), is(closeTo(s.getSum()/s.getSize(), delta)));
    }
    
    @Theory
    public void staysSame(Sample s) {
        assertThat(s, is(new Sample(s.toArr())));
    }
    
    @Test
    public void maxTest() {
        assertThat(ex1.getMax(), is(closeTo(7.0, delta)));
    }
    
    @Test
    public void minTest() {
        assertThat(ex1.getMin(), is(closeTo(3.0, delta)));
    }
    
    @Test
    public void rangeTest() {
        assertThat(ex1.getRange(), is(closeTo(4.0, delta)));
    }
    
    @Test
    public void varianceTest() {
        Sample s = new Sample(1, 2, 3, 4, 5, 6);
        assertThat(s.getPopulationVariance(), is(closeTo(35.0/12.0, delta)));
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
