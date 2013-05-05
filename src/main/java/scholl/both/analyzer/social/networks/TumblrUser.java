package scholl.both.analyzer.social.networks;

import scholl.both.analyzer.social.PostSet;
import scholl.both.analyzer.social.SocialUser;

import java.util.*;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.User;

public class TumblrUser implements SocialUser {
    private final JumblrClient client;
    private final Blog blog;
    
    public TumblrUser(Blog blog) {
        this.blog = blog;
        this.client = blog.getClient();
    }
    
    public TumblrUser(String blogName, JumblrClient client) {
        this.client = client;
        this.blog = client.blogInfo(blogName);
    }
    
    @Override
    public String getName() {
        return blog.getName();
    }
    
    /**
     * Returns the blog title
     * 
     * @return blog title
     * @see com.tumblr.jumblr.types.Blog#getTitle()
     */
    @Override
    public String getTitle() {
        return blog.getTitle();
    }
    
    /**
     * Returns the blog description
     * 
     * @return the blog description
     * @see com.tumblr.jumblr.types.Blog#getDescription()
     */
    @Override
    public String getDescription() {
        return blog.getDescription();
    }
    
    @Override
    public PostSet getPosts(int num) {
        return TumblrClient.getPosts(blog, new HashMap<String, Object>(), num);
    }
    
    @Override
    public int getPostCount() {
        return blog.getPostCount();
    }
    
    @Override
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
                Blog b = client.blogInfo(u.getName());
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
    @Override
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
        } else if (!blog.equals(other.blog))
            return false;
        return true;
    }
}
