package scholl.both.analyzer.social;

import scholl.both.analyzer.text.Text;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MainClient {
    public static final boolean THREADING = true;

    public static void main(String[] args) {
        List<String> texts = new ArrayList<String>();
        
        for (String s : args) {
            texts.add(s);
        }
        
        for (String str : texts) {
            Text t = new Text(str);
            System.out.printf("%d words, %d characters: %s%n", t.getWordCount(),
                    t.getCharacterCount(), t.getOriginal());
        }
        
        try {
            SocialStats.tumblrThing(500);
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
