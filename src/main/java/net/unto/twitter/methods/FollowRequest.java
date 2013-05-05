package net.unto.twitter.methods;

import net.unto.twitter.JsonUtil;
import net.unto.twitter.TwitterProtos.User;

/**
 * Enables notifications for updates from the specified user to the
 * authenticating user. Returns the specified user when successful.
 * <p>
 * The Notification Methods require the authenticated user to already be friends with the specified user otherwise the error "there was a problem following the specified user" will be returned.
 * </p>
 * 
 */
public final class FollowRequest extends AbstractRequest {

  public static Builder builder(String id) {
    return new Builder(id);
  }

  FollowRequest(Builder builder) {
    super(builder);
  }

  public static final class Builder extends AbstractRequest.Builder<Builder> {

    /**
     * @param id The ID or screen name of the user to follow.
     */
    Builder(String id) {
      path(String.format("/notifications/follow/%s.json", id));
      authorizationRequired(true);
    }

    public FollowRequest build() {
      return new FollowRequest(this);
    }
  }

  public User post() {
    return JsonUtil.newUser(postJson());
  }
}
