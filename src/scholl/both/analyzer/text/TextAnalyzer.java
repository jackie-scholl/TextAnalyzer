package scholl.both.analyzer.text;

import java.util.*;

public class TextAnalyzer {
    public static void main (String[] args) {
        if (args.length == 0) {
            String[] newArgs = {"hello", "hello world", "this is a simple test"};
            args = newArgs;
        }
        
        for (String s : args) {
            Text t = new Text(s);
            System.out.printf("%s: %d words, %d characters %n", t.getOriginal(), t.getWordCount(), t.getNumCharacters());
        }
    }
}

class Text {
    private final String original;
    private List<String> words;
    
    public Text(String original) {
        this.original = original;
        
        words = new ArrayList<String>();
        String[] wordsArr = original.split("\\s");
        for (String w : wordsArr) {
            words.add(w);
        }
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
    
    public int getNumCharacters() {
        int sum = 0;
        for (String w : words) {
            sum += w.length();
        }
        
        return sum;
    }    
}