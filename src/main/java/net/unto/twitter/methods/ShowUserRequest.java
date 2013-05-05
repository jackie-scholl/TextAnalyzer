package net.unto.twitter.methods;

import net.unto.twitter.JsonUtil;
import net.unto.twitter.TwitterProtos.User;

// http://apiwiki.twitter.com/REST+API+Documentation#show

/**
 * Returns extended information of a given user, specified by ID or screen name
 * as per the required id parameter below. This information includes design
 * settings, so third party developers can theme their widgets according to a
 * given user's preferences. You must be properly authenticated to request the
 * page of a protected user.
 */
public final class ShowUserRequest extends AbstractRequest {

  public static Builder builder() {
    return new Builder();
  }

  ShowUserRequest(Builder builder) {
    super(builder);
  }

  public static final class Builder extends AbstractRequest.Builder<Builder> {

    Builder() {
      // One of id, userId, or screenName must be set
      authorizationRequired(true);
      path(null);
    }

    /**
     * The ID or screen name of a user. 
     *
     * @param id The ID or screen name of a user. 
     * @return {@link Builder}
     */
    public Builder id(String id) {
      assert (id != null);
      if (path != null) {
        throw new IllegalStateException(
            "Only one of id, userId, or screenName may be set.");
      }
      path(String.format("/users/show/%s.json", id));
      return this;
    }

    /**
     * The user id of a user.
     *
     * @param userId The user id of a user.
     * @return {@link Builder}
     */
    public Builder userId(long userId) {
      if (path != null) {
        throw new IllegalStateException(
            "Only one of id, userId, or screenName may be set.");
      }
      path("/users/show.json");
      return parameter("user_id", Long.toString(userId));
    }

    /**
     * The screen name of a user
     *
     * @param screenName The screen name of a user.
     * @return {@link Builder}
     */
    public Builder screenName(String screenName) {
      assert (screenName != null);
      if (path != null) {
        throw new IllegalStateException(
            "Only one of id, userId, or screenName may be set.");
      }
      path("/users/show.json");
      return parameter("screen_name", screenName);
    }

    public ShowUserRequest build() {
      if (path == null) {
        throw new IllegalStateException(
            "One of id, userId, or screenName must be set.");
      }
      return new ShowUserRequest(this);
    }
  }

  public User get() {
    return JsonUtil.newUser(getJson());
  }
}
