package scholl.both.analyzer.social;

import java.util.List;

//TODO: Add Javadocs

public interface SocialUser {

    public abstract String getName();
    
    public abstract String getTitle();
    
    public abstract String getDescription();
    
    public abstract PostSet getPosts(int num);
    
    public abstract int getPostCount();
    
    public abstract List<SocialUser> getFollowers();
    
    /**
     * Returns the number of milliseconds since the epoch at the time of the more recent post to
     * this blog.
     * 
     * @return time last updated
     */
    public abstract long getLastUpdated();

    public abstract int hashCode();

    public abstract boolean equals(Object obj);

}
