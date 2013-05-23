package scholl.both.analyzer.social.networks;

import scholl.both.analyzer.social.User;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface Client {
    
    public abstract void authenticate() throws IOException;
    
    public abstract User getAuthenticatedUser();
    
    public abstract Set<User> getInterestingUsers();
    
    public abstract List<User> getFollowing(int num);
    
    public abstract User getUser(String name);
    
}
