package scholl.both.analyzer.social;

import java.util.*;
import scholl.both.analyzer.text.Text;

/**
 * A social network-type post, with at least a poster, timestamp, and text. There can also be an arbitrary list of
 * string "tags" and a user that is "mentioned". The last one, especially, is very vague; see the 
 * {@link #getMention() getMention} method for more.
 * 
 * @author Jackson
 *
 */
public class SocialPost implements Comparable<SocialPost> {
    private final User poster;
    private final long timestamp; // milliseconds since epoch
    private final Text text;
    private final User mention;
    private final List<String> tags;

    public SocialPost(User poster, long timestamp, String text, User mention, List<String> tags) {
        this.poster = poster;
        this.timestamp = timestamp;
        this.text = new Text(text);
        this.mention = mention;
        this.tags = Collections.unmodifiableList(tags);
    }
    
    public SocialPost(SimpleUser poster, long timestamp, String text) {
        this(poster, timestamp, text, null, new ArrayList<String>());
    }
    
    public SocialPost(String poster, long timestamp, String text, String mention, String[] tags) {
        this(new SimpleUser(poster), timestamp, text, new SimpleUser(mention),
                Arrays.asList(tags));
    }
    
    /**
     * Returns the user who published this post.
     * @return the poster
     */
    public User getPoster() {
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
     * Return the text of the post, as a Text object.
     * 
     * @return the text
     */
    public Text getText() {
        return this.text;
    }

    /**
     * Return the "mentioned" user. As described above, this can have many different meanings. On tumblr, it is the person
     * that the post was reblogged from, if any. On Facebook, it is the "wall" on which it was posted. On Twitter, it may
     * have no meaning. This type is immutable and is sorted chronologically.
     * 
     * @return the mention
     */
    public User getMention() {
        return this.mention;
    }

    /**
     * Get the tags for this post.
     * @return the tags
     */
    public String[] getTags() {
        return tags.toArray(new String[]{});
    }
    
    /**
     * {@inheritDoc}
     * 
     * Compares by timestamp; the result is positive if this post is more recent, zero if they were published at the
     * same time, and negative if the other post was published first.
     */
    public int compareTo(SocialPost other) {
        return Long.compare(this.timestamp, other.timestamp);
    }

    @Override
    public String toString() {
        return "Post [poster=" + this.poster
                + ", timestamp=" + this.timestamp
                + ", post=\"" + this.text + "\""
                + ", mention=" + this.mention
                + ", tags=" + this.tags
                + "]";
    }
}
