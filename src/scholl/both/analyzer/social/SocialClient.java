package scholl.both.analyzer.social;

import scholl.both.analyzer.text.*;

import java.io.*;
import java.util.*;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TumblrApi;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.*;

public class SocialClient {
    private static List<String> requests = new ArrayList<String>();
    
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
    
    private static void tumlbrThing() throws IOException {
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
        
        String consumerKey = obj.getAsJsonPrimitive("consumer_key").getAsString();
        String consumerSecret = obj.getAsJsonPrimitive("consumer_secret").getAsString();
        // Create a client
        JumblrClient client = new JumblrClient(consumerKey, consumerSecret);
        
        OAuthService service = new ServiceBuilder().provider(TumblrApi.class).apiKey(consumerKey).apiSecret(consumerSecret).build();
        Token request = service.getRequestToken();
        
        String url = service.getAuthorizationUrl(request);
        openBrowser(url);
        
        OAuthCallbackServer s = new OAuthCallbackServer(8002);
        String response = s.handleRequest();
        
        Token access = service.getAccessToken(request, new Verifier(response));
        client.setToken(access.getToken(), access.getSecret());
        
        System.out.println(client.user().getName());
        User u = client.user();
        
        Set<Blog> blogs = new HashSet<Blog>();
        
        System.out.printf("User %s has these blogs: %n", u.getName());
        for (Blog b : u.getBlogs()) {
            blogs.add(b);
            
            List<User> followers = getFollowers(b, new HashMap<String, Object>(), 100);
            System.out.printf("\t%s (%s) has these %d followers:%n", b.getName(), b.getTitle(), followers.size());
            
            for (User u2 : followers) {
                System.out.printf("\t\t%s%n", u2.getName());
                Blog b3 = client.blogInfo(u2.getName());
                blogs.add(b3);
            }
        }
        
        List<Blog> following = getFollowing(client, new HashMap<String, Object>(), 100);
        System.out.printf("User %s is following these %d blogs: %n", following.size(), u.getName());
        for (Blog b : following) {
            System.out.printf("\t%s (%s)%n", b.getName(), b.getTitle());
            blogs.add(b);
        }
        
        System.out.println();
        
        
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("reblog_info", "true");
        options.put("filter", "text");
        options.put("notes_info", "true");
        options.put("limit", "20");
        
        PrintStream originalOutStream = System.out;
        
        String[] blogNames = {"dataandphilosophy", "b41779690b83f182acc67d6388c7bac9", "frittlesnink"};
        for (String blog : blogNames) {
            blogs.add(client.blogInfo(blog));
        }
        
        int count = 10;
        
        for (Blog b : blogs) {
            System.setOut(new PrintStream(String.format("out//%s.txt", b.getName())));
            
            PostSet ps = getPosts(b, options, count);
            System.out.println(ps.getWordCount2().toString2());
            
            System.setOut(originalOutStream);
            System.out.printf("Finished blog %s with %d posts.%n", b.getName(), ps.size());
        }
        
        System.out.println("Finished.");
    }
    
    private static void openBrowser(String url) throws IOException {
        System.out.println(Desktop.isDesktopSupported());
        Desktop d = Desktop.getDesktop();
        URI uri;
        try {
            uri = new URI(url);
            d.browse(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    
    private static List<User> getFollowers(Blog b, Map<String, Object> options, int num) {
        List<User> users = new ArrayList<User>();
        
        int lim = 20;
        lim = num>lim? lim : num;
        
        int initialOffset = 0;
        for (int i = initialOffset; i < num; i += lim) {
            options.put("limit", lim);
            options.put("offset", i);
            users.addAll(b.followers(options));
        }
        
        return users;
    }
    
    private static List<Blog> getFollowing(JumblrClient c, Map<String, Object> options, int num) {
        List<Blog> blogs = new ArrayList<Blog>();
        
        int lim = 20;
        lim = num>lim? lim : num;
        
        int initialOffset = 0;
        for (int i = initialOffset; i < num; i += lim) {
            options.put("limit", lim);
            options.put("offset", i);
            blogs.addAll(c.userFollowing(options));
        }
        
        return blogs;
    }
    
    private static PostSet getPosts(Blog b, Map<String, Object> options, int num) {
        PostSet ps = new PostSet();
        
        int lim = 20;
        
        lim = num>lim? lim : num;
        
        int initialOffset = 0;
        for (int i = initialOffset; i < num; i += lim) {
            options.put("limit", lim);
            options.put("offset", i);
            List<Post> posts = b.posts(options);
            for (Post p : posts) {
                ps.add(socialFromTumblrPost(p));
            }
        }
        
        return ps;
    }
    
    private static SocialPost socialFromTumblrPost(Post p) {
        SocialUser u = new SimpleUser(p.getBlogName());
        
        SocialUser mentioned = new SimpleUser(p.getRebloggedName());
        
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
        
        SocialPost np = new SocialPost(u, p.getTimestamp(), text, mentioned, p.getTags());
        return np;
        
    }
}

/*
 * List<String> tags = new ArrayList<>(); for (String t : p.getTags()) { tags.add(t); }
 */

/*
 * 
 * /*OAuthService service = client.getRequestBuilder().getOAuth();
        Token t = service.getRequestToken();
        String url = service.getAuthorizationUrl(t);
        System.out.printf("Go to this URL to authenticate: %s%n", url);
        (new Scanner(System.in)).nextLine();
        System.out.println(t.getRawResponse());
        client.setToken(t.getToken(), t.getSecret());
        System.out.println(client.user().getName());
        Scanner sc = new Scanner(System.in);
        //sc.nextLine();
        
        //String requestURL = requests.remove(0);
        //String response = requestURL.replaceAll("callback\\?oauth_token=\\w+&oauth_verifier=(?<verifier>\\w+)", "$g{verifier}");
        
 * public static void doNotesStuff(Blog b) { JumblrClient c = b.getClient(); Map<String, Object> options = new
 * HashMap<>(); options.put("reblog_info", "true"); options.put("filter", "text"); options.put("notes_info", "true");
 * options.put("id", "49118498864"); options.put("limit", "1");
 * 
 * Post p = c.blogPosts(b.getName(), options).get(0);
 * 
 * List<Note> notes = new ArrayList<Note>(); for (int i = 0; i < p.getNoteCount(); i += 50) { options.put("start", i);
 * options.put("id", p.getId()); Post p2 = b.posts(options).get(0); notes.addAll(Arrays.asList(p2.getNotes())); }
 * 
 * System.out.printf("%s: (%d)%n", p, notes.size()); for (Note n : notes) { System.out.printf("  %s%n", n); }
 * System.out.println();
 * 
 * List<Post> posts = new ArrayList<Post>(); for (Note n : notes) { if (n.getPostId() == null) continue; Map<String,
 * Object> options2 = new HashMap<>(); options2.put("reblog_info", "true"); options2.put("id", n.getPostId()); Post q =
 * c.blogPosts(b.getName(), options).get(0);
 * 
 * System.out.printf("%s%n", n);
 * 
 * System.out.printf("(%s-%d) : (%s-%d) [%tF %5$tr] [%s]%n", q.getBlogName(), q.getId(), q.getRebloggedName(),
 * q.getRebloggedId(), q.getTimestamp()*1000L, q.getReblogKey());
 * 
 * posts.add(q); }
 * 
 * for (Post q : posts) {
 * 
 * } System.out.println(posts);
 * 
 * }
 */

/*
 * for (Post p : b.posts(options)) { SocialPost sp = socialFromTumblrPost(p);
 * //System.out.println(sp.getText().getWordCount2()); ps.add(sp); }
 */
/*
 * for (SocialPost p : ps.getPosts()) { System.out.println(p); }
 */
