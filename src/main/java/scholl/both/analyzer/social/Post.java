package scholl.both.analyzer.social;

import scholl.both.analyzer.text.Text;

import java.util.*;

/**
 * A social network-type post, with at least a poster, timestamp, and text. There can also be an
 * arbitrary list of string "tags" and a user that is "mentioned". The last one, especially, is very
 * vague; see the {@link #getMention() getMention} method for more. This type is immutable. It is
 * sorted chronologically.
 * 
 * @author Jackson
 * 
 */
public class Post extends Text implements Comparable<Post> {
    private final long timestamp; // milliseconds since epoch
    private final User poster;
    private final User mention;
    private final List<String> tags;
    
    /**
     * Makes a new SocialPost.
     * 
     * @param text the text of the post
     * @param timestamp the time at which this was posted, in milliseconds; defaults to current time
     * @param poster the user who posted this; defaults to {@code null}
     * @param mention the user mentioned in this post; defaults to {@code null}
     * @param tags any tags associated with this post; defaults to empty
     * 
     * @throws NullPointerException if tags or text is null
     */
    public Post(String text, long timestamp, User poster, User mention,
            List<String> tags) throws NullPointerException {
        super(text);
        
        if (tags == null) {
            throw new NullPointerException("The list of tags is not allowed to be null.");
        }
        
        this.poster = poster;
        this.timestamp = timestamp;
        this.mention = mention;
        this.tags = Collections.unmodifiableList(tags);
    }
    
    /**
     * Makes a new SocialPost. The mentioned user is defaulted to null, and tags defaults to an
     * empty list.
     * 
     * @param text the text of the post
     * @param timestamp the time at which this was posted, in milliseconds
     * @param poster the user who posted this
     */
    public Post(String text, long timestamp, User poster) {
        this(text, timestamp, poster, null, new ArrayList<String>());
    }
    
    /**
     * Makes a new SocialPost. The mentioned user is defaulted to null, the posting user defaults to
     * null, the timestamp defaults to the current time, and tags defaults to an empty list.
     * 
     * @param text the text of the post
     */
    public Post(String text) {
        this(text, System.currentTimeMillis(), null);
    }
    
    public Post(Post other) {
        this(other.getOriginal(), other.getTimestamp(), other.getPoster(), other.getMention(), 
                other.getTags());
    }
    
    /**
     * Returns the user who published this post.
     * 
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
     * Returns the time at which this post was published, as a Calendar.
     * 
     * @return the post time
     */
    public Calendar getCalendar() {
        return getCalendar(TimeZone.getDefault());
    }
    
    /**
     * Returns the time at which this post was published, as a Calendar.
     * 
     * @param tz the timezone to set the Calendar in
     * @return the post time
     */
    public Calendar getCalendar(TimeZone tz) {
        return getCalendar(tz, Locale.getDefault());
    }
    
    /**
     * Returns the time at which this post was published, as a Calendar.
     * 
     * @param timezone the timezone to the the Calendar in
     * @param locale the locale to set the Calendar in
     * 
     * @return the post time
     */
    public Calendar getCalendar(TimeZone timezone, Locale locale) {
        Calendar c = Calendar.getInstance(timezone, locale);
        c.setTimeInMillis(getTimestamp());
        return c;
    }
    
    /**
     * Return the "mentioned" user. As described above, this can have many different meanings. On
     * tumblr, it is the person that the post was reblogged from, if any. On Facebook, it is the
     * "wall" on which it was posted. On Twitter, it may have no meaning.
     * 
     * @return the mentioned user
     */
    public User getMention() {
        return this.mention;
    }
    
    /**
     * Get the tags for this post.
     * 
     * @return the tags
     */
    public List<String> getTags() {
        return new ArrayList<String>(tags);
    }
        
    /**
     * {@inheritDoc}
     * 
     * Compares by timestamp; the result is positive if this post is more recent, zero if they were
     * published at the same time, and negative if the other post was published first.
     */
    public int compareTo(Post other) {
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
        Post other = (Post) obj;
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
