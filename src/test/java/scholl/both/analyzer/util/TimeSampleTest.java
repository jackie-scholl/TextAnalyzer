package scholl.both.analyzer.util;

import static org.junit.Assert.*;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.pobox.cbarham.testhelpers.EqualsHashCodeTestCase;

/**
 * Class to test the class {@link TimeSample}.
 * 
 * @author Jackson
 *
 */
@RunWith(Theories.class)
public class TimeSampleTest {
    private static final double delta = 0.0001;
    
    @Test
    @Ignore
    public void test() {
        TimeSample t = new TimeSample();
        
    }
    
}
