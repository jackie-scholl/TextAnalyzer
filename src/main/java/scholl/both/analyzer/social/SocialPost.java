package scholl.both.analyzer.social;

import scholl.both.analyzer.text.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A social network-type post, with at least a poster, timestamp, and text. There can also be an
 * arbitrary list of string "tags" and a user that is "mentioned". The last one, especially, is very
 * vague; see the {@link #getMention() getMention} method for more.
 * 
 * @author Jackson
 * 
 */
public class SocialPost extends Text implements Comparable<SocialPost> {
    private final SocialUser poster;
    private final long timestamp; // milliseconds since epoch
    private final SocialUser mention;
    private final List<String> tags;
    
    public SocialPost(SocialUser poster, long timestamp, String text, SocialUser mention,
            List<String> tags) {
        super(text);
        this.poster = poster;
        this.timestamp = timestamp;
        this.mention = mention;
        this.tags = Collections.unmodifiableList(tags);
    }
    
    public SocialPost(String text, SocialUser poster, long timestamp) {
        this(poster, timestamp, text, null, new ArrayList<String>());
    }
    
    public SocialPost(String text) {
        this(text, null, System.currentTimeMillis());
    }
    
    /**
     * Returns the user who published this post.
     * 
     * @return the poster
     */
    public SocialUser getPoster() {
        return this.poster;
    }
    
    /**
     * Returns the time at which this post was published, in milliseconds since the epoch.
     * 
     * @return the timestamp
     */
    public long getTimestamp() {
        return this.timestamp;
    }
    
    /**
     * Return the "mentioned" user. As described above, this can have many different meanings. On
     * tumblr, it is the person that the post was reblogged from, if any. On Facebook, it is the
     * "wall" on which it was posted. On Twitter, it may have no meaning. This type is immutable and
     * is sorted chronologically.
     * 
     * @return the mentioned user
     */
    public SocialUser getMention() {
        return this.mention;
    }
    
    /**
     * Get the tags for this post.
     * 
     * @return the tags
     */
    public String[] getTags() {
        return tags.toArray(new String[]{});
    }
    
    /**
     * {@inheritDoc}
     * 
     * Compares by timestamp; the result is positive if this post is more recent, zero if they were
     * published at the same time, and negative if the other post was published first.
     */
    public int compareTo(SocialPost other) {
        return new Long(this.timestamp).compareTo(other.timestamp);
    }
    
    @Override
    public String toString() {
        final int maxLen = 10;
        return String
                .format("SocialPost [poster=%s, timestamp=%s, mention=%s, tags=%s]",
                        this.poster,
                        this.timestamp,
                        this.mention,
                        this.tags != null ? this.tags.subList(0, Math.min(this.tags.size(), maxLen))
                                : null);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((this.mention == null) ? 0 : this.mention.hashCode());
        result = prime * result + ((this.poster == null) ? 0 : this.poster.hashCode());
        result = prime * result + ((this.tags == null) ? 0 : this.tags.hashCode());
        result = prime * result + (int) (this.timestamp ^ (this.timestamp >>> 32));
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        SocialPost other = (SocialPost) obj;
        if (this.mention == null) {
            if (other.mention != null)
                return false;
        } else if (!this.mention.equals(other.mention))
            return false;
        if (this.poster == null) {
            if (other.poster != null)
                return false;
        } else if (!this.poster.equals(other.poster))
            return false;
        if (this.tags == null) {
            if (other.tags != null)
                return false;
        } else if (!this.tags.equals(other.tags))
            return false;
        if (this.timestamp != other.timestamp)
            return false;
        return true;
    }
}
