package scholl.both.analyzer.text;
import java.util.*;

public class TextAnalyzer {
    public static void main (String[] args) {
        if (args.length == 0) {
            String[] newArgs = {"hello", "hello world", "this is a simple test"};
            args = newArgs;
        }
        
        for (String str : args) {
            Text t = new Text(str);
            System.out.printf("%s: %d words, %d characters %n", t.getOriginal(), t.getWordCount(), t.getCharacterCount());
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
        for (String str : wordsArr) {
            words.add(str);
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
    
    public int getCharacterCount() {
        int sum = 0;
        for (String str : words) {
            sum += str.length();
        }
        
        return sum;
    }    
}