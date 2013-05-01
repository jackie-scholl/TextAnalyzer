package scholl.both.analyzer.text;

import java.util.*;

import scholl.both.analyzer.util.Counter;

//TODO: Add Javadocs

/**
 * @author      Keller Scholl <Keller.scholl@gmail.com>
 * @version     0.3
 * @since       2013-04-30
 */
public class Text {
	
	//Sets up variables for later use.
    private final String original;
    private List<String> words;
    private List<String> sentences;
    
    /**
     * Method that creates a text given a String. 
     * @param The string that you want to analyze. 
     */
    public Text(String original) {
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
        /*int lastSentenceEnd = -1;
        for(int i=0; i < this.original.length(); i++)
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
        		sentences.add(original.substring(lastSentenceEnd,i));
        		lastSentenceEnd = i;
        	}
        }*/
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
    
    public List<String> getSentences() {
    	return sentences;
    }
    
    public int getSentenceCount() {
    	return sentences.size();
    }

    public int getCharacterCount() {
    	return original.length();
    }

    public double averageWordLength() {
        int size = words.size();
        double sumLength = 0;
        for (String word : words) {
            sumLength += word.length();
        }
        return sumLength / size;
    }
    
    public double averageSentenceLength() {
    	int size = sentences.size();
    	double sumLength = 0;
    	for (String sentence : sentences) {
    		sumLength += sentence.length();
    	}
    	return sumLength / size;
    }
    // public static double averageSentenceLength()
    
    // TODO: Letter counter
    public Counter<Character> getLetterCount2() {
        return null;
    }

    public Counter<String> getWordCount2() {
        Counter<String> counter = new Counter<String>();
        for (String w : words) {
            counter.add(w.toLowerCase());
        }
        counter.remove("");
        return counter;
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
