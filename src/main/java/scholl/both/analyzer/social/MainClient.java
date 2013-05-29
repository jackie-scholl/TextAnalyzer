package scholl.both.analyzer.social;

import scholl.both.analyzer.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        
        tumblrThing();
    }
    
    private static void tumblrThing() {
        int count = 100;
        Set<String> users = new HashSet<String>();
        users.add("dataandphilosophy");
        users.add("florescam");
        users.add("b41779690b83f182acc67d6388c7bac9");
        
        try {
            System.out.println("Started analysis");
            long start = System.currentTimeMillis();
            SocialStats.tumblrAnalysis(users, count);
            long end = System.currentTimeMillis();
            double timeTaken = (end - start) / 1000.0;            
            System.out.printf("Finished - took %.3f seconds%n", timeTaken);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
