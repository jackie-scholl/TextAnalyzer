package scholl.both.analyzer.social.networks;

import scholl.both.analyzer.social.*;

import java.io.*;
import java.util.*;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TumblrApi;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.*;

public class TumblrClient {
    private JumblrClient client;
    private String consumerKey, consumerSecret;

    public TumblrClient() throws IOException {
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

        consumerKey = obj.getAsJsonPrimitive("consumer_key").getAsString();
        consumerSecret = obj.getAsJsonPrimitive("consumer_secret").getAsString();
        // Create a client
        client = new JumblrClient(consumerKey, consumerSecret);
    }

    public void authenticate() throws IOException {
        OAuthService service = new ServiceBuilder().provider(TumblrApi.class).apiKey(consumerKey)
                .apiSecret(consumerSecret).build();
        Token request = service.getRequestToken();

        String url = service.getAuthorizationUrl(request);
        SocialClient.openBrowser(url);

        OAuthCallbackServer s = new OAuthCallbackServer(8002);
        String response = s.handleRequest();

        Token access = service.getAccessToken(request, new Verifier(response));
        client.setToken(access.getToken(), access.getSecret());
    }

    public JumblrClient getJumblr() {
        return client;
    }
    
    public SocialUser getAuthenticatedUser() {
        return new TumblrUser(client.blogInfo(client.user().getName()));
    }
    
    public Set<SocialUser> getInterestingUsers() {
        Set<SocialUser> s = new HashSet<SocialUser>();
        
        User u = client.user();
        
        System.out.printf("User %s has these blogs: %n", u.getName());
        for (Blog b : u.getBlogs()) {
            s.add(new TumblrUser(b));
            
            List<User> followers = TumblrClient.getFollowers(b, new HashMap<String, Object>(), 100);
            System.out.printf("\t%s (%s) has these %d followers:%n", b.getName(), b.getTitle(), followers.size());
            
            for (User u2 : followers) {
                System.out.printf("\t\t%s%n", u2.getName());
                Blog b3 = client.blogInfo(u2.getName());
                s.add(new TumblrUser(b3));
            }
        }
        
        List<Blog> following = TumblrClient.getFollowing(client, new HashMap<String, Object>(), 100);
        System.out.printf("User %s is following these %d blogs: %n", u.getName(), following.size());
        for (Blog b : following) {
            System.out.printf("\t%s (%s)%n", b.getName(), b.getTitle());
            s.add(new TumblrUser(b));
        }
        
        System.out.println();
        
        return s;
    }
    
    public SocialUser getUser(Blog b) {
        return new TumblrUser(b);
    }
    
    public SocialUser getUser(String b) {
        return getUser(client.blogInfo(b));
    }

    public static List<User> getFollowers(Blog b, Map<String, Object> options, int num) {
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

    public static List<Blog> getFollowing(JumblrClient c, Map<String, Object> options, int num) {
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

    public static SocialPost socialFromTumblrPost(Post p) {
        SocialUser u = new TumblrUser(p.getBlogName(), p.getClient());
        
        //SocialUser mentioned = new TumblrUser(p.getRebloggedName(), p.getClient());
        
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
        
        SocialPost np = new SocialPost(u, p.getTimestamp(), text, null, p.getTags());
        return np;
        
    }

    public static PostSet getPosts(Blog b, Map<String, Object> options, int num) {
        PostSet ps = new PostSet();
        
        int lim = 20;
        
        lim = num>lim? lim : num;
        
        int initialOffset = 0;
        for (int i = initialOffset; i < num; i += lim) {
            options.put("limit", lim);
            options.put("offset", i);
            List<Post> posts = b.posts(options);
            for (Post p : posts) {
                ps.add(TumblrClient.socialFromTumblrPost(p));
            }
        }
        
        return ps;
    }
}
