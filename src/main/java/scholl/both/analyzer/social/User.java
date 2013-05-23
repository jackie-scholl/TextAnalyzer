package scholl.both.analyzer.social;

import java.util.List;

/**
 * Represents a social user or blog
 * 
 * @author Jackson
 */
public interface User {
    
    /**
     * Get the name of the user/blog.
     * 
     * @return the name of the user/blog
     */
    public abstract String getName();
    
    /**
     * Get the title of the user or blog.
     * 
     * @return the name of the user/blog
     */
    public abstract String getTitle();
    
    /**
     * Get the description of the user or blog.
     * 
     * @return the description of the user/blog
     */
    public abstract String getDescription();
    
    /**
     * Get the last num posts.
     * 
     * @param num number of posts to return
     * @return the last num posts
     */
    public abstract PostSet getPosts(int num);
    
    /**
     * Get the number of posts.
     * 
     * @return the number of posts
     */
    public abstract int getPostCount();
    
    /**
     * Get a list of the user's followers.
     * 
     * @return the user's followers
     */
    public abstract List<User> getFollowers();
    
    /**
     * Returns the number of milliseconds since the epoch at the time of the more recent post to
     * this blog.
     * 
     * @return time last updated
     */
    public abstract long getLastUpdated();
    
    @Override
    public abstract int hashCode();
    
    @Override
    public abstract boolean equals(Object obj);
}
