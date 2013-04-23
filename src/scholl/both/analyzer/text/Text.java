package scholl.both.analyzer.text;

import java.util.ArrayList;
import java.util.List;

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
