package scholl.both.analyzer.social;

import scholl.both.analyzer.text.*;

import java.io.*;
import java.util.*;


public class SocialClient {
    public static void main (String[] args) {
        List<String> texts = new ArrayList<String>();
        
        if (args.length == 0) {
            String[] newArgs = {"hello world!", "hello", "First sentence. Second sentence.", "doopity doo baa baa."};
            args = newArgs;
        }
        
        if (args[0] == "-f" || args[0] == "--file") {
            // Rest of arguments are files.
            for (int i=0; i<args.length; i++) {
                File f = new File(args[i]);
                
                Reader is = null;
                try {
                    is = new BufferedReader(new FileReader(f));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                StringBuffer sb = new StringBuffer();
                int ch = 0;
                while (ch != -1) {
                    sb.append((char) ch);
                    try {
                        ch = is.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                texts.add(args[i]);
            }
        } else {
            for (String s : args) {
                texts.add(s);
            }
        }
        
        for (String str : texts) {
            Text t = new Text(str);
            System.out.printf("%d words, %d characters: %s%n", t.getWordCount(), t.getCharacterCount(), t.getOriginal());
        }
    }
}
