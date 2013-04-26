package scholl.both.analyzer.social;

import scholl.both.analyzer.text.*;
import java.io.*;
import java.util.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Blog;

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
            thing();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static void thing() throws IOException {
        // Read in the JSON data for the credentials
        BufferedReader br = new BufferedReader(new FileReader("credentials.json"));
        String json = "";
        while (br.ready()) { json += br.readLine(); }
        br.close();
        
        // Parse the credentials
        JsonParser parser = new JsonParser();
        JsonObject obj = (JsonObject) parser.parse(json);
        
        // Create a client
        JumblrClient client = new JumblrClient(
                obj.getAsJsonPrimitive("consumer_key").getAsString(),
                obj.getAsJsonPrimitive("consumer_secret").getAsString()
                );
        
        String blogName = "b41779690b83f182acc67d6388c7bac9";
        Blog b = client.blogInfo(blogName);
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("reblog_info", "true");
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
    }
}
