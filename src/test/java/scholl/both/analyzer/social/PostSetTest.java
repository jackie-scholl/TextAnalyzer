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

@RunWith(Theories.class)
public class PostSetTest {
    @DataPoint public static PostSet nullps = null;
    @DataPoint public static PostSet emptyps = new PostSet();
    @DataPoint public static PostSet ps1;
    
    @DataPoint public static String helloWorld = "Hello World!";
    @DataPoint public static String nullString = null;
    @DataPoint public static String emptyString = "";
    @DataPoint public static String a = "a";
    @DataPoint public static String b = "b";
    @DataPoint public static String c = "c";
    @DataPoint public static String d = "d";
    
    @BeforeClass
    public static void setupClass() {
        ps1 = new PostSet();
        SocialPost[] posts = new SocialPost[]{};
        String[] texts = new String[]{"a", "b", "c", "d", "e", "f"};
        for (String str : texts) {
            ps1.add(new SocialPost(str));
        }
    }
    
    @Theory
    public void testGetPostArray(PostSet ps) {
        assumeThat(ps, is(notNullValue()));
        
        SocialPost[] posts1 = ps.getPostArray();
        SocialPost[] posts2 = ps.toSet().toArray(new SocialPost[0]);
        assertThat(posts1, is(equalTo(posts2)));
    }
    
    @Theory
    public void testGetPostList(PostSet ps) {
        assumeThat(ps, is(notNullValue()));
        
        assertThat(ps.getPostList(), is(equalTo(Arrays.asList(ps.getPostArray()))));
    }
    
    @Theory
    public void testGetAllWithTag(PostSet ps, String tag) {
        assumeThat(ps, is(notNullValue()));
        assumeThat(tag, is(notNullValue()));
        
        PostSet withTag = ps.getAllWithTag(tag);
        
        for (SocialPost post : ps) {
            assertThat(post.getTags().contains(tag), is(equalTo(withTag.contains(post))));
        }
    }
    
    @Test
    public void testGetMostRecent() {
        //fail("Not yet implemented");
    }
    
    @Test
    public void testGetOldest() {
        //fail("Not yet implemented");
    }
    
    @Test
    public void testGetAllByUser() {
        //fail("Not yet implemented");
    }
    
    @Test
    public void testGetAllTags() {
        //fail("Not yet implemented");
    }
    
    @Test
    public void testSize() {
        //fail("Not yet implemented");
    }
    
}
