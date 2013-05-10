package scholl.both.analyzer.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.experimental.theories.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Class to test the class {@link Counter}.
 * 
 * @author Jackson
 * 
 */
@RunWith(JUnit4.class)
@RunWith(Theories.class)
public class CounterTest {
    @DataPoint
    public static Counter<Byte> blankCounter = new Counter();
    @DataPoint
    public static Counter<Byte> nullCounter = null;
    
    
    private Counter<Byte> counter;
    
    
    
    @Test
    public static void simpleTest() {
        Counter<String> cntr = new Counter<String>();
        assertEquals(0, cntr.get("a"));
        cntr.add("a");
        assertEquals(1, cntr.get("a"));
        
        cntr.add("a");
        assertEquals(2, cntr.get("a"));
        
        cntr.add("a", 2);
        assertEquals(4, cntr.get("a"));
        
        cntr.add("a", -2);
        assertEquals(2, cntr.get("a"));
    }
    
    @Theory
    public void equals(Counter<Byte> a, Counter<Byte> b) {
        assumeThat(a != null && b != null);
        boolean equal = true;
        for (byte i = 0; i > -1; i++) {
            equal &= a.get(b) == b.get(a);
            equal &= a.contains(b) == a.contains(b);
        }
        assertEqual(equal, a.equals(b));
        assertEqual(equal, b.equals(a));
    }
}
