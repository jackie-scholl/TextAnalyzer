package scholl.both.analyzer.social;

import scholl.both.analyzer.social.networks.Client;
import scholl.both.analyzer.social.networks.TumblrClient;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

public class SocialStats implements Runnable {
    public static final int MAX_BLOGS = 10;
    private static File outputFolder;
    
    private final User b;
    private final int count;
    private final File userFolder;
    
    private double postsPerHour = 0.0;
    
    private SocialStats(User b, int count) {
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
    
    private void run(int retriesLeft) {
        try {
            long start = System.currentTimeMillis();
            
            PostSet ps = b.getPosts(count);
            
            getGeneral(ps);
            getWordFrequencies(ps);
            getLetterFrequencies(ps);
            
            long end = System.currentTimeMillis();
            System.out.printf("Finished blog %-40s with %3d posts (%-9.5g posts per hour) " +
                    "- took %.3f seconds%n", b.getName(), ps.size(), postsPerHour,
                    (end - start) / 1000.0);
        } catch (IOException e) {
            System.out.printf("Failed on user %s, folder %s.%n", b, userFolder);
            if (retriesLeft <= 0) {
                e.printStackTrace();
            } else {
                run(retriesLeft - 1);
            }
        }
    }
    
    private void getGeneral(PostSet ps) throws IOException {
        postsPerHour = 0.0;
        File general = new File(userFolder, "general.txt");
        
        PrintStream stream = null;
        general.createNewFile();
        stream = new PrintStream(general);

        
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
            stream.printf("Oldest post (surveyed): %tc%n", oldest);
            stream.printf("Difference: %.3g hours%n", timeDiffHours);
            stream.printf("Estimated post rate: %n");
            stream.printf("\t%.5g posts per hour%n", postsPerHour);
            stream.printf("\t%.5g posts per day%n", postsPerHour * 24.0);
        } else {
            stream.printf("Cannot get time data, there were no posts%n");
        }
        
        stream.printf("Letter frequencies: %n%s", ps.getLetterCount().toString2());
        stream.close();
    }

    private void getWordFrequencies(PostSet ps) throws IOException {
        File wordFrequencies = new File(userFolder, "wordFrequencies.txt");
        
        printToFile(wordFrequencies, ps.getWordCount().toString2());
    }
    
    private void printToFile(File f, String s) throws IOException {
        PrintStream stream = null;
        try {
            f.createNewFile();
            stream = new PrintStream(f);            
            stream.print(s);
            stream.close();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }
    
    private void getLetterFrequencies(PostSet ps) throws IOException {
        File wordFrequencies = new File(userFolder, "letterFrequencies.txt");
        
        PrintStream fileStream = null;
        try {
            wordFrequencies.createNewFile();
            fileStream = new PrintStream(wordFrequencies);
            
            fileStream.println(ps.getLetterCount().toString2());
            fileStream.close();
        } finally {
            if (fileStream != null) {
                fileStream.close();
            }
        }
    }
        
    public static void doStats(User b, int count) {
        SocialStats ss = new SocialStats(b, count);
        ss.run();
    }

    private static void runAnalysis(Set<User> users, int count) {
        outputFolder = new File("out");
        outputFolder.mkdir();
        
        for (User u : users) {
            doStats(u, count);
        }
    }
    
    private static void runAnalysis(Client c, Set<String> names, int count) {
        Set<User> users = new HashSet<User>();
        for (String name : names) {
            users.add(c.getUser(name));
        }
        runAnalysis(users, count);
    }
    
    private static void runAnalysis(Client c, int count) {
        runAnalysis(c.getInterestingUsers(), count);
    }
    
    public static Client getTumblrClient() throws IOException {
        return new TumblrClient("tumblr_credentials.json");
    }

    public static void tumblrAnalysis(Set<String> interesting, int count) throws IOException {
        runAnalysis(getTumblrClient(), interesting, count);
    }
    
    public static void tumblrAnalysis(int count) throws IOException {
        Client c = getTumblrClient();
        c.authenticate();
        runAnalysis(c, count);
    }
}
