package scholl.both.analyzer.social;

import scholl.both.analyzer.social.networks.Client;
import scholl.both.analyzer.social.networks.TumblrClient;
import scholl.both.analyzer.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainClient {
    public static final boolean THREADING = true;

    public static void main(String[] args) throws IOException {
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
        graph();
    }
    
    private static void tumblrThing() throws IOException {
        int count = 10;
        Set<String> users = new HashSet<String>();
        
        /*Client c = new TumblrClient();
        c.authenticate();
        Set<User> interestingUsers = c.getInterestingUsers();
        for (User u : interestingUsers) {
            users.add(u.getName());
        }*/
        
        users.add("murder-by-death");
        
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
    
    private static void graph() throws IOException {
        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec("Rscript src/main/R/hoursDaysGrapher.R");
        
    }
}
