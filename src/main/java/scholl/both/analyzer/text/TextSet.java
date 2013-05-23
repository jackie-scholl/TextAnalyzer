package scholl.both.analyzer.text;

import scholl.both.analyzer.social.PostSet;
import scholl.both.analyzer.social.Post;
import scholl.both.analyzer.text.Text;

import org.apache.commons.math3.stat.correlation.*;

public class TextSet {
    private PostSet texts;
    
    /**
     * Main method is a basic test if given no arguments. If it is given arguments, it returns the
     * text along with the number of words and the number of characters in each.
     * 
     * @param args some number of strings to analyze
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
    
    /**
     * Base constructor.
     * 
     */
    public TextSet(){
        this.texts = new PostSet();
    }
    
    public void add(String t) {
        texts.add(new Post(t));
    }
    
    
    /**
     * Example: I want to see if sentence length and word length are correlated across posts.
     * 
     * @return standard measure of correlation
     */
    public double sentenceWordLengthCorrelation(){
        //Set up two arrays to hold the variables.
        double[] avgWordLengths     = new double[texts.size()];
        double[] avgSentenceLengths = new double[texts.size()];
        //Add the variables into the arrays
        int i = 0;
        for (Text t : texts.toSet()){
            avgWordLengths[i] = t.averageWordLength();
            avgSentenceLengths[i] = t.averageSentenceLength();
        }
        
        //Returns the standard measure of correlation
        return new Covariance().covariance(
                avgWordLengths, avgSentenceLengths);
    }
}
