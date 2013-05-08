package scholl.both.analyzer.social.networks;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TwitterClient {
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
}
