package scholl.both.analyzer.social;

import java.util.Set;
import scholl.both.analyzer.text.Text;

public class Post {
    private User poster;
    private long timestamp; // milliseconds since epoch
    private Text post;
    private User mention;
    private Set<String> tags;

    public Post(User poster, long timestamp, String post, User mention, Set<String> tags) {
        this.poster = poster;
        this.timestamp = timestamp;
        this.post = new Text(post);
        this.mention = mention;
        this.tags = tags;
    }
    
    public String[] getTags() {
        return tags.toArray(new String[]{});
    }
}

class User {
    String name;
}
