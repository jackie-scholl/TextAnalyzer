package scholl.both.analyzer.social.networks;

import scholl.both.analyzer.social.PostSet;
import scholl.both.analyzer.social.SocialUser;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TwitterClient implements SocialClient {
    private Twitter twitter;
    
    public static void main(String[] args) throws IOException, TwitterException {
        System.out.println("Hello World!");
        
        BufferedReader br = new BufferedReader(new FileReader("twitter_credentials.txt"));
        String consumerKey = br.readLine();
        String consumerSecret = br.readLine();
        String accessToken = br.readLine();
        String tokenSecret = br.readLine();
        br.close();
        
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(tokenSecret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        
        User user = twitter.verifyCredentials();
        List<Status> statuses = twitter.getHomeTimeline();
        System.out.println("Showing @" + user.getScreenName() + "'s home timeline.");
        for (Status status : statuses) {
            System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
        }
        
        long cursor = -1;
        List<User> following = new ArrayList<User>();
        while (cursor != 0) {
            PagableResponseList<User> l = twitter.getFriendsList(user.getId(), cursor);
            following.addAll(l);
            cursor = l.getNextCursor();
        }
        System.out.println(following);
    }
    
    public TwitterClient(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String consumerKey = br.readLine();
        String consumerSecret = br.readLine();
        String accessToken = br.readLine();
        String tokenSecret = br.readLine();
        br.close();
        
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(tokenSecret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
    }

    public void authenticate() throws IOException {
        // TODO Auto-generated method stub
        
    }

    public SocialUser getAuthenticatedUser(){
        try {
            User u = twitter.verifyCredentials();
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        // TODO Auto-generated method stub
        return null;
    }

    public Set<SocialUser> getInterestingUsers() {
        // TODO Auto-generated method stub
        return null;
    }

    public List<SocialUser> getFollowing(int num) {
        // TODO Auto-generated method stub
        return null;
    }
    
    private class TwitterUser implements SocialUser {
        private User user;
        
        public String getName() {
            return user.getName();
        }

        public String getTitle() {
            return user.getScreenName();
        }

        public String getDescription() {
            return user.getDescription();
        }

        public PostSet getPosts(int num) {
            
            // TODO Auto-generated method stub
            return null;
        }

        public int getPostCount() {
            // TODO Auto-generated method stub
            return 0;
        }

        public List<SocialUser> getFollowers() {
            // TODO Auto-generated method stub
            return null;
        }

        public long getLastUpdated() {
            // TODO Auto-generated method stub
            return 0;
        }
        
    }
}
