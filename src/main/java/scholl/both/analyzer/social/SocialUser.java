package scholl.both.analyzer.social;

//TODO: Add Javadocs

public interface SocialUser {

    public abstract String getName();
    
    public abstract PostSet getPosts(int num);

    public abstract int hashCode();

    public abstract boolean equals(Object obj);

}
