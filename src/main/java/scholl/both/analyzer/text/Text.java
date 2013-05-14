package scholl.both.analyzer.text;

import java.util.*;
import org.apache.commons.math3.stat.StatUtils;

import scholl.both.analyzer.util.Counter;

/**
 * @author      Keller Scholl <Keller.scholl@gmail.com>
 * @version     0.3
 * @since       2013-04-30
 */
public class Text {
	
    protected final String original;
    protected final String clean;
    protected final List<String> words;
    protected final List<String> sentences;
    
    /**
     * Constructor that creates a text given a String. 
     * 
     * @param original the string that you want to analyze. 
     */
    public Text(String original) {
        this.original = original;
        
        //Strips out links
        original = original.replaceAll("(\\w+://)?(\\w+\\.)+(\\w+)([\\w\\+\\?/\\\\=-]+)*", "");
        
        this.clean = original;

        words = new ArrayList<String>();
        String[] wordsArr = original.split("\\s");
        for (String str : wordsArr) {
            if (str.length() == 0 /* This is an empty string */
                    || str.charAt(str.length()-1) == ':' /* This is a tumblr username followed by a colon to show a quote */
                    || str.matches("(\\w+://)?(\\w+\\.)+(\\w+)([\\w/-=\\?\\\\]*)") /* This is a URL */) {
                continue;
            }
            str = str.replaceAll("[\\p{Punct}\u2018\u2019\u201C\u201D]", ""); // Remove punctuation and quote marks
            if (str.equals("")) { continue; }
            words.add(str);
        }

        //Want to go through the text until I find a sentence ending mark, and then add everything between
        //that mark and the last mark to the list of sentences.
        
        sentences = new ArrayList<String>();
        int lastSentenceEnd = -1;
        //The minimum length of a sentence is four characters(I am, 
        //so periods before and after can safely be excluded.)
        for(int i=3; i < this.clean.length()-4; i++)
        {
            if (clean.charAt(i)=='.'){
                //Covers titles
                if (clean.substring(i-3,  i).matches("(.Dr)|(.Mr)|(Mrs)|(.Ms)|(Esq)|")) continue;
                //Cover ellipses and acronyms
                else if (clean.substring(i-3,  i).matches("(\\p{Upper}\\.\\p{Upper})|(.\\.\\.)")) continue;
                else if (clean.substring(i+1,i+3).matches("(\\p{Upper}\\.)|(\\.\\.)")) continue;
                else if (clean.substring(i-1,i+2).matches("\\.{3}")) continue;
                //If nothing else has forced the for loop to skip past this area, it will add everything from
                //the last sentence ending up to this one. 
                sentences.add(clean.substring(lastSentenceEnd+1,i));
                lastSentenceEnd = i;
            }
            if (clean.charAt(i)=='?'){
                sentences.add(clean.substring(lastSentenceEnd+1,i));
                lastSentenceEnd = i;
            }
        }
        //Ensures that a sentence is always ended. 
        sentences.add(clean.substring(lastSentenceEnd+1));
    }
    
    public int getCharacterCount() {
    	return clean.length();
    }

    public String getOriginal() {
        return clean;
    }

    public List<String> getWords() {
        return words;
    }

    public int getWordCount() {
        return words.size();
    }
    
    public double averageWordLength() {
        int size = words.size();
        double sumLength = 0;
        for (String word : words) {
            sumLength += word.length();
        }
        return sumLength / size;
    }
    
    //public double getWordStandardDeviation() {
    //    return StatUtils.variance(word.size() for word in words);
    //}

    public List<String> getSentences() {
    	return sentences;
    }
    
    public int getSentenceCount() {
    	return sentences.size();
    }

    //Needs to force getWordCount to be a double because otherwise integer division
    //causes problems.
    public double averageSentenceLength() {
        return getSentenceCount()/(double) getWordCount();
    }
    
    public long getCharCount(char desired){
        return getLetterCount2().get(desired);
    }
    
    public Counter<Character> getLetterCount2() {
        Counter<Character> count = new Counter<Character>();
        for (char cha : clean.toCharArray()) {
            count.add(cha);
        }
        return count;
    }

    public Counter<String> getWordCount2() {
        Counter<String> counter = new Counter<String>();
        for (String w : words) {
            counter.add(w.toLowerCase());
        }
        counter.remove("");
        return counter;
    }
    
    public double getPunctuationDiversityIndex(){
        Counter<Character> count = getLetterCount2();
        char[] chars = {'.', ',', ';', ':', '?'};
        List<Double> counts = new ArrayList<Double>();
        for (char c : chars)
            counts.add((double) count.get(c));
        double totalCount = 0;
        double bottomSum = 0;
        for (Double d : counts){
            totalCount+=d;
            bottomSum+=Math.pow(d, 2);
        }
        return Math.pow(totalCount,2)/bottomSum;
    }

    @Override
	public String toString() {
        return clean;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.clean == null) ? 0 : this.clean.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Text other = (Text) obj;
        if (this.clean == null) {
            if (other.clean != null)
                return false;
        } else if (!this.clean.equals(other.clean))
            return false;
        return true;
    }
}
