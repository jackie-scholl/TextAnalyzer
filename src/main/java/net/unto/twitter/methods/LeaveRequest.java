package net.unto.twitter.methods;

import net.unto.twitter.JsonUtil;
import net.unto.twitter.TwitterProtos.User;

/**
 * Disables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
 * <p>
 * The Notification Methods require the authenticated user to already be friends with the specified user otherwise the error "there was a problem following the specified user" will be returned.
 * </p>
 */
public final class LeaveRequest extends AbstractRequest {

  public static Builder builder(String id) {
    return new Builder(id);
  }

  LeaveRequest(Builder builder) {
    super(builder);
  }

  public static final class Builder extends AbstractRequest.Builder<Builder> {

    /**
     * @param id The ID or screen name of the user to leave.
     */
    Builder(String id) {
      path(String.format("/notifications/leave/%s.json", id));
      authorizationRequired(true);
    }

    public LeaveRequest build() {
      return new LeaveRequest(this);
    }
  }

  public User post() {
    return JsonUtil.newUser(postJson());
  }
}
