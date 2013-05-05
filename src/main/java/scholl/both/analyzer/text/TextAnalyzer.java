package scholl.both.analyzer.text;

import java.util.*;
import scholl.both.analyzer.social.PostSet;
import scholl.both.analyzer.social.SocialPost;
import scholl.both.analyzer.text.Text;

public class TextAnalyzer {
    SocialPost[] posts;
    /*
     * Main method is a basic test if given no arguments
     * If it is given arguments, it returns the text along 
     * with the number of words and the number of characters in each.
     * @param some number of strings to analyze
     */
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
    /*
     * Makes an instance of TextAnalyzer with the given posts.
     */
    TextAnalyzer(PostSet p){
        posts = p.getPosts();
    }
    
}
