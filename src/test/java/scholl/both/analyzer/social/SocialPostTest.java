package scholl.both.analyzer.social;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assume.assumeThat;

import java.util.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * Class to test the class {@link Post}.
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
    
    @DataPoint public static User nullUser = null;
    @DataPoint public static User mockUserJackson = new MockSocialUser("Jackson");
    @DataPoint public static User mockUserKeller = new MockSocialUser("Keller");
    
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
        Post p = new Post(str);
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
        Post a = new Post(str);
        Post b = new Post(str, a.getTimestamp(), null);
        assertThat(a, is(equalTo(b)));
    }
    
    @Theory
    public void constructorTest2(String str, long millis, User poster) {
        assumeThat(str, is(notNullValue()));
        Post a = new Post(str, millis, poster);
        Post b = new Post(str, millis, poster, null, new ArrayList<String>());
        assertThat(a, is(equalTo(b)));
    }
        
    @Theory
    public void simpleConstructorTheory(String str, long millis, User poster, 
            User mention, List<String> tags) {
        assumeThat(str, is(notNullValue()));
        assumeThat(poster, is(notNullValue()));
        assumeThat(mention, is(notNullValue()));
        assumeThat(tags, is(notNullValue()));
        
        Post a = new Post(str, millis, poster, mention, tags);
        assertThat(a.getOriginal(), is(equalTo(str)));
        assertThat(a.getTimestamp(), is(equalTo(millis)));
        assertThat(a.getPoster(), is(equalTo(poster)));
        assertThat(a.getMention(), is(equalTo(mention)));
        assertThat(a.getTags(), is(equalTo(tags)));
    }
    
    @Theory
    public void nullTagsTheory(String str, long millis, User poster, User mention) {
        assumeThat(str, is(notNullValue()));
        
        Exception ex = null;
        
        try {
            new Post(str, millis, poster, mention, null);
        } catch (NullPointerException e) {
            ex = e;
        }
        
        assertThat(ex, is(notNullValue()));
    }
    
    @Test
    public void calendarTest() {
        TimeZone tz = TimeZone.getDefault();
        
        Post p = new Post("hello world!");
        Calendar a = p.getCalendar(tz);
        
        Calendar b = Calendar.getInstance(tz);
        a.set(Calendar.MILLISECOND, 0);
        b.set(Calendar.MILLISECOND, 0);
        
        assertThat((double) a.getTimeInMillis(), is(closeTo((double) b.getTimeInMillis(), 100)));
        System.out.printf("%tc%n", a.getTime());
        System.out.printf("%tc%n", Calendar.getInstance().getTime());
    }
    
    @Theory
    public void calendarTheory(String str, long millis, User poster) {
        assumeThat(str, is(notNullValue()));
        assumeThat(poster, is(notNullValue()));
        
        Calendar a = Calendar.getInstance();
        a.setTimeInMillis(millis);
        
        Post p = new Post(str, millis, poster);
        Calendar b = p.getCalendar();
        
        assertThat(a, is(equalTo(b)));
    }
}
