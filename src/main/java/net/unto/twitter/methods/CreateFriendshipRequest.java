package net.unto.twitter.methods;

import net.unto.twitter.JsonUtil;
import net.unto.twitter.TwitterProtos.User;

/**
 * Sends a new direct message to the specified user from the authenticating user.
 */
public final class CreateFriendshipRequest extends AbstractRequest {

  public static Builder builder(String id) {
    return new Builder(id);
  }

  CreateFriendshipRequest(Builder builder) {
    super(builder);
  }

  public static final class Builder extends AbstractRequest.Builder<Builder> {

    /**
     * @param id  The ID or screen name of the user to befriend.
     */
    Builder(String id) {
      assert(id != null);
      path(String.format("/friendships/create/%s.json", id));
      authorizationRequired(true);
    }

    public CreateFriendshipRequest build() {
      return new CreateFriendshipRequest(this);
    }
    
    /**
     * Enable notifications for the target user in addition to becoming friends. 
     * 
     * @param follow Enable notifications for the target user in addition to becoming friends. 
     * @return {@link Builder}
     */
    public Builder follow(boolean follow) {
      return parameter("follow", Boolean.toString(follow));
    }
  }

  public User post() {
    return JsonUtil.newUser(postJson());
  }
}
