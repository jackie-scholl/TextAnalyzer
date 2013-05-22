package scholl.both.analyzer.social;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assume.assumeThat;

import java.util.ArrayList;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class SocialPostTest {
    @DataPoint public static String helloWorld = "Hello World!";
    @DataPoint public static String nullString = null;
    @DataPoint public static String emptyString = "";
    
    @DataPoint public static long initTime = System.currentTimeMillis();
    @DataPoint public static long zeroTime = 0L;
    
    @DataPoint public static SocialUser nullUser = null;
    @DataPoint public static SocialUser mockUserJackson = new MockSocialUser("Jackson");
    
    @Theory
    public void currentTimeTest(String str) {
        assumeThat(str, is(notNullValue()));
        double millis = System.currentTimeMillis();
        SocialPost p = new SocialPost(str);
        double newTime = p.getTimestamp();
        assertThat(newTime, is(closeTo(millis, 100)));
    }
    
    @Theory
    public void currentTimeTest2(String str) {
        assumeThat(str, is(notNullValue()));
        
    }
    
    @Theory
    public void constructorTest(String str, long millis, SocialUser poster) {
        assumeThat(str, is(notNullValue()));
        SocialPost a = new SocialPost(str, poster, millis);
        SocialPost b = new SocialPost(poster, millis, str, null, new ArrayList<String>());
        assertThat(a, is(equalTo(b)));
    }
}
