package scholl.both.analyzer.text;

import static org.junit.Assert.assertEquals;

import scholl.both.analyzer.util.Sample;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.experimental.theories.*;

import com.pobox.cbarham.testhelpers.EqualsHashCodeTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
@RunWith(Theories.class)
public class TextTest{
    private static final double delta = 0.0001;
    @DataPoint public static Text ex1 = new Text("What a to do to die today at a minute or two to two. A thing distinctly hard to say but harder still to do.");
    @Test
    public void getSentenceCountTest() {
        assertThat(ex1.getSentenceCount(), is(2));
    }
}