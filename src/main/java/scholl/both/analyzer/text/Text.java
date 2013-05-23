package scholl.both.analyzer.text;

//import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import org.apache.commons.math3.stat.StatUtils;

import scholl.both.analyzer.util.Counter;

/**
 * @author      Keller Scholl <Keller.scholl@gmail.com>
 * @version     0.3
 * @since       2013-04-30
 */
public class Text {
	
    private final String original;
    private final String clean;
    private final List<String> words;
    private final List<String> sentences;
    /**
     * valenceMapping goes between some words and an
     */
    private final Map<String, Integer> valenceMapping;
    
    /**
     * Constructor that creates a text given a String. 
     * 
     * @param original the string that you want to analyze. 
     */
    public Text(String original) throws NullPointerException {
        if (original == null) {
            throw new NullPointerException("Input string is not allowed to be null.");
        }
        
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
        sentences = new ArrayList<String>();
        sentences.addAll(Arrays.asList(clean.split("(?<!(Dr)|(Mr)|(Mrs)|(Ms)|(Esq)|([^\\.]\\.\\.))\\.($| )")));
        
        Map<String, Integer> AFINN = new HashMap<String, Integer>();
        Scanner n = new Scanner("AFINN-111.txt");
        String nextWord;
        int nextInt;
        while (n.hasNext()){
            nextWord = n.next();
            nextInt = Integer.parseInt(n.next());
            AFINN.put(nextWord, nextInt);
        }
        valenceMapping = AFINN;
    }
    
    public int getCharacterCount() {
    	return clean.length();
    }
    private int getValence(String word){
        if (valenceMapping.get(word) == null) return 0;
        return valenceMapping.get(word);
    }
    public double getAverageValence(){
        double sum = 0;
        for (String word : words)
            sum+=getValence(word);
        return sum/getWordCount();
    }
    public List<Integer> getValenceList(){
        List<Integer> k = new ArrayList<Integer>();
        for (String word : words)
            k.add(getValence(word));      
        return k;
    }

    public String getOriginal() {
        return original;
    }
    /**
     * 
     * @return The original text, with the links stripped out. 
     */
    public String getClean() {
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
    
    public double averageSentenceLength() {
        //Needs to force getWordCount to be a double because otherwise integer division
        //causes problems.
        return getWordCount()/(double) getSentenceCount();
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
