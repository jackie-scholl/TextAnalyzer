package scholl.both.analyzer.social;

import scholl.both.analyzer.social.networks.TumblrClient;

import java.io.*;
import java.util.*;

public class SocialStats implements Runnable {
    public static final int MAX_BLOGS = 10;
    
    private static File outputFolder;
    static {
        outputFolder = new File("out");
        outputFolder.mkdir();
    }

    private final SocialUser b;
    private final int count;
    private final File userFolder;
    
    private double postsPerHour = 0.0;

    public SocialStats(SocialUser b, int count) {
        this.b = b;
        this.count = count;
        userFolder = new File(outputFolder, b.getName());
        delete(userFolder);
        userFolder.mkdir();
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
        if (f.exists()) {
            System.err.printf("Something went wrong - file %s still exists%n", f);
        }
    }

    public void run() {
        run(5);
    }
    
    public void run(int retriesLeft) {
        try {
            long start = System.currentTimeMillis();
            
            PostSet ps = b.getPosts(count);
        
            getWordFrequencies(ps);
            getGeneral(ps);
            
            long end = System.currentTimeMillis();
            System.out.printf("Finished blog %-40s with %3d posts (%-9.5g posts per hour) " +
                    "- took %.3f seconds%n", b.getName(), ps.size(), postsPerHour,
                    (end - start) / 1000.0);
        } catch (IOException e) {
            System.err.printf("Failed on user %s, folder %s.%n", b, userFolder);
            if (retriesLeft <= 0) {
                e.printStackTrace();
            } else {
                run(retriesLeft-1);
            }
        }
    }
    
    public static void doStats(SocialUser b, int count) {
        SocialStats ss = new SocialStats(b, count);
        ss.run();
    }
    
    public void getWordFrequencies(PostSet ps) throws IOException {
        File wordFrequencies = new File(userFolder, "wordFrequencies.txt");
        
        PrintStream fileStream = null;
        try {
            wordFrequencies.createNewFile();
            fileStream = new PrintStream(wordFrequencies);
            
            fileStream.println(ps.getWordCount2().toString2());
            fileStream.close();
        } finally {
            if (fileStream != null) {
                fileStream.close();
            }
        }
    }
    
    public void getGeneral(PostSet ps) throws IOException {
        postsPerHour = 0.0;
        File general = new File(userFolder, "general.txt");
        
        PrintStream stream = null;
        try {
            general.createNewFile();
            stream = new PrintStream(general);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        
        stream.printf("Name: %s%n", b.getName());
        stream.printf("Title: %s%n", b.getTitle());
        stream.printf("Description: %s%n", b.getDescription());
        stream.printf("Number of posts (total): %d%n", b.getPostCount());
        stream.printf("Number of posts (surveyed): %d%n", ps.size());
        
        if (ps.size() > 0) {
            long mostRecent = ps.getMostRecent().getTimestamp();
            long oldest = ps.getOldest().getTimestamp();
            double timeDiffHours = (mostRecent - oldest) / (1000.0 * 60.0 * 60.0);
            postsPerHour = ps.size() / timeDiffHours;
            
            stream.printf("Most recent post: %tc%n", mostRecent);
            stream.printf("Oldest post: %tc%n", oldest);
            stream.printf("Difference: %.3g hours%n", timeDiffHours);
            stream.printf("Estimated post rate: %n");
            stream.printf("\t%.5g posts per hour%n", postsPerHour);
            stream.printf("\t%.5g posts per day%n", postsPerHour * 24.0);
        } else {
            stream.printf("Cannot get time data, there were no posts%n");
        }
        
        stream.printf("Letter frequencies: %n%s", ps.getLetterCount2().toString2());
        stream.close();
    }

    
    static void tumlbrThing(int COUNT) throws IOException {
        TumblrClient tclient = new TumblrClient("tumblr_credentials.json");
        tclient.authenticate();
        
        long start = System.currentTimeMillis();
        System.out.println(tclient.getAuthenticatedUser().getName());
        
        Set<SocialUser> blogs = tclient.getInterestingUsers();
        
        Map<String, Object> options = new HashMap<String, Object>();
        //options.put("reblog_info", "true");
        options.put("filter", "text");
        //options.put("notes_info", "true");
        options.put("limit", "20");
        
        List<Thread> threads = new ArrayList<Thread>();
        int i = 0;
        for (SocialUser b : blogs) {
            if (MainClient.THREADING) {
                Thread t = new Thread(new SocialStats(b, COUNT));
                t.start();
                threads.add(t);
            } else {
                doStats(b, 10);
            }
            i++;
            if (i > MAX_BLOGS) {
                break;
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
        
        System.out.printf("Finished - took %.3f seconds%n", timeTaken);
    }
    
}
