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
    
    public final static boolean THREADING = true;
    private JumblrClient client;
    private OAuthService service;

    public TumblrClient(String credentialsFileName) throws IOException {
        this(new File(credentialsFileName));
    }
    
    public TumblrClient(File credentialsFile) throws IOException {
        this(readCredential(credentialsFile));
    }
    
    private TumblrClient(JsonObject obj) {
        this(obj.getAsJsonPrimitive("consumer_key").getAsString(),
                obj.getAsJsonPrimitive("consumer_secret").getAsString());
    }
    
    public TumblrClient(String consumerKey, String consumerSecret) {
        this.client = new JumblrClient(consumerKey, consumerSecret);
        service = new ServiceBuilder().provider(TumblrApi.class).apiKey(consumerKey)
                .apiSecret(consumerSecret).build();
    }
    
    public static JsonObject readCredential(File credentialsFile) throws IOException {
        // Read in the JSON data for the credentials
        BufferedReader br = new BufferedReader(new FileReader(credentialsFile));
        String json = "";
        while (br.ready()) {
            json += br.readLine();
        }
        br.close();
        
        // Parse the credentials
        JsonObject obj = (JsonObject) (new JsonParser()).parse(json);
        
        return obj;
    }
    
    public void authenticate() throws IOException {
        assert service != null;
        
        Token request = service.getRequestToken();
        System.out.println(request);
        
        String url = service.getAuthorizationUrl(request);
        MainClient.openBrowser(url);
        
        OAuthCallbackServer s = new OAuthCallbackServer(8004);
        String response = s.getVerifier();
        
        Token access = service.getAccessToken(request, new Verifier(response));
        System.out.printf("Access token: %s%n", access);
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
        
        com.tumblr.jumblr.types.User authenticated = client.user();
        
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
            s.add(new TumblrUser(str));
        }
        
        System.out.println();
        
        return s;
    }
    
    private SocialUser getUser(Blog b) {
        return new TumblrUser(b);
    }
    
    public SocialUser getUser(String b) {
        return getUser(client.blogInfo(b));
    }
    
    public List<SocialUser> getFollowing(int num) {
        return getFollowing(new HashMap<String, Object>(), num);
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
    
    private SocialPost getSocialPost(Post p) {
        SocialUser u = new TumblrUser(p.getBlogName());
        
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
    
    private PostSet getPosts(Blog blog, Map<String, Object> options, int num) {
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
            
            if (THREADING){
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
    
    private List<SocialUser> getFollowers(Blog blog, Map<String, Object> options, int num) {
        List<SocialUser> followers = new ArrayList<SocialUser>();
        
        int lim = 20;
        lim = num > lim ? lim : num;
        
        int initialOffset = 0;
        for (int i = initialOffset; i < num; i += lim) {
            options.put("limit", lim);
            options.put("offset", i);
            for (com.tumblr.jumblr.types.User u : blog.followers(options)) {
                Blog b = client.blogInfo(u.getName());
                SocialUser su = new TumblrUser(b);
                followers.add(su);
            }
        }
        
        return followers;
    }
    
    private class PostGetter implements Runnable {
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
                    ps.add(getSocialPost(p));
                }
            }
        }
    }
    
    class TumblrUser implements SocialUser {
        private final Blog blog;
        
        public TumblrUser(Blog blog) {
            this.blog = blog;
        }
        
        public TumblrUser(String blogName) {
            this(client.blogInfo(blogName));
        }
        
        public String getName() {
            return blog.getName();
        }
        
        /**
         * Returns the blog title
         * 
         * @return blog title
         * @see com.tumblr.jumblr.types.Blog#getTitle()
         */
        public String getTitle() {
            return blog.getTitle();
        }
        
        /**
         * Returns the blog description
         * 
         * @return the blog description
         * @see com.tumblr.jumblr.types.Blog#getDescription()
         */
        public String getDescription() {
            return blog.getDescription();
        }
        
        /**
         * {@inheritDoc}
         * 
         * @see com.tumblr.jumblr.types.Blog#getUpdated()
         */
        public long getLastUpdated() {
            return 1000L * blog.getUpdated();
        }

        public int getPostCount() {
            return blog.getPostCount();
        }

        public PostSet getPosts(int num) {
            return getPosts(new HashMap<String, Object>(), num);
        }
        
        public PostSet getPosts(Map<String, Object> options, int num) {
            return TumblrClient.this.getPosts(blog, options, num);
        }
        
        public List<SocialUser> getFollowers() {
            return getFollowers(new HashMap<String, Object>(), 100);
        }
        
        /**
         * Get the list of followers with the options.
         * 
         * @param options
         * @return the followers of this blog
         * @see com.tumblr.jumblr.types.Blog#followers(java.util.Map)
         */
        public List<SocialUser> getFollowers(Map<String, Object> options, int num) {
            return TumblrClient.this.getFollowers(blog, options, num);
        }
        
        @Override
        public String toString() {
            return blog.toString();
        }
        
        public int compareTo(SocialUser other) {
            return this.getName().compareTo(other.getName());
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((blog == null) ? 0 : blog.hashCode());
            return result;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            TumblrUser other = (TumblrUser) obj;
            if (blog == null) {
                if (other.blog != null)
                    return false;
            } else if (!getName().equals(other.getName()))
                return false;
            return true;
        }
    }

}



/*public static List<User> getFollowers(Blog b, Map<String, Object> options, int num) {
    List<User> followers = new ArrayList<User>();
    
    int lim = 20;
    lim = num > lim ? lim : num;
    
    int initialOffset = 0;
    for (int i = initialOffset; i < num; i += lim) {
        options.put("limit", lim);
        options.put("offset", i);
        followers.addAll(b.followers(options));
    }
    
    return followers;
}*/
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


/*private static class PostGetter implements Runnable {
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
}*/