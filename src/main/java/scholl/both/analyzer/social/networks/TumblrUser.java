package scholl.both.analyzer.social.networks;

import scholl.both.analyzer.social.*;
import java.util.*;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.*;

public class TumblrUser implements SocialUser {
    private final JumblrClient client;
    private final Blog blog;
    
    public TumblrUser(Blog blog) {
        this.blog = blog;
        this.client = blog.getClient();
    }
    
    public TumblrUser(String blogName, JumblrClient client) {
        this.client = client;
        //System.out.printf("Blog name: %s%n", blogName);
        this.blog = client.blogInfo(blogName);
    }
    
    public String getName() {
        return blog.getName();
    }

    public PostSet getPosts(int num) {
        return TumblrClient.getPosts(blog, new HashMap<String, Object>(), num);
    }

    public Integer getPostCount() {
        return blog.getPostCount();
    }

    public List<User> followers() {
        return blog.followers();
    }
    
    public String toString() {
        return blog.toString();
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((blog == null) ? 0 : blog.hashCode());
        return result;
    }

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
