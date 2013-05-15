package scholl.both.analyzer.social;

import scholl.both.analyzer.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainClient {
    public static final boolean THREADING = true;

    public static void main(String[] args) {
        List<String> texts = new ArrayList<String>();
        
        for (String s : args) {
            texts.add(s);
        }
        
        for (String str : texts) {
            Text t = new Text(str);
            System.out.printf("%d words, %d characters: %s%n", t.getWordCount(),
                    t.getCharacterCount(), t.getOriginal());
        }
        
        try {
            SocialStats.tumblrThing(100);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
