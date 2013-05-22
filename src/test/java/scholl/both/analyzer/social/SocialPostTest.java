package scholl.both.analyzer.social;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assume.assumeThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * Class to test the class {@link SocialPost}.
 * 
 * @author Jackson
 * 
 */
@RunWith(Theories.class)
public class SocialPostTest {
    @DataPoint public static String helloWorld = "Hello World!";
    @DataPoint public static String nullString = null;
    @DataPoint public static String emptyString = "";
    
    @DataPoint public static long initTime = System.currentTimeMillis();
    @DataPoint public static long zeroTime = 0L;
    
    @DataPoint public static SocialUser nullUser = null;
    @DataPoint public static SocialUser mockUserJackson = new MockSocialUser("Jackson");
    @DataPoint public static SocialUser mockUserKeller = new MockSocialUser("Keller");
    
    @DataPoint public static List<String> nullStrList = null;
    @DataPoint public static List<String> emptryStrList = new ArrayList<String>();
    @DataPoint public static List<String> sampleStrList = Arrays.asList(new String[]{"a", "b", "b", "d"});
    @DataPoint public static List<String> sampleStrList2 = Arrays.asList(new String[]{"e", "c", "g", "h"});
    
    @BeforeClass
    public static void setupClass() {
        ;
    }
    
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
    public void constructorTest(String str) {
        assumeThat(str, is(notNullValue()));
        SocialPost a = new SocialPost(str);
        SocialPost b = new SocialPost(str, a.getTimestamp(), null);
        assertThat(a, is(equalTo(b)));
    }
    
    @Theory
    public void constructorTest2(String str, long millis, SocialUser poster) {
        assumeThat(str, is(notNullValue()));
        SocialPost a = new SocialPost(str, millis, poster);
        SocialPost b = new SocialPost(str, millis, poster, null, new ArrayList<String>());
        assertThat(a, is(equalTo(b)));
    }
        
    @Theory
    public void simpleConstructorTheory(String str, long millis, SocialUser poster, 
            SocialUser mention, List<String> tags) {
        assumeThat(str, is(notNullValue()));
        assumeThat(poster, is(notNullValue()));
        assumeThat(mention, is(notNullValue()));
        assumeThat(tags, is(notNullValue()));
        
        SocialPost a = new SocialPost(str, millis, poster, mention, tags);
        assertThat(a.getOriginal(), is(equalTo(str)));
        assertThat(a.getTimestamp(), is(equalTo(millis)));
        assertThat(a.getPoster(), is(equalTo(poster)));
        assertThat(a.getMention(), is(equalTo(mention)));
        assertThat(a.getTags(), is(equalTo(tags)));
    }
    
    @Theory
    public void nullTagsTheory(String str, long millis, SocialUser poster, SocialUser mention) {
        assumeThat(str, is(notNullValue()));
        
        Exception ex = null;
        
        try {
            new SocialPost(str, millis, poster, mention, null);
        } catch (NullPointerException e) {
            ex = e;
        }
        
        assertThat(ex, is(notNullValue()));
    }
    
    @Test
    public void calendarTest() {
        SocialPost p = new SocialPost("hello world!");
        Calendar c = p.getTime();
        System.out.println(c);
        System.out.printf("%tc%n", c.getTime());
    }
}
