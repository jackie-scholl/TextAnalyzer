package scholl.both.analyzer.social.networks;

import scholl.both.analyzer.social.PostSet;
import scholl.both.analyzer.social.Post;
import scholl.both.analyzer.social.User;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;
import java.util.*;

public class TwitterClient implements Client {
    private Twitter twitter;
    
    public TwitterClient(String fileName) throws IOException {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
    }
    
    public void authenticate() throws IOException {
        ;
    }
    
    public User getAuthenticatedUser() {
        User u = null;
        for (int i = 0; i < 5; i++) {
            try {
                u = new TwitterUser(twitter.verifyCredentials());
                return u;
            } catch (TwitterException e) {
                System.out.printf("Failed to get authenticated user; attempt %d%n", i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e2) {
                    ;
                }
            }
        }
        
        return u;
    }
    
    public Set<User> getInterestingUsers() {
        Set<User> s = new HashSet<User>();
        s.addAll(getFollowing(20));
        return s;
    }
    
    public List<User> getFollowing(int num) {
        try {
            List<User> b = new ArrayList<User>();
            for (twitter4j.User u : twitter.getFriendsList(twitter.verifyCredentials().getId(), -1)) {
                b.add(new TwitterUser(u));
            }
            return b;
        } catch (TwitterException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public User getUser(String name) {
        try {
            return new TwitterUser(twitter.showUser(name));
        } catch (TwitterException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Map<String, RateLimitStatus> getRateLimit() {
        try {
            return twitter.getRateLimitStatus();
        } catch (TwitterException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private class TwitterUser implements User {
        private twitter4j.User user;
        
        public TwitterUser(twitter4j.User u) {
            this.user = u;
        }
        
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
            try {
                ResponseList<Status> l = twitter.getUserTimeline(user.getId());
                PostSet ps = new PostSet();
                for (Status s : l) {
                    ps.add(getSocial(s));
                }
                return ps;
            } catch (TwitterException e) {
                e.printStackTrace();
                return null;
            }
        }
        
        public int getPostCount() {
            // TODO Auto-generated method stub
            return 0;
        }
        
        public List<User> getFollowers() {
            try {
                List<User> ls = new ArrayList<User>();
                
                for (twitter4j.User u : twitter.getFollowersList(user.getId(), -1)) {
                    ls.add(new TwitterUser(u));
                }
                
                return ls;
            } catch (TwitterException e) {
                e.printStackTrace();
                return null;
            }
        }
        
        public long getLastUpdated() {
            return getPosts(1).getMostRecent().getTimestamp();
        }
        
        @Override
        public String toString() {
            return getName();
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + this.getName().hashCode();
            return result;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (!(obj instanceof TwitterUser))
                return false;
            TwitterUser other = (TwitterUser) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (!this.getName().equals(other.getName()))
                return false;
            return true;
        }
        
        private TwitterClient getOuterType() {
            return TwitterClient.this;
            
        }
        
    }
    
    private scholl.both.analyzer.social.Post getSocial(Status s) {
        return new scholl.both.analyzer.social.Post(s.getText(), s.getCreatedAt().getTime(),
                new TwitterUser(s.getUser()));
    }
}
