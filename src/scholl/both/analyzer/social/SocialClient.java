package scholl.both.analyzer.social;

import scholl.both.analyzer.text.*;
import java.io.*;
import java.util.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.*;

public class SocialClient {
    public static void main(String[] args) {
        List<String> texts = new ArrayList<String>();
        
        if (args.length == 0) {
            String[] newArgs = { "hello world!", "hello", "First sentence. Second sentence.", "doopity doo baa baa." };
            args = newArgs;
        }
        
        if (args[0] == "-f" || args[0] == "--file") {
            // Rest of arguments are files.
            for (int i = 0; i < args.length; i++) {
                File f = new File(args[i]);
                
                Reader is = null;
                try {
                    is = new BufferedReader(new FileReader(f));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                
                StringBuffer sb = new StringBuffer();
                int ch = 0;
                while (ch != -1) {
                    sb.append((char) ch);
                    try {
                        ch = is.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                texts.add(sb.toString());
            }
        } else {
            for (String s : args) {
                texts.add(s);
            }
        }
        
        for (String str : texts) {
            Text t = new Text(str);
            System.out
                    .printf("%d words, %d characters: %s%n", t.getWordCount(), t.getCharacterCount(), t.getOriginal());
        }
        
        try {
            tumlbrThing();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void tumlbrThing() throws IOException {
        // Read in the JSON data for the credentials
        BufferedReader br = new BufferedReader(new FileReader("credentials.json"));
        String json = "";
        while (br.ready()) {
            json += br.readLine();
        }
        br.close();
        
        // Parse the credentials
        JsonParser parser = new JsonParser();
        JsonObject obj = (JsonObject) parser.parse(json);
        
        // Create a client
        JumblrClient client = new JumblrClient(obj.getAsJsonPrimitive("consumer_key").getAsString(), obj
                .getAsJsonPrimitive("consumer_secret").getAsString());
        
        String blogName = "b41779690b83f182acc67d6388c7bac9";
        
        Blog b = client.blogInfo(blogName);
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("reblog_info", "true");
<<<<<<< HEAD
        options.put("filter", "text");
        options.put("notes_info", "true");
        options.put("limit", "20");
        
        PostSet ps = new PostSet();
        
        for (Post p : b.posts(options)) {
            SocialPost sp = socialFromTumblrPost(p);
            System.out.println(sp.getText().getWordCount2());
            ps.add(sp);
        }
        
        System.out.println(ps.getWordCount2());
        
        for (SocialPost p : ps.getPosts()) {
            System.out.println(p);
        }
        
        //doNotesStuff(b);
    }
    
    public static void doNotesStuff(Blog b) {
        JumblrClient c = b.getClient();
        Map<String, Object> options = new HashMap<>();
        options.put("reblog_info", "true");
        options.put("filter", "text");
        options.put("notes_info", "true");
        options.put("id", "49118498864");
        options.put("limit", "1");
        
        Post p = c.blogPosts(b.getName(), options).get(0);
        
        List<Note> notes = new ArrayList<Note>();
        for (int i = 0; i < p.getNoteCount(); i += 50) {
            options.put("start", i);
            options.put("id", p.getId());
            Post p2 = b.posts(options).get(0);
            notes.addAll(Arrays.asList(p2.getNotes()));
        }
        
        System.out.printf("%s: (%d)%n", p, notes.size());
        for (Note n : notes) {
            System.out.printf("  %s%n", n);
        }
        System.out.println();
        
        List<Post> posts = new ArrayList<Post>();
        for (Note n : notes) {
            if (n.getPostId() == null)
                continue;
            Map<String, Object> options2 = new HashMap<>();
            options2.put("reblog_info", "true");
            options2.put("id", n.getPostId());
            Post q = c.blogPosts(b.getName(), options).get(0);
            
            System.out.printf("%s%n", n);
            
            System.out.printf("(%s-%d) : (%s-%d) [%tF %5$tr] [%s]%n", q.getBlogName(), q.getId(),
                    q.getRebloggedName(), q.getRebloggedId(), q.getTimestamp()*1000L, q.getReblogKey());
            
            posts.add(q);
        }
        
        for (Post q : posts) {
            
        }
        System.out.println(posts);
        
    }
    
    public static SocialPost socialFromTumblrPost(Post p) {
        SimpleUser u = new SimpleUser(p.getBlogName());
        List<String> tags = new ArrayList<>();
        for (String t : p.getTags()) {
            tags.add(t);
        }
        
        SimpleUser mentioned = new SimpleUser(p.getRebloggedName());
        
        String type = p.getType();
        String text = "unknown";
        if (type.equals("text")) {
            assert p instanceof TextPost;
            text = ((TextPost) p).getBody();
        } else if (type.equals("photo")) {
            assert p instanceof PhotoPost;
            text = ((PhotoPost) p).getCaption();
        } else if (type.equals("quote")) {
            assert p instanceof QuotePost;
            text = ((QuotePost) p).getText();
        } else if (type.equals("link")) {
            assert p instanceof LinkPost;
            text = ((LinkPost) p).getDescription();
        }
        
        /*
         * long postId = p.getRebloggedId(); Post reblogged = client.blogPost(blogName, postId);
         */
        
        SocialPost np = new SocialPost(u, p.getTimestamp(), text, mentioned, tags);
        return np;
=======
        PostSet ps = new PostSet();
        for (com.tumblr.jumblr.types.Post p : b.posts(options)) {
            User u = new SimpleUser(p.getBlogName());
            Set<String> tags = new HashSet<String>();
            for (String t : p.getTags()) {
                tags.add(t);
            }
            String type = p.getType();
            Post np = new Post(u, p.getTimestamp(), type, new SimpleUser(p.getRebloggedName()), tags);
            ps.add(np);
        }
        
        System.out.println(ps);        
>>>>>>> 78e224b34b5c38d3e587740be6b906a2664e0da8
    }
}
