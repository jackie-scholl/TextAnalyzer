package scholl.both.analyzer.social;

import java.io.*;
import java.net.*;

/**
 * NOTE: I copied this from http://fragments.turtlemeat.com/javawebserver.php
 */

// file: server.java
// the real (http) serverclass
// it extends thread so the server is run in a different
// thread than the gui, that is to make it responsive.
// it's really just a macho coding thing.
/**
 * An HTTP server.
 * 
 * WARNING: Considered highly dangerous. Not ready for production use.
 * 
 * @author Jackson
 */
public class Server extends Thread {
    private WebserverStarter message_to; // the starter class, needed for gui
    private int port; // port we are going to listen to
    
    /**
     * Sole constructor.
     * 
     * @param listen_port port to bind to (default 80)
     * @param to_send_message_to reference to gui to pass messages
     */
    public Server(int listen_port, WebserverStarter to_send_message_to) {
        message_to = to_send_message_to;
        port = listen_port;
        
        // Spawns a new thread for the server. All requests are handled in this thread.
        this.start();
    }
    
    /**
     * Send the given message to the GUI
     * 
     * @param message message to send to the GUI
     */
    private void s(String message) {
        message_to.send_message_to_window(message);
    }
    
    @Override
    public void run() {
        // we are now inside our own thread separated from the gui.
        ServerSocket serversocket = null;
        
        s("A simple HTTP server, used for OAuth callback.\n\n");
        // Pay attention, this is where things starts to cook!
        try {
            // print/send message to the guiwindow
            s(String.format("Trying to bind to localhost on port %d...", port));
            // make a ServerSocket and bind it to given port,
            serversocket = new ServerSocket(port);
        } catch (IOException e) { // catch any errors and print errors to gui
            e.printStackTrace();
            return;
        }
        
        s("OK!\n");
        // go in a infinite loop, wait for connections, process request, send response
        
        while (true) {
            s("\nReady, Waiting for requests...\n");
            try {
                // this call waits/blocks until someone connects to the port we
                // are listening to
                Socket connectionsocket = serversocket.accept();
                // figure out what ipaddress the client commes from, just for show!
                InetAddress client = connectionsocket.getInetAddress();
                // and print it to gui
                s(client.getHostName() + " connected to server.\n");
                // Read the http request from the client from the socket interface
                // into a buffer.
                BufferedReader input = new BufferedReader(new InputStreamReader(
                        connectionsocket.getInputStream()));
                // Prepare a outputstream from us to the client,
                // this will be used sending back our response
                // (header + requested file) to the client.
                DataOutputStream output = new DataOutputStream(connectionsocket.getOutputStream());
                
                // as the name suggest this method handles the http request, see further down.
                // abstraction rules
                http_handler(input, output);
            } catch (IOException e) { // catch any errors, and print them
                s("\nError:" + e.getMessage());
            } finally {
                try {
                    serversocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    // our implementation of the hypertext transfer protocol
    // its very basic and stripped down
    /**
     * Very basic implementation of HTTP.
     * @param input input stream from client
     * @param output output stream to client
     */
    private void http_handler(BufferedReader input, DataOutputStream output) {
        int method = 0; // 1 get, 2 head, 0 not supported
        String http = new String(); // a bunch of strings to hold
        String path = new String(); // the various things, what http v, what path,
        String file = new String(); // what file
        String user_agent = new String(); // what user_agent
        try {
            // This is the two types of request we can handle
            // GET /index.html HTTP/1.0
            // HEAD /index.html HTTP/1.0
            String tmp = input.readLine();
            String tmp2 = new String(tmp);
            if (tmp.toUpperCase().startsWith("GET")) {
                method = 1;
            } else { // Unsupported operation
                try {
                    output.writeBytes(construct_http_header(501, 0)); // HTTP "Not Implemented" response
                    output.close();
                    return;
                } catch (IOException e) {
                    s("error: " + e.getMessage());
                }
            }
                        
            // tmp contains "GET /index.html HTTP/1.0 ......."
            // find first space
            // find next space
            // copy whats between minus slash, then you get "index.html"
            // it's a bit of dirty code, but bear with me...
            int start = 0;
            int end = 0;
            for (int a = 0; a < tmp.length(); a++) {
                if (tmp.charAt(a) == ' ' && start != 0) {
                    end = a;
                    break;
                }
                if (tmp.charAt(a) == ' ' && start == 0) {
                    start = a;
                }
            }
            path = tmp.replaceAll("GET /(?<path>\\w+) .+", "${path}");
            System.out.println(path);
            //path = tmp.substring(start + 2, end); // fill in the path
        } catch (IOException e) {
            s("error: " + e.getMessage());
        } // catch any exception
        
        // path do now have the filename to what to the file it wants to open
        s("\nClient requested:" + new File(path).getAbsolutePath() + "\n");
        FileInputStream requestedfile = null;
        
        try {
            // NOTE that there are several security consideration when passing
            // the untrusted string "path" to FileInputStream.
            // You can access all files the current user has read access to!!!
            // current user is the user running the javaprogram.
            // you can do this by passing "../" in the url or specify absoulute path
            // or change drive (win)
            
            // try to open the file,
            requestedfile = new FileInputStream(path);
        } catch (Exception e) {
            try {
                // if you could not open the file send a 404
                output.writeBytes(construct_http_header(404, 0));
                // close the stream
                output.close();
            } catch (Exception e2) {}
            ;
            s("error" + e.getMessage());
        } // print error to gui
        
        // happy day scenario
        try {
            int type_is = 0;
            // find out what the filename ends with,
            // so you can construct a the right content type
            if (path.endsWith(".zip")) {
                type_is = 3;
            }
            if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
                type_is = 1;
            }
            if (path.endsWith(".gif")) {
                type_is = 2;
                // write out the header, 200 ->everything is ok we are all happy.
            }
            output.writeBytes(construct_http_header(200, 5));
            
            // if it was a HEAD request, we don't print any BODY
            if (method == 1) { // 1 is GET 2 is head and skips the body
                while (true) {
                    // read the file from filestream, and print out through the
                    // client-outputstream on a byte per byte base.
                    int b = requestedfile.read();
                    if (b == -1) {
                        break; // end of file
                    }
                    output.write(b);
                }
                
            }
            // clean up the files, close open handles
            output.close();
            requestedfile.close();
        }
        
        catch (Exception e) {}
        
    }
    
    // this method makes the HTTP header for the response
    // the headers job is to tell the browser the result of the request
    // among if it was successful or not.
    private String construct_http_header(int return_code, int file_type) {
        String s = "HTTP/1.0 ";
        // you probably have seen these if you have been surfing the web a while
        switch (return_code) {
        case 200:
            s = s + "200 OK";
            break;
        case 400:
            s = s + "400 Bad Request";
            break;
        case 403:
            s = s + "403 Forbidden";
            break;
        case 404:
            s = s + "404 Not Found";
            break;
        case 500:
            s = s + "500 Internal Server Error";
            break;
        case 501:
            s = s + "501 Not Implemented";
            break;
        }
        
        s = s + "\r\n"; // other header fields,
        s = s + "Connection: close\r\n"; // we can't handle persistent connections
        s = s + "Server: SimpleHTTPtutorial v0\r\n"; // server name
        
        // Construct the right Content-Type for the header.
        // This is so the browser knows what to do with the
        // file, you may know the browser dosen't look on the file
        // extension, it is the servers job to let the browser know
        // what kind of file is being transmitted. You may have experienced
        // if the server is miss configured it may result in
        // pictures displayed as text!
        switch (file_type) {
        // plenty of types for you to fill in
        case 0:
            break;
        case 1:
            s = s + "Content-Type: image/jpeg\r\n";
            break;
        case 2:
            s = s + "Content-Type: image/gif\r\n";
        case 3:
            s = s + "Content-Type: application/x-zip-compressed\r\n";
        default:
            s = s + "Content-Type: text/html\r\n";
            break;
        }
        
        // //so on and so on......
        s = s + "\r\n"; // this marks the end of the httpheader
        // and the start of the body
        // ok return our newly created header!
        return s;
    }
    
} // class phhew caffeine yes please!

/*

*/
