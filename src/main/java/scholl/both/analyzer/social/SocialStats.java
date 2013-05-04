package scholl.both.analyzer.social;

import scholl.both.analyzer.social.networks.TumblrClient;

import java.io.*;
import java.util.*;

public class SocialStats {
    
    private static File outputFolder;
    
    static {
        outputFolder = new File("out");
        outputFolder.mkdir();
    }

    public static void doStats(SocialUser b, int count) {
        long blogStart = System.currentTimeMillis();
        
        PostSet ps = b.getPosts(count);
        
        File userFolder = new File(outputFolder, b.getName());
        userFolder.mkdir();
        getWordFrequencies(userFolder, ps);
        
        long blogEnd = System.currentTimeMillis();
        System.out.printf("Finished blog %s with %d posts - took %.3f seconds%n", b.getName(), ps.size(), (blogEnd-blogStart)/1000.0);
    }
    
    public static void getWordFrequencies(File userFolder, PostSet ps) {
        File wordFrequencies = new File(userFolder, "wordFrequencies.txt");
        
        PrintStream fileStream = null;
        try {
            wordFrequencies.createNewFile();
            fileStream = new PrintStream(wordFrequencies);
            
            fileStream.println(ps.getWordCount2().toString2());
            fileStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileStream != null) {
                fileStream.close();
            }
        }
        
    }

    static void tumlbrThing() throws IOException {
        long start = System.currentTimeMillis();
        
        TumblrClient tclient = new TumblrClient();
        tclient.authenticate();
        
        System.out.println(tclient.getAuthenticatedUser().getName());
        
        Set<SocialUser> blogs = tclient.getInterestingUsers();
        
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("reblog_info", "true");
        options.put("filter", "text");
        options.put("notes_info", "true");
        options.put("limit", "20");
        
        String[] blogNames = {"dataandphilosophy"};
        for (String blog : blogNames) {
            blogs.add(tclient.getUser(blog));
        }
        
        for (SocialUser b : blogs) {
            doStats(b, 10);
        }
        
        long end = System.currentTimeMillis();
        double timeTaken = (end-start)/1000.0;
        
        System.out.printf("Finished - took %.3f seconds", timeTaken);
    }
    
}
