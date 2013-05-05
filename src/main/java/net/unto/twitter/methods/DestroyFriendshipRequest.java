package net.unto.twitter.methods;

import net.unto.twitter.JsonUtil;
import net.unto.twitter.TwitterProtos.User;

/**
 * Discontinues friendship with the user specified in the ID parameter as the authenticating user. 
 */
public final class DestroyFriendshipRequest extends AbstractRequest {

  public static Builder builder(String id) {
    return new Builder(id);
  }

  DestroyFriendshipRequest(Builder builder) {
    super(builder);
  }

  public static final class Builder extends AbstractRequest.Builder<Builder> {

    /**
     * @param id  The ID or screen name of the user with whom to discontinue friendship.
     */
    Builder(String id) {
      assert(id != null);
      path(String.format("/friendships/destroy/%s.json", id));
      authorizationRequired(true);
    }

    public DestroyFriendshipRequest build() {
      return new DestroyFriendshipRequest(this);
    }
  }

  public User post() {
    return JsonUtil.newUser(postJson());
  }
}
