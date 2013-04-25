package scholl.both.analyzer.text;

import java.util.ArrayList;
import java.util.List;

public class Text {
    private final String original;
    private List<String> words;
    private List<String> sentences;

    public Text(String original) {
        this.original = original;

        words = new ArrayList<String>();
        String[] wordsArr = original.split("\\s");
        for (String str : wordsArr) {
            words.add(str);
        }
        //Want to go through the text until I find a sentence ending mark, and then add everything between
        //that mark and the last mark to the list of sentences.
        //int lastSentenceEnd = -1;
        //for(int i=0; i < this.original.length(); i++)
        //{
        //	if original.charAt(i)
        //}
        
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

    public double averageWordLength() {
        int size = words.size();
        double sumLength = 0;
        for (String word : words) {
            sumLength += word.length();
        }
        return sumLength / size;
    }
    // public static double averageSentenceLength()
}
