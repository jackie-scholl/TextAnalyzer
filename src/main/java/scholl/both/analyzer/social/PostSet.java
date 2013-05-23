package scholl.both.analyzer.social;

import scholl.both.analyzer.util.Counter;
import scholl.both.analyzer.util.TimeSample;

import java.util.*;
import java.util.regex.Pattern;

// TODO: Add Javadocs

public class PostSet implements Iterable<Post> {
    private SortedSet<Post> posts;
    private Map<String, SortedSet<Post>> tagIndex;
    
    public PostSet() {
        posts = new TreeSet<Post>();
        this.tagIndex = new HashMap<String, SortedSet<Post>>();
    }
    
    public PostSet(Iterable<Post> posts) {
        this();
        addAll(posts);
    }  
        
    public void add(Post p) {
        posts.add(p);
        for (String tag : p.getTags()) {
            SortedSet<Post> postsForTag = tagIndex.get(tag);
            if (postsForTag == null) {
                postsForTag = new TreeSet<Post>();
            }
            postsForTag.add(p);
            tagIndex.put(tag, postsForTag);
        }
    }

    public void addAll(Iterable<Post> other) {
        if (other == null) {
            return;
        }
        
        for (Post p : other) {
            add(p);
        }
    }

    public PostSet getAllWithTag(String tag) {
        return new PostSet(tagIndex.get(tag));
    }
    
    public PostSet getAllTextsMatchingRegex(Pattern pattern) {
        PostSet ps = new PostSet();
        for (Post p : posts) {
            if (pattern.matcher(p.getOriginal()).find()) {
                ps.add(p);
            }
        }
        return ps;
    }
    
    public Post getMostRecent() {
        if (posts.isEmpty()) {
            return null;
        }
        return posts.last();
    }
    
    public Post getOldest() {
        if (posts.isEmpty()) {
            return null;
        }
        return posts.first();
    }
    
    public PostSet getAllByUser(User u) {
        PostSet ps = new PostSet();
        for (Post p : posts) {
            if (u.equals(p.getPoster())) {
                ps.add(p);
            }
        }
        return ps;
    }
    
    public Set<String> getAllTags() {
        return tagIndex.keySet();
    }
    
    public boolean contains(Post p) {
        return posts.contains(p);
    }

    public Counter<Character> getLetterCount() {
        Counter<Character> c = new Counter<Character>();
        for (Post p : posts) {
            c.addAll(p.getLetterCount2());
        }
        return c;
    }
    
    public Counter<String> getWordCount() {
        Counter<String> c = new Counter<String>();
        for (Post p : posts) {
            c.addAll(p.getWordCount2());
        }
        return c;
    }
    
    public TimeSample getTimeSample() {
        TimeSample sample = new TimeSample();
        for (Post p : posts) {
           sample.add(p.getCalendar());
        }
        return sample;
    }
    
    public int size() {
        return posts.size();
    }

    public SortedSet<Post> toSet() {
        return new TreeSet<Post>(posts);
    }
    
    public Post[] toArray() {
        return posts.toArray(new Post[0]);
    }

    public List<Post> toList() {
        return new ArrayList<Post>(posts);
    }

    public Iterator<Post> iterator() {
        return posts.iterator();
    }
    
    @Override
    public PostSet clone() throws CloneNotSupportedException {
        PostSet other = (PostSet) super.clone();
        other.posts = new TreeSet<Post>(posts);
        other.tagIndex = new HashMap<String, SortedSet<Post>>(tagIndex);
        return other;
    }
    
    @Override
    public String toString() {
        return "PostSet [posts=" + this.posts + "]";
    }
}
