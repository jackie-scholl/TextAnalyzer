package scholl.both.analyzer.social;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Class to test the class {@link SocialStats}.
 * 
 * @author Jackson
 * 
 */
@RunWith(JUnit4.class)
public class SocialStatsTest {
    
    @Test
    @Ignore
    public void simpleTest() {
        try {
            SocialStats.tumlbrThing(300);
        } catch (IOException e) {
            org.junit.Assert.assertTrue("Failure - IO exception:\n", false);
        }
    }
}