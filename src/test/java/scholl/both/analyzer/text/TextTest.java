package scholl.both.analyzer.text;

import static org.junit.Assert.assertEquals;

import scholl.both.analyzer.util.Sample;

import java.util.Comparator;
import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.experimental.theories.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;

@RunWith(Theories.class)
public class TextTest{
    private static final double delta = 0.0001;
    @DataPoint public static Text ex1 = new Text("What a to do to die today at a minute or two to" +
    		" two. A thing distinctly hard to say but harder still to do.");
    @Test(expected = NullPointerException.class)
    public void constructorNullStringTest() {
        new Text(null);
    }
    @Test
    public void getSentenceCountTest() {
        assertThat(ex1.getSentenceCount(), is(2));
    }
    @Test
    public void getCharacterCountTest() {
        assertThat(ex1.getCharacterCount(), is(107));
    }
    @Test
    public void getOriginalTest() {
        assertThat(ex1.getOriginal(), is("What a to do to die today at a minute or two to two. A thing distinctly hard to say but harder still to do."));
    }
    @Test
    public void getWordsTest() {
        assertThat(ex1.getWords(), is(Arrays.asList(new String[]{"What", "a", "to", "do", "to", 
                "die", "today", "at", "a", "minute", "or", "two", "to", "two", "A", "thing", 
                "distinctly", "hard", "to", "say", "but", "harder", "still", "to", "do"})));
    }
    @Test
    public void getWordCount(){
        assertThat(ex1.getWordCount(), is(25));
    }
    @Test
    public void averageWordLengthTest() {
        assertThat(ex1.averageWordLength(), is(closeTo(3.24, delta)));
    }
    @Test
    public void getCharCountTest() {
        //Adding l after the end makes something a long.
        assertThat(ex1.getCharCount('h'), is(4L));
        assertThat(ex1.getCharCount('x'), is(0L));
    }
    @Test
    public void getSentencesTest() {
        assertThat(ex1.getSentences(), is(Arrays.asList(new String[]{
                "What a to do to die today at a minute or two to two", 
                "A thing distinctly hard to say but harder still to do"})));
    }
    @Test
    public void getSentencesTest2() {
        String test = "I ate an apple. I met Dr. Smith's wife Mrs. Smith through Mr. John the " +
        		"stock broker for Ms. Count.";
        assertThat(new Text(test).getSentences(), is(Arrays.asList(new String[]{
                "I ate an apple",
                "I met Dr. Smith's wife Mrs. Smith through Mr. John the stock broker for Ms. Count"
        })));
    }
    @Test
    public void getSentencesTest3() {
        String test = "I met Dr. Smith's wife, Mrs. Smith, through Mr. John, the stock broker for" +
        		"Ms. Count, who's lawyer was Artemis Bag, Esq., A.K.A Arty, ..., an esteemed " +
        		"fellow in high society....";
        assertThat(new Text(test).getSentenceCount(), is(1));
        System.out.println(new Text(test).getSentences());
    }
    @Test
    public void getSentenceCount() {
        assertThat(ex1.getSentenceCount(), is(2));
    }
    @Test
    public void averageSentenceLengthTest() {
        assertThat(ex1.averageSentenceLength(), is(closeTo(25.0/2.0, delta)));
    }
    @Test
    public void getPunctuationDiversityIndexTest() {
        assertThat(ex1.getPunctuationDiversityIndex(), is(closeTo(1d, delta)));
    }
    @Test
    public void toStringTest() {
        assertThat(ex1.toString(), is("What a to do to die today at a minute or two to two. A thing distinctly hard to say but harder still to do."));
    }
}