package scholl.both.analyzer.text;

import java.util.*;
import scholl.both.analyzer.social.PostSet;
import scholl.both.analyzer.social.SocialPost;
import scholl.both.analyzer.text.Text;
import org.apache.commons.math3.stat.correlation.*;

public class TextAnalyzer {
    SocialPost[] posts;
    /*
     * Main method is a basic test if given no arguments
     * If it is given arguments, it returns the text along 
     * with the number of words and the number of characters in each.
     * @param some number of strings to analyze
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            String[] newArgs = { "hello", "hello world", "this is a simple test" };
            args = newArgs;
        }

        for (String str : args) {
            Text t = new Text(str);
            System.out.printf("%s: %d words, %d characters %n", t.getOriginal(), t.getWordCount(),
                    t.getCharacterCount());
        }     
    }
    /*
     * Makes an instance of TextAnalyzer with the given posts.
     */
    TextAnalyzer(PostSet p){
        posts = p.getPosts();
    }
    //Example
    //I want to see if sentence length and word length
    //are correlated across posts
    double sentenceWordLengthCorrelation(){
        //Set up two arrays to hold the variables.
        double[] avgWordLengthArr    = new double[posts.length];
        double[] avgSentenceLengthArr= new double[posts.length];
        //Add the variables into the arrays
        for (int i = 0; i < posts.length; i++){
            avgSentenceLengthArr[i] = 
                    posts[i].getText().averageSentenceLength();
            avgWordLengthArr[i] = 
                    posts[i].getText().averageWordLength();
        }
        //Returns the standard measure of correlation
        return new Covariance().covariance(
                avgWordLengthArr, avgSentenceLengthArr);
    }
}
