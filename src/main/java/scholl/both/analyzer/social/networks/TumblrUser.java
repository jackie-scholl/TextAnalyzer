package scholl.both.analyzer.social.networks;

import scholl.both.analyzer.social.*;

import java.util.*;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.*;

public class TumblrUser implements SocialUser {
    private final TumblrClient tclient;
    private final JumblrClient jclient;
    private final Blog blog;
    
    public TumblrUser(Blog blog, JumblrClient jclient, TumblrClient tclient) {
        this.blog = blog;
        this.jclient = jclient;
        this.tclient = tclient;
    }
    
    public TumblrUser(Blog blog) {
        this(blog, blog.getClient(), null);
    }
    
    public TumblrUser(String blogName, JumblrClient client) {
        this(client.blogInfo(blogName), client, null);
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
    
    public PostSet getPosts(int num) {
        return getPosts(new HashMap<String, Object>(), num);
    }
    
    public PostSet getPosts(Map<String, Object> options, int num) {
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
    
    public int getPostCount() {
        return blog.getPostCount();
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
        List<SocialUser> followers = new ArrayList<SocialUser>();
        
        int lim = 20;
        lim = num > lim ? lim : num;
        
        int initialOffset = 0;
        for (int i = initialOffset; i < num; i += lim) {
            options.put("limit", lim);
            options.put("offset", i);
            for (User u : blog.followers(options)) {
                Blog b = jclient.blogInfo(u.getName());
                SocialUser su = new TumblrUser(b);
                followers.add(su);
            }
        }
        
        return followers;
    }
    
    public static List<User> getFollowers(Blog b, Map<String, Object> options, int num) {
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
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see com.tumblr.jumblr.types.Blog#getUpdated()
     */
    public long getLastUpdated() {
        return 1000L * blog.getUpdated();
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
