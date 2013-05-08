package scholl.both.analyzer.social.networks;

import static org.junit.Assert.assertEquals;

import scholl.both.analyzer.social.PostSet;
import scholl.both.analyzer.social.SocialPost;
import scholl.both.analyzer.social.SocialUser;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

public class TwitterClientTest {

    @Test
    public void simpleTest() {
        SocialClient tc = null;
        try {
            tc = new TwitterClient("tumblr_credentials.json");
        } catch (IOException e) {
            org.junit.Assert.assertTrue("Failure on creating client - IO exception:\n", false);
        }
        
        try {
            tc.authenticate();
        } catch (IOException e) {
            org.junit.Assert.assertTrue("Failure on authenticating client - IO exception:\n", false);
        }
        
        SocialUser u = tc.getAuthenticatedUser();
        assertEquals("raptortech97", u.getTitle());
        
        for (SocialUser su : tc.getInterestingUsers()) {
            PostSet ps = su.getPosts(20);
            System.out.printf("%s: %n", su.getName());
            for (SocialPost p : ps.toSet()) {
                System.out.printf("\t%s%n", p);
            }
        }
    }
}
