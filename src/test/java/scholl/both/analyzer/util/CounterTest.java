package scholl.both.analyzer.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Class to test the class {@link Counter}.
 * 
 * @author Jackson
 * 
 */
@RunWith(JUnit4.class)
public class CounterTest {
    
    @Test
    public void simpleTest() {
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
}
