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

/*
 * OLD CODE








        
        //System.out.printf("URI: %s%n", request);
        //String tmp = request.getQuery();
        //System.out.printf("Path: %s%n", tmp);

    /**
     * This method makes the HTTP header for the response.
     * 
     * @param returnCode HTTP response code
     * @return HTTP header
     *
    private String constructHeader(int returnCode) {
        String responseString = "";
        if (returnCode == 200) {
            responseString = "OK";
        } else if (returnCode == 501) {
            responseString = "Not Implemented";
        } else {
            responseString = "(return code unknown)";
        }
        
        String s = String
                .format("HTTP/1.0 %d %s%s", returnCode, responseString,
                        "%nConnection: close%nServer: SimpleHTTPtutorial v0%nContent-Type: text/html%n%n\r\n");
        
        return s;
    }
    
    private String getOAuthVerifier(BufferedReader input, DataOutputStream output)
            throws IOException {
        PrintStream print = new PrintStream(new BufferedOutputStream(output));
        String tmp = input.readLine();
        
        if (!tmp.toUpperCase().startsWith("GET")) { // Unsupported operation
            output.writeBytes(constructHeader(501)); // HTTP "Not Implemented" response
            output.close();
            throw new UnsupportedOperationException();
        }
        
        print.print(constructHeader(200));
        
        String verifier = tmp.replaceAll(
                "GET /callback\\?oauth_token=\\w+&oauth_verifier=(\\w+) HTTP/1.1", "$1");
        print.print("<script type=\"text/javascript\">\r\nwindow.open('javascript:window.open(\"\", "
                + "\"_self\", \"\");window.close();', '_self');\r\n</script>");
        print.close();
        
        return verifier;
    }

/**
     * Get a request on the set port and return the verifier as a string.
     * 
     * @return the OAuth verifier
     * @throws IOException
     *
    private String handleRequest() throws IOException {
        System.out.printf("A simple HTTP server, used for OAuth callback.%n%n");
        System.out.printf("Trying to bind to localhost on port %d...", port);
        
        ServerSocket serverSocket = new ServerSocket(port);
        
        System.out.printf("Ready, Waiting for requests...%n");
        
        try {
            // this call waits/blocks until someone connects to the port we are listening to
            Socket connectionSocket = serverSocket.accept();
            
            return getOAuthVerifier(
                    new BufferedReader(new InputStreamReader(connectionSocket.getInputStream())),
                    new DataOutputStream(connectionSocket.getOutputStream()));
        } finally {
            serverSocket.close();
        }
    } 
 <!--
window.open('javascript:window.open(\"\", \"_self\", \"\");" +
                    "window.close();', '_self');
//-->
 * 
 * 
 * // Construct the right Content-Type for the header. // This is so the browser knows what to do
 * with the // file, you
 * may know the browser dosen't look on the file // extension, it is the servers job to let the
 * browser know // what
 * kind of file is being transmitted. You may have experienced // if the server is miss configured
 * it may result in //
 * pictures displayed as text! switch (file_type) { // plenty of types for you to fill in case 0:
 * break; case 1: s = s +
 * "Content-Type: image/jpeg\r\n"; break; case 2: s = s + "Content-Type: image/gif\r\n"; case 3: s =
 * s +
 * "Content-Type: application/x-zip-compressed\r\n"; default: s = s + "Content-Type: text/html\r\n";
 * break; } /*s = s +
 * "\r\n"; // other header fields, s = s + "Connection: close\r\n"; // we can't handle persistent
 * connections s = s +
 * "Server: SimpleHTTPtutorial v0\r\n"; // server name
 * 
 * case 400: s = s + "Bad Request"; break; case 403: s = s + "Forbidden"; break; case 404: s = s +
 * "Not Found"; break;
 * case 500: s = s + "Internal Server Error"; break;
 * 
 * @Override public void run() { // we are now inside our own thread separated from the gui.
 * ServerSocket serversocket =
 * null;
 * 
 * s("A simple HTTP server, used for OAuth callback.\n\n"); // Pay attention, this is where things
 * starts to cook! try {
 * out.printf("Trying to bind to localhost on port %d...", port); serversocket = new
 * ServerSocket(port); } catch
 * (IOException e) { e.printStackTrace(); return; }
 * 
 * s("OK!\n"); // go in a infinite loop, wait for connections, process request, send response
 * 
 * // while (true) { s("\nReady, Waiting for requests...\n"); try { // this call waits/blocks until
 * someone connects to
 * the port we are listening to Socket connectionsocket = serversocket.accept();
 * 
 * // Read the http request from the client BufferedReader input = new BufferedReader(new
 * InputStreamReader(
 * connectionsocket.getInputStream()));
 * 
 * // Prepare a output stream from to the client, // this will be used sending back our response to
 * the client.
 * DataOutputStream output = new DataOutputStream(connectionsocket.getOutputStream());
 * 
 * getOauthVerifier(input, output); } catch (IOException e) { // catch any errors, and print them
 * s("\nError:" +
 * e.getMessage()); } finally { try { serversocket.close(); } catch (IOException e) {
 * e.printStackTrace(); } } // } }
 * 
 * 
 * 
 * 
 * int method = 0; // 1 get, 2 head, 0 not supported String http = new String(); // a bunch of
 * strings to hold String
 * file = new String(); // what file String user_agent = new String(); // what user_agent
 * 
 * 
 * 
 * // Read the http request from the client BufferedReader input = new BufferedReader(new
 * InputStreamReader(
 * connectionsocket.getInputStream()));
 * 
 * // Prepare a output stream from to the client. This will be used sending back our response to the
 * client.
 * DataOutputStream output = new DataOutputStream(connectionsocket.getOutputStream());
 * 
 * /* // path do now have the filename to what to the file it wants to open s("\nClient requested:"
 * + new
 * File(path).getAbsolutePath() + "\n"); FileInputStream requestedfile = null;
 * 
 * try { // NOTE that there are several security consideration when passing // the untrusted string
 * "path" to
 * FileInputStream. // You can access all files the current user has read access to!!! // current
 * user is the user
 * running the javaprogram. // you can do this by passing "../" in the url or specify absoulute path
 * // or change drive
 * (win)
 * 
 * // try to open the file, requestedfile = new FileInputStream(path); } catch (Exception e) { try {
 * // if you could not
 * open the file send a 404 output.writeBytes(construct_http_header(404, 0)); // close the stream
 * output.close(); }
 * catch (Exception e2) { } ; s("error" + e.getMessage()); } // print error to gui
 * 
 * // happy day scenario try { int type_is = 0; // find out what the filename ends with, // so you
 * can construct a the
 * right content type if (path.endsWith(".zip")) { type_is = 3; } if (path.endsWith(".jpg") ||
 * path.endsWith(".jpeg")) {
 * type_is = 1; } if (path.endsWith(".gif")) { type_is = 2; // write out the header, 200
 * ->everything is ok we are all
 * happy. } output.writeBytes(construct_http_header(200, 5));
 * 
 * // if it was a HEAD request, we don't print any BODY if (method == 1) { // 1 is GET 2 is head and
 * skips the body
 * while (true) { // read the file from filestream, and print out through the // client-outputstream
 * on a byte per byte
 * base. int b = requestedfile.read(); if (b == -1) { break; // end of file } output.write(b); } }
 * // clean up the
 * files, close open handles output.close(); requestedfile.close(); } catch (Exception e) { }
 * 
 * 
 * // tmp contains "GET /index.html HTTP/1.0 ......." // find first space // find next space // copy
 * whats between minus
 * slash, then you get "index.html" // it's a bit of dirty code, but bear with me... int start = 0;
 * int end = 0; for
 * (int a = 0; a < tmp.length(); a++) { if (tmp.charAt(a) == ' ' && start != 0) { end = a; break; }
 * if (tmp.charAt(a) ==
 * ' ' && start == 0) { start = a; } }
 * 
 * // figure out what ipaddress the client commes from, just for show! //InetAddress client =
 * connectionsocket.getInetAddress(); // and print it to gui //s(client.getHostName() +
 * " connected to server.\n");
 * 
 * /** Very basic implementation of HTTP.
 * 
 * @param input input stream from client
 * 
 * @param output output stream to client
 * 
 * private void http_handler(BufferedReader input, DataOutputStream output) { int method = 0; // 1
 * get, 2 head, 0 not
 * supported String http = new String(); // a bunch of strings to hold String path = new String();
 * // the various
 * things, what http v, what path, String file = new String(); // what file String user_agent = new
 * String(); // what
 * user_agent try { // GET /index.html HTTP/1.0 String tmp = input.readLine();
 * 
 * if (tmp.toUpperCase().startsWith("GET")) { method = 1; } else { // Unsupported operation try {
 * output.writeBytes(construct_http_header(501, 0)); // HTTP "Not Implemented" response
 * output.close(); return; } catch
 * (IOException e) { s("error: " + e.getMessage()); } }
 * 
 * System.out.printf("tmp: %s%n", tmp);
 * 
 * // tmp contains "GET /index.html HTTP/1.0 ......." // find first space // find next space // copy
 * whats between minus
 * slash, then you get "index.html" // it's a bit of dirty code, but bear with me... int start = 0;
 * int end = 0; for
 * (int a = 0; a < tmp.length(); a++) { if (tmp.charAt(a) == ' ' && start != 0) { end = a; break; }
 * if (tmp.charAt(a) ==
 * ' ' && start == 0) { start = a; } } //path =
 * tmp.replaceAll(
 * "GET /callback?oauth_token=([a-zA-Z0-9]{1,60})&oauth_verifier=([a-zA-Z0-9]{1,60}) HTTP/1\\.1",
 * "$2");
 * path = tmp.replaceAll("GET /callback\\?oauth_token=\\w+&oauth_verifier=(\\w+) HTTP/1.1", "$1");
 * System.out.printf("path: %s%n", path); // path = tmp.substring(start + 2, end); // fill in the
 * path } catch
 * (IOException e) { s("error: " + e.getMessage()); } // catch any exception
 * 
 * // path do now have the filename to what to the file it wants to open s("\nClient requested:" +
 * new
 * File(path).getAbsolutePath() + "\n"); FileInputStream requestedfile = null;
 * 
 * try { // NOTE that there are several security consideration when passing // the untrusted string
 * "path" to
 * FileInputStream. // You can access all files the current user has read access to!!! // current
 * user is the user
 * running the javaprogram. // you can do this by passing "../" in the url or specify absoulute path
 * // or change drive
 * (win)
 * 
 * // try to open the file, requestedfile = new FileInputStream(path); } catch (Exception e) { try {
 * // if you could not
 * open the file send a 404 output.writeBytes(construct_http_header(404, 0)); // close the stream
 * output.close(); }
 * catch (Exception e2) { } ; s("error" + e.getMessage()); } // print error to gui
 * 
 * // happy day scenario try { int type_is = 0; // find out what the filename ends with, // so you
 * can construct a the
 * right content type if (path.endsWith(".zip")) { type_is = 3; } if (path.endsWith(".jpg") ||
 * path.endsWith(".jpeg")) {
 * type_is = 1; } if (path.endsWith(".gif")) { type_is = 2; // write out the header, 200
 * ->everything is ok we are all
 * happy. } output.writeBytes(construct_http_header(200, 5));
 * 
 * // if it was a HEAD request, we don't print any BODY if (method == 1) { // 1 is GET 2 is head and
 * skips the body
 * while (true) { // read the file from filestream, and print out through the // client-outputstream
 * on a byte per byte
 * base. int b = requestedfile.read(); if (b == -1) { break; // end of file } output.write(b); } }
 * // clean up the
 * files, close open handles output.close(); requestedfile.close(); } catch (Exception e) {} }
 */
