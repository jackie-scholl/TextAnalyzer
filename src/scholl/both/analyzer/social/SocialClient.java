package scholl.both.analyzer.social;

import scholl.both.analyzer.text.*;

import java.io.*;
import java.util.*;


public class SocialClient {
    public static void main (String[] args) {
        List<String> texts = new ArrayList<String>();
        
        if (args.length == 0) {
            String[] newArgs = {"-f", "sample.txt"};
            args = newArgs;
        }
        
        if (args[0] == "-f" || args[0] == "--file") {
            // Rest of arguments are files.
            for (int i=0; i<args.length; i++) {
                File f = new File(args[i]);
                
                InputStreamReader is = null;
                try {
                    is = new InputStreamReader(new FileInputStream(f));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                texts.add(args[i]);
            }
        } else {
            for (String s : args) {
                texts.add(s);
            }
        }
        
        for (String str : texts) {
            //Text t = new Text(str);
        }
    }
}
