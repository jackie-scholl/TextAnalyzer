package scholl.both.analyzer.social.networks;

import scholl.both.analyzer.social.SocialUser;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface SocialClient {
    
    public abstract void authenticate() throws IOException;
    
    public abstract SocialUser getAuthenticatedUser();
    
    public abstract Set<SocialUser> getInterestingUsers();
    
    public abstract List<SocialUser> getFollowing(int num);
    
    public abstract SocialUser getUser(String name);
    
}
