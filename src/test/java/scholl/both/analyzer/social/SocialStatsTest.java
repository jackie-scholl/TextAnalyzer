package scholl.both.analyzer.social;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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
            Set<String> users = new HashSet<String>();
            users.add("dataandphilosophy");
            SocialStats.tumblrAnalysis(users, 100);
        } catch (IOException e) {
            org.junit.Assert.assertTrue("Failure - IO exception:\n", false);
        }
    }
}