package scholl.both.analyzer.util;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.experimental.theories.*;

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
@RunWith(JUnit4.class)
public class SampleTest {
    
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
        double[] arr = new double[]{3.0, 5.0, 7.0};
        Sample s = new Sample(arr);
        assertThat(s.getSum(), is(closeTo(15.0, 0.001)));
    }
}
