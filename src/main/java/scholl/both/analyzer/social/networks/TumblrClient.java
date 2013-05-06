package scholl.both.analyzer.social.networks;

import scholl.both.analyzer.social.*;

import java.io.*;
import java.util.*;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TumblrApi;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.*;

public class TumblrClient {
    private JumblrClient client;
    private String consumerKey, consumerSecret;
    
    /*public TumblrClient() {
        this.client = new JumblrClient();
    }*/
    
    /*public TumblrClient(JumblrClient client) {
        this.client = client;
        this.consumerKey = client.
    }*/

    public TumblrClient() throws IOException {
        this("credentials.json");
    }
    
    public TumblrClient(String credentialsFileName) throws IOException {
        this(new File(credentialsFileName));
    }
    
    public TumblrClient(File credentialsFile) throws IOException {
        // Read in the JSON data for the credentials
        BufferedReader br = new BufferedReader(new FileReader(credentialsFile));
        String json = "";
        while (br.ready()) {
            json += br.readLine();
        }
        br.close();
        
        // Parse the credentials
        //JsonParser parser = new JsonParser();
        JsonObject obj = (JsonObject) (new JsonParser()).parse(json);
        
        consumerKey = obj.getAsJsonPrimitive("consumer_key").getAsString();
        consumerSecret = obj.getAsJsonPrimitive("consumer_secret").getAsString();
        
        // Create a client
        client = new JumblrClient(consumerKey, consumerSecret);
    }
    
    public TumblrClient(String consumerKey, String consumerSecret) {
        client = new JumblrClient(consumerKey, consumerSecret);
    }
    
    public void authenticate() throws IOException {
        OAuthService service = new ServiceBuilder().provider(TumblrApi.class).apiKey(consumerKey)
                .apiSecret(consumerSecret).build();
        Token request = service.getRequestToken();
        
        String url = service.getAuthorizationUrl(request);
        SocialClient.openBrowser(url);
        
        OAuthCallbackServer s = new OAuthCallbackServer(8004);
        String response = s.getVerifier();
        
        Token access = service.getAccessToken(request, new Verifier(response));
        client.setToken(access.getToken(), access.getSecret());
    }
    
    public JumblrClient getJumblr() {
        return client;
    }
    
    public SocialUser getAuthenticatedUser() {
        return new TumblrUser(client.blogInfo(client.user().getName()));
    }
    
    public Set<SocialUser> getInterestingUsers() {
        Set<SocialUser> s = new HashSet<SocialUser>();
        
        User authenticated = client.user();
        
        System.out.printf("User %s has these blogs: %n", authenticated.getName());
        for (Blog b : authenticated.getBlogs()) {
            SocialUser blog = new TumblrUser(b);
            s.add(blog);
            
            List<SocialUser> followers = blog.getFollowers();
            System.out.printf("\t%s (%s) has these %d followers:%n", b.getName(), b.getTitle(),
                    followers.size());
            
            for (SocialUser follower : followers) {
                System.out.printf("\t\t%s%n", follower.getName());
                s.add(follower);
            }
        }
        
        List<SocialUser> following = getFollowing(100);
        System.out.printf("User %s is following these %d blogs: %n", authenticated.getName(),
                following.size());
        for (SocialUser b : following) {
            System.out.printf("\t%s (%s)%n", b.getName(), b.getTitle());
            s.add(b);
        }
        
        String[] otherBlogs = new String[] {"dataandphilosophy", "old-shoes-for-new-feet"};
        for (String str : otherBlogs) {
            s.add(new TumblrUser(str, client));
        }
        
        System.out.println();
        
        return s;
    }
    
    public SocialUser getUser(Blog b) {
        return new TumblrUser(b);
    }
    
    public SocialUser getUser(String b) {
        return getUser(client.blogInfo(b));
    }
    
    public List<SocialUser> getFollowing(Map<String, Object> options, int num) {
        List<Blog> blogs = new ArrayList<Blog>();
        List<SocialUser> following = new ArrayList<SocialUser>();
        
        int lim = 20;
        lim = num > lim ? lim : num;
        
        int initialOffset = 0;
        for (int i = initialOffset; i < num; i += lim) {
            options.put("limit", lim);
            options.put("offset", i);
            for (Blog b : client.userFollowing(options)) {
                following.add(new TumblrUser(b));
            }
            blogs.addAll(client.userFollowing(options));
        }
        
        return following;
    }
    
    public List<SocialUser> getFollowing(int num) {
        return getFollowing(new HashMap<String, Object>(), num);
    }
    
    public static SocialPost getSocialPost(Post p) {
        SocialUser u = new TumblrUser(p.getBlogName(), p.getClient());
        
        String type = p.getType();
        String text = "unknown";
        if (type.equals("text")) {
            assert p instanceof TextPost;
            text = ((TextPost) p).getBody();
        } else if (type.equals("photo")) {
            assert p instanceof PhotoPost;
            text = ((PhotoPost) p).getCaption();
        } else if (type.equals("quote")) {
            assert p instanceof QuotePost;
            text = ((QuotePost) p).getText();
        } else if (type.equals("link")) {
            assert p instanceof LinkPost;
            text = ((LinkPost) p).getDescription();
        }
        
        return new SocialPost(u, p.getTimestamp()*1000L, text, null, p.getTags());
    }
    
    public PostSet getPosts(Blog blog, Map<String, Object> options, int num) {
        PostSet ps = new PostSet();
        
        int lim = 20;
        lim = num > lim ? lim : num;
        
        List<Thread> threads = new ArrayList<Thread>();
        int initialOffset = 0;
        for (int i = initialOffset; i < num; i += lim) {
            int diff = num - i;
            diff = diff > lim ? lim : diff;
            options.put("limit", lim);
            options.put("offset", i);
            
            PostGetter pg = new PostGetter(blog, options, ps);
            
            if (SocialClient.THREADING){ 
                Thread t = new Thread(pg);
                t.start();
                threads.add(t);
            } else {
                pg.run();
            }
            
        }
        
        try {
            for (Thread t : threads) {
                t.join();
            }
        } catch (InterruptedException e) {
            System.err.println("Interrupted while waiting for worker threads of getPosts to finish.");
            e.printStackTrace();
        }
        
        return ps;
    }
    
    private static class PostGetter implements Runnable {
        private final Map<String, Object> options;
        private volatile PostSet ps;
        private final Blog blog;
        
        public PostGetter(Blog blog, Map<String, Object> options, PostSet ps) {
            this.options = new HashMap<String, Object>(options);
            this.ps = ps;
            this.blog = blog;
        }
        
        public void run() {
            List<Post> posts = blog.posts(options);
            for (Post p : posts) {
                synchronized (ps) {
                    ps.add(TumblrClient.getSocialPost(p));
                }
            }
        }
    }
}

/*





List<User> followers = TumblrUser.getFollowers(b, new HashMap<String, Object>(), 100);
System.out.printf("\t%s (%s) has these %d followers:%n", b.getName(), b.getTitle(),
        followers.size());

for (User u2 : followers) {
    System.out.printf("\t\t%s%n", u2.getName());
    Blog b3 = client.blogInfo(u2.getName());
    s.add(new TumblrUser(b3));
}
/*public static List<Blog> getFollowing(JumblrClient c, Map<String, Object> options, int num) {
        
        /*List<Blog> blogs = new ArrayList<Blog>();
        
        int lim = 20;
        lim = num > lim ? lim : num;
        
        int initialOffset = 0;
        for (int i = initialOffset; i < num; i += lim) {
            options.put("limit", lim);
            options.put("offset", i);
            blogs.addAll(c.userFollowing(options));
        }
        
        return blogs;
    }
    


*/
