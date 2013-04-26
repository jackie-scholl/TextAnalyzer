package scholl.both.analyzer.social;

import java.util.Set;
import scholl.both.analyzer.text.Text;

public class Post {
    private final User poster;
    private final long timestamp; // milliseconds since epoch
    private final Text post;
    private final User mention;
    private final Set<String> tags;

    public Post(User poster, long timestamp, String post, User mention, Set<String> tags) {
        this.poster = poster;
        this.timestamp = timestamp;
        this.post = new Text(post);
        this.mention = mention;
        this.tags = tags;
    }
    
    public User getPoster() {
        return poster;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Text getPost() {
        return post;
    }

    public User getMention() {
        return mention;
    }

    public String[] getTags() {
        return tags.toArray(new String[]{});
    }

    public String toString() {
        return "Post [poster=" + poster + ", timestamp=" + timestamp + ", post=" + post + ", mention=" + mention
                + ", tags=" + tags + "]";
    }
}
