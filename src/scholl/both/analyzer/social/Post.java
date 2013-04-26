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
    
    public User(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof User))
            return false;
        User other = (User) obj;
        if (this.name == null) {
            if (other.name != null)
                return false;
        } else if (!this.name.equals(other.name))
            return false;
        return true;
    }
}
