package net.unto.twitter.methods;

import net.unto.twitter.JsonUtil;
import net.unto.twitter.TwitterProtos.User;

/**
 * Un-blocks the user specified in the ID parameter as the authenticating user.
 * Returns the un-blocked user in the requested format when successful.
 */
public final class DestroyBlockRequest extends AbstractRequest {

  public static Builder builder(String id) {
    return new Builder(id);
  }

  DestroyBlockRequest(Builder builder) {
    super(builder);
  }

  public static final class Builder extends AbstractRequest.Builder<Builder> {

    /**
     * @param id The ID or screen_name of the user to un-block.
     */
    Builder(String id) {
      path(String.format("/blocks/destroy/%s.json", id));
      authorizationRequired(true);
    }

    public DestroyBlockRequest build() {
      return new DestroyBlockRequest(this);
    }
  }

  public User post() {
    return JsonUtil.newUser(postJson());
  }
}
