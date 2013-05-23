package scholl.both.analyzer.social.networks;

import java.io.IOException;

import org.junit.Test;
import org.junit.Ignore;

/**
 * Class to test the class {@link TumblrClient}.
 * 
 * @author Jackson
 * 
 */
public class TumblrClientTest {
    
    @Test
    @Ignore
    public void simpleTest() {
        Client tc = null;
        try {
            tc = new TumblrClient("twitter_credentials.txt");
        } catch (IOException e) {
            org.junit.Assert.assertTrue("Failure on creating client - IO exception:\n", false);
        }
        
        try {
            tc.authenticate();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println(tc.getAuthenticatedUser());
    }
}
