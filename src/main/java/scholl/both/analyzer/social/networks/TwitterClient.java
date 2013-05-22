package scholl.both.analyzer.social.networks;

import scholl.both.analyzer.social.PostSet;
import scholl.both.analyzer.social.SocialPost;
import scholl.both.analyzer.social.SocialUser;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;
import java.util.*;

public class TwitterClient implements SocialClient {
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
    
    public SocialUser getAuthenticatedUser() {
        SocialUser u = null;
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
    
    public Set<SocialUser> getInterestingUsers() {
        Set<SocialUser> s = new HashSet<SocialUser>();
        s.addAll(getFollowing(20));
        return s;
    }
    
    public List<SocialUser> getFollowing(int num) {
        try {
            List<User> a;
            a = twitter.getFriendsList(twitter.verifyCredentials().getId(), -1);
            List<SocialUser> b = new ArrayList<SocialUser>();
            for (User u : a) {
                b.add(new TwitterUser(u));
            }
            return b;
        } catch (TwitterException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public SocialUser getUser(String name) {
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
    
    private class TwitterUser implements SocialUser {
        private User user;
        private String name;
        
        public TwitterUser(User u) {
            this.user = u;
            this.name = u.getName();
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
        
        public List<SocialUser> getFollowers() {
            try {
                List<User> l = twitter.getFollowersList(user.getId(), -1);
                List<SocialUser> ls = new ArrayList<SocialUser>();
                for (User u : l) {
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
            return this.name;
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
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
            if (this.name == null) {
                if (other.name != null)
                    return false;
            } else if (!this.name.equals(other.name))
                return false;
            return true;
        }
        
        private TwitterClient getOuterType() {
            return TwitterClient.this;
            
        }
        
    }
    
    private SocialPost getSocial(Status s) {
        return new SocialPost(s.getText(), s.getCreatedAt().getTime(), new TwitterUser(s.getUser()));
    }
}
