package scholl.both.analyzer.text;

import java.util.*;

public class TextAnalyzer {
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
    
}
