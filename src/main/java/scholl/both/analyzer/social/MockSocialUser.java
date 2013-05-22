package scholl.both.analyzer.social;

import java.util.ArrayList;
import java.util.List;

public class MockSocialUser implements SocialUser {
    private final String name;
    
    public MockSocialUser(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return String.format("Name is %s", getName());
    }

    public String getDescription() {
        return String.format("Hello! I am a mock user. My name is %s.", getName());
    }

    public PostSet getPosts(int num) {
        return new PostSet();
    }

    public int getPostCount() {
        return 0;
    }

    public List<SocialUser> getFollowers() {
        return new ArrayList<SocialUser>();
    }

    public long getLastUpdated() {
        return System.currentTimeMillis();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MockSocialUser other = (MockSocialUser) obj;
        if (this.name == null) {
            if (other.name != null)
                return false;
        } else if (!this.name.equals(other.name))
            return false;
        return true;
    }
    
}
