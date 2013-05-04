package scholl.both.analyzer.social;

import scholl.both.analyzer.text.*;

import java.awt.Desktop;
import java.io.*;
import java.util.*;

import java.net.URI;
import java.net.URISyntaxException;

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
            System.out.printf("%d words, %d characters: %s%n",
                    t.getWordCount(), t.getCharacterCount(), t.getOriginal());
        }
        
        try {
            SocialStats.tumlbrThing();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void openBrowser(String url) throws IOException {
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
}

/*
 * List<String> tags = new ArrayList<>(); for (String t : p.getTags()) { tags.add(t); }
 
 OAuthService service = client.getRequestBuilder().getOAuth();
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
