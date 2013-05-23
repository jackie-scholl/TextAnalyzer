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
        String[] texts = new String[]{"a", "b", "c", "d", "e", "f"};
        for (String str : texts) {
            ps1.add(new Post(str));
            sleepShort();
        }
    }
    
    private static void sleepShort() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Theory
    public void testGetPostArray(PostSet ps) {
        assumeThat(ps, is(notNullValue()));
        
        Post[] posts1 = ps.toArray();
        Post[] posts2 = ps.toSet().toArray(new Post[0]);
        assertThat(posts1, is(equalTo(posts2)));
    }
    
    @Theory
    public void testGetPostList(PostSet ps) {
        assumeThat(ps, is(notNullValue()));
        
        assertThat(ps.toList(), is(equalTo(Arrays.asList(ps.toArray()))));
    }
    
    @Theory
    public void testGetAllWithTag(PostSet ps, String tag) {
        assumeThat(ps, is(notNullValue()));
        assumeThat(tag, is(notNullValue()));
        
        PostSet withTag = ps.getAllWithTag(tag);
        
        for (Post post : ps) {
            assertThat(post.getTags().contains(tag), is(equalTo(withTag.contains(post))));
        }
    }
    
    @Test
    public void testGetMostRecent() {
        assertThat(ps1.getMostRecent().getOriginal(), is(equalTo("f")));
    }
    
    @Test
    public void testGetOldest() {
        assertThat(ps1.getOldest().getOriginal(), is(equalTo("a")));
    }
    
    @Test
    public void testGetAllByUser() {
        //fail("Not yet implemented");
    }
    
    @Theory
    public void testGetAllTags(PostSet ps) {
        assumeThat(ps, is(notNullValue()));
        
        Set<String> tags = new HashSet<String>();
        for (Post p : ps) {
           tags.addAll(p.getTags()); 
        }
        assertThat(ps.getAllTags(), is(equalTo(tags)));
    }
    
    @Theory
    public void testSize(PostSet ps) {
        assumeThat(ps, is(notNullValue()));
        
        int size = ps.size();
        assertThat(size, is(equalTo(ps.toSet().size())));
        assertThat(size, is(equalTo(ps.toArray().length)));
        assertThat(size, is(equalTo(ps.toList().size())));
    }
    
}
