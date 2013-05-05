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
    
    static void tumlbrThing() throws IOException {
        TumblrClient tclient = new TumblrClient();
        tclient.authenticate();
        
        long start = System.currentTimeMillis();
        
        System.out.println(tclient.getAuthenticatedUser().getName());
        
        Set<SocialUser> blogs = tclient.getInterestingUsers();
        
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("reblog_info", "true");
        options.put("filter", "text");
        options.put("notes_info", "true");
        options.put("limit", "20");
        
        String[] blogNames = { "dataandphilosophy" };
        for (String blog : blogNames) {
            blogs.add(tclient.getUser(blog));
        }
        
        List<Thread> threads = new ArrayList<Thread>();
        for (SocialUser b : blogs) {
            if (SocialClient.THREADING) {
                Thread t = new Thread(new StatsDoer(b, 40));
                t.start();
                threads.add(t);
            } else {
                doStats(b, 10);
            }
        }
        
        try {
            for (Thread t : threads) {
                t.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        long end = System.currentTimeMillis();
        double timeTaken = (end - start) / 1000.0;
        
        System.out.printf("Finished - took %.3f seconds", timeTaken);
    }
    
    private static class StatsDoer implements Runnable {
        private final SocialUser b;
        private final int count;
        
        public StatsDoer(SocialUser b, int count) {
            this.b = b;
            this.count = count;
        }
        
        public void run() {
            doStats(b, count);
        }
    }
    
    public static void doStats(SocialUser b, int count) {
        long start = System.currentTimeMillis();
        
        PostSet ps = b.getPosts(count);
        
        File userFolder = new File(outputFolder, b.getName());
        delete(userFolder);
        userFolder.mkdir();
        getWordFrequencies(userFolder, ps);
        getGeneral(userFolder, ps, b);
        
        long blogEnd = System.currentTimeMillis();
        System.out.printf("Finished blog %s with %d posts - took %.3f seconds%n", b.getName(),
                ps.size(), (blogEnd - start) / 1000.0);
    }
    
    private static void delete(File f) {
        if (f == null)
            return;
        if (f.isDirectory()) {
            for (File f2 : f.listFiles()) {
                delete(f2);
            }
        }
        f.delete();
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
    
    public static void getGeneral(File userFolder, PostSet ps, SocialUser u) {
        File general = new File(userFolder, "general.txt");
        
        PrintStream stream = null;
        try {
            general.createNewFile();
            stream = new PrintStream(general);
            
            stream.printf("Name: %s%n", u.getName());
            stream.printf("Title: %s%n", u.getTitle());
            stream.printf("Description: %s%n", u.getDescription());
            stream.printf("Number of posts: %d%n", u.getPostCount());
            stream.printf("Most recent post: %tc%n", ps.size() == 0? 0L : ps.getMostRecent().getTimestamp());
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }
    
}
