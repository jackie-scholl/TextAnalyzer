package scholl.both.analyzer.text;

import java.util.*;

import scholl.both.analyzer.util.Counter;

/**
 * @author      Keller Scholl <Keller.scholl@gmail.com>
 * @version     0.3
 * @since       2013-04-30
 */
public class Text {
	
	//Sets up variables for later use.
    private String original;
    private List<String> words;
    private List<String> sentences;
    
    /**
     * Method that creates a text given a String. 
     * @param original the string that you want to analyze. 
     */
    public Text(String original) {
        TextMaker(original);
    }
    /**
     * Takes in an array of strings, mashes them together. 
     * @param original the strings that you want to analyze. 
     */
    public Text(String[] originArr){
        String o = "";
        for (String s : originArr) {
            o.concat(s);
        }
        TextMaker(o);
    }
    
    private void TextMaker(String original){
        //Strips out links
        original = original.replaceAll("(\\w+://)?(\\w+\\.)+(\\w+)([\\w\\+\\?/\\\\=-]+)*", "");
        
        this.original = original;

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
        for(int i=3; i < this.original.length()-4; i++)
        {
        	if (original.charAt(i)=='.'){
        		//Covers titles
        		if (original.substring(i-3,  i).matches("(.Dr)|(.Mr)|(Mrs)|(.Ms)|(Esq)|")) continue;
        		//Cover ellipses and acronyms
        		else if (original.substring(i-3,  i).matches("(\\p{Upper}\\.\\p{Upper})|(.\\.\\.)")) continue;
        		else if (original.substring(i+1,i+3).matches("(\\p{Upper}\\.)|(\\.\\.)")) continue;
        		else if (original.substring(i-1,i+2).matches("\\.{3}")) continue;
        		//If nothing else has forced the for loop to skip past this area, it will add everything from
        		//the last sentence ending up to this one. 
        		sentences.add(original.substring(lastSentenceEnd+1,i));
        		lastSentenceEnd = i;
        	}
        	if (original.charAt(i)=='?'){
        		sentences.add(original.substring(lastSentenceEnd+1,i));
        		lastSentenceEnd = i;
        	}
        }
        //Ensures that a sentence is always ended. 
        sentences.add(original.substring(lastSentenceEnd+1));
    }
    
    public int getCharacterCount() {
    	return original.length();
    }

    public String getOriginal() {
        return original;
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

    public List<String> getSentences() {
    	return sentences;
    }
    
    public int getSentenceCount() {
    	return sentences.size();
    }

    //Needs to force getWordCount to be a double because otherwise integer division
    //causes problems.
    public double averageSentenceLength() {
        return getSentenceCount()/(double)getWordCount();
    }
    public long getCharCount(char desired){
        return getLetterCount2().get(desired);
    }
    public Counter<Character> getLetterCount2() {
        Counter<Character> count = new Counter<Character>();
        for (char cha : original.toCharArray()) {
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
        return original;
    }
}


//Map<String, Integer> m = new HashMap<>();
//w = w.replaceAll("[\\.\"']", "").toLowerCase();
/*w = w.toLowerCase();
Integer count = m.get(w);
if (count == null) {
    count = 0;
}
count++;
m.put(w, count);*/

/*Set<String> toRemove = new HashSet<String>();
for (String k : m.keySet()) {
    if (k.charAt(k.length()-1) == ':') {
        toRemove.add(k);
    }
}
for (String k : toRemove) {
    m.remove(k);
}*/
