package scholl.both.analyzer.social;

import java.util.*;

public class PostSet {
    private SortedSet<SocialPost> posts;
    private Map<String, SortedSet<SocialPost>> index;
    
    public PostSet() {
        this.posts = new TreeSet<SocialPost>();
        this.index = new HashMap<String, SortedSet<SocialPost>>();
    }
    
    public PostSet(Set<SocialPost> posts) {
        this();
        for (SocialPost p : posts) {
            add(p);
        }
    }
    
    public void addAll(PostSet other) {
        posts.addAll(other.posts);
        index.putAll(other.index);
    }
    
    public void addAll(Set<SocialPost> other) {
        addAll(new PostSet(other));
    }
    
    public void add(SocialPost p) {
        posts.add(p);
        for (String tag : p.getTags()) {
            SortedSet<SocialPost> postsForTag = index.get(tag);
            if (postsForTag == null) {
                postsForTag = new TreeSet<SocialPost>();
            }
            postsForTag.add(p);
            index.put(tag, postsForTag);
        }
    }
    
    public SocialPost[] getPosts() {
        return posts.toArray(new SocialPost[0]);
    }
    
    public PostSet getAllWithTag(String tag) {
        return new PostSet(index.get(tag));
    }
    
    public Map<String, Integer> getWordCount2() {
        Map<String, Integer> m = new HashMap<>();
        for (SocialPost p : posts) {
            Map<String, Integer> n = p.getText().getWordCount2();
            for (String w : n.keySet()) {
                Integer count = m.get(w);
                if (count == null) {
                    count = 0;
                }
                count += n.get(w);
                m.put(w, count);
            }
        }
        return m;
    }
    
    public PostSet clone() throws CloneNotSupportedException {
        PostSet other = (PostSet) super.clone();
        other.posts = new TreeSet<SocialPost>(posts);
        other.index = new HashMap<String, SortedSet<SocialPost>>(index);
        return other;
    }
    
    @Override
    public String toString() {
        return "PostSet [posts=" + this.posts + "]";
    }
}
