package scholl.both.analyzer.social;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;

import com.sun.net.httpserver.*;

/**
 * An HTTP server.
 * 
 * NOTE: I modified this from http://stackoverflow.com/a/3732328/1772907
 * 
 * @author Jackson
 */
@SuppressWarnings("restriction")
public class OAuthCallbackServer {
    private URI request;
    private HttpServer server;
    
    /**
     * Sole constructor.
     * 
     * @param listenPort port to bind to (default 80)
     */
    public OAuthCallbackServer(int listenPort) {
        request = null;
        
        try {
            server = HttpServer.create(new InetSocketAddress(listenPort), 0);
            server.createContext("/callback", new MyHandler(this));
            server.setExecutor(null); // creates a default executor
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    static class MyHandler implements HttpHandler {
        private final OAuthCallbackServer s;
        
        public MyHandler(OAuthCallbackServer s) {
            this.s = s;
        }
        
        public void handle(HttpExchange t) throws IOException {
            StringBuffer sb = new StringBuffer();
            Reader r = new FileReader("callback_response_page.html");
            while (r.ready()) {
                sb.append((char) r.read());
            }
            r.close();
            
            String response = sb.toString();
            
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
            
            s.setRequest(t.getRequestURI());
        }
    }
    
    private void setRequest(URI uri) {
        request = uri;
    }
    
    public String getVerifier() {
        while (request == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        server.stop(1);
        
        String verifier = request.getQuery().replaceAll("oauth_token=\\w+&oauth_verifier=(\\w+)",
                "$1");
        System.out.printf("Verifier: %s%n", verifier);
        
        return verifier;
    }
}
