package scholl.both.analyzer.social;

import java.util.*;

public class PostSet {
    private Set<Post> posts;
    private Map<String, Set<Post>> postsByTag;
    
    public PostSet() {
        this.posts = new HashSet<Post>();
        this.postsByTag = new HashMap<String, Set<Post>>();
    }
    
    public PostSet(Set<Post> posts) {
        this();
        for (Post p : posts) {
            add(p);
        }
    }
    
    public void addAll(PostSet other) {
        posts.addAll(other.posts);
        postsByTag.putAll(other.postsByTag);
    }
    
    public void addAll(Set<Post> other) {
        addAll(new PostSet(other));
    }
    
    public void add(Post p) {
        posts.add(p);
        for (String tag : p.getTags()) {
            Set<Post> postsForTag = postsByTag.get(tag);
            if (postsForTag == null) {
                postsForTag = new HashSet<Post>();
            }
            postsForTag.add(p);
        }
    }
    
    public PostSet getAllWithTag(String tag) {
        return new PostSet(postsByTag.get(tag));
    }

    public PostSet clone() throws CloneNotSupportedException {
        PostSet other = (PostSet) super.clone();
        other.posts = new HashSet<Post>(posts);
        other.postsByTag = new HashMap<String, Set<Post>>(postsByTag);
        return other;
    }
    
    public String toString() {
        return "PostSet [posts=" + posts + "]";
    }
}
