package scholl.both.analyzer.social;

import scholl.both.analyzer.util.Counter;

import java.util.*;
import java.util.regex.Pattern;

// TODO: Add Javadocs

public class PostSet implements Iterable<SocialPost> {
    private SortedSet<SocialPost> posts;
    private Map<String, SortedSet<SocialPost>> tagIndex;
    
    public PostSet() {
        posts = new TreeSet<SocialPost>();
        this.tagIndex = new HashMap<String, SortedSet<SocialPost>>();
    }
    
    public PostSet(Iterable<SocialPost> posts) {
        this();
        addAll(posts);
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

    public void addAll(Iterable<SocialPost> other) {
        if (other == null) {
            return;
        }
        
        for (SocialPost p : other) {
            add(p);
        }
    }

    public SocialPost[] getPostArray() {
        return posts.toArray(new SocialPost[0]);
    }
    
    public List<SocialPost> getPostList() {
        return new ArrayList<SocialPost>(posts);
    }
    
    public PostSet getAllWithTag(String tag) {
        return new PostSet(tagIndex.get(tag));
    }
    
    public PostSet getAllTextsMatchingRegex(Pattern pattern) {
        PostSet ps = new PostSet();
        for (SocialPost p : posts) {
            if (pattern.matcher(p.getOriginal()).find()) {
                ps.add(p);
            }
        }
        return ps;
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
    
    public Set<String> getAllTags() {
        return tagIndex.keySet();
    }
    
    public boolean contains(SocialPost p) {
        return posts.contains(p);
    }

    public Counter<Character> getLetterCount() {
        Counter<Character> c = new Counter<Character>();
        for (SocialPost p : posts) {
            c.addAll(p.getLetterCount2());
        }
        return c;
    }
    
    public Counter<String> getWordCount() {
        Counter<String> c = new Counter<String>();
        for (SocialPost p : posts) {
            c.addAll(p.getWordCount2());
        }
        return c;
    }
    
    public int size() {
        return posts.size();
    }

    public SortedSet<SocialPost> toSet() {
        return new TreeSet<SocialPost>(posts);
    }
    
    public Iterator<SocialPost> iterator() {
        return posts.iterator();
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
