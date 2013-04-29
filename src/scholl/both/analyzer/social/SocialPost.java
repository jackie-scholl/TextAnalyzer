package scholl.both.analyzer.social;

import java.util.*;
import scholl.both.analyzer.text.Text;

public class SocialPost implements Comparable<SocialPost> {
    private final SimpleUser poster;
    private final long timestamp; // milliseconds since epoch
    private final Text text;
    private final SimpleUser mention;
    private final List<String> tags;

    public SocialPost(SimpleUser poster, long timestamp, String text, SimpleUser mention, List<String> tags) {
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
     * @return the poster
     */
    public SimpleUser getPoster() {
        return this.poster;
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return this.timestamp;
    }

    /**
     * @return the text
     */
    public Text getText() {
        return this.text;
    }

    /**
     * @return the mention
     */
    public SimpleUser getMention() {
        return this.mention;
    }

    public String[] getTags() {
        return tags.toArray(new String[]{});
    }
    
    public int compareTo(SocialPost other) {
        return Long.compare(timestamp, other.timestamp);
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
