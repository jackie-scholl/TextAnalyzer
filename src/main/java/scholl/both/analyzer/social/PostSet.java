package scholl.both.analyzer.social;

import scholl.both.analyzer.util.Counter;

import java.util.*;

// TODO: Add Javadocs

public class PostSet {
    private volatile SortedSet<SocialPost> posts;
    private volatile Map<String, SortedSet<SocialPost>> tagIndex;

    
    public PostSet() {
        this.posts = new TreeSet<SocialPost>();
        this.tagIndex = new HashMap<String, SortedSet<SocialPost>>();
    }
    
    public PostSet(Iterable<SocialPost> posts) {
        this();
        for (SocialPost p : posts) {
            add(p);
        }
    }
    
    public SocialPost[] getPosts() {
        return posts.toArray(new SocialPost[0]);
    }
    
    public PostSet getAllWithTag(String tag) {
        return new PostSet(tagIndex.get(tag));
    }
    
    public SocialPost getMostRecent() {
        if (posts.isEmpty()) {
            return null;
        }
        return posts.last();
    }
    
    public SocialPost getOldest() {
        if (posts.isEmpty()) {
            return null;
        }
        return posts.first();
    }
    
    public PostSet getAllByUser(SocialUser u) {
        PostSet ps = new PostSet();
        for (SocialPost p : posts) {
            if (u.equals(p.getPoster())) {
                ps.add(p);
            }
        }
        return ps;
    }

    public int size() {
        return posts.size();
    }
    
    public Counter<Character> getLetterCount2() {
        Counter<Character> c = new Counter<Character>();
        for (SocialPost p : posts) {
            c.addAll(p.getLetterCount2());
        }
        return c;
    }
    
    public Counter<String> getWordCount2() {
        Counter<String> c = new Counter<String>();
        for (SocialPost p : posts) {
            c.addAll(p.getWordCount2());
        }
        return c;
    }
    
    public void add(SocialPost p) {
        posts.add(p);
        for (String tag : p.getTags()) {
            SortedSet<SocialPost> postsForTag = tagIndex.get(tag);
            if (postsForTag == null) {
                postsForTag = new TreeSet<SocialPost>();
            }
            postsForTag.add(p);
            tagIndex.put(tag, postsForTag);
        }
    }
    
    public void addAll(PostSet other) {
        posts.addAll(other.posts);
        tagIndex.putAll(other.tagIndex);
    }
    
    public void addAll(Iterable<SocialPost> other) {
        addAll(new PostSet(other));
    }
    
    @Override
    public PostSet clone() throws CloneNotSupportedException {
        PostSet other = (PostSet) super.clone();
        other.posts = new TreeSet<SocialPost>(posts);
        other.tagIndex = new HashMap<String, SortedSet<SocialPost>>(tagIndex);
        return other;
    }
    
    @Override
    public String toString() {
        return "PostSet [posts=" + this.posts + "]";
    }
}
