package net.unto.twitter.methods;

import net.unto.twitter.JsonUtil;
import net.unto.twitter.TwitterProtos.User;

/**
 * Blocks the user specified in the ID parameter as the authenticating user.
 * Returns the blocked user in the requested format when successful.
 */
public final class CreateBlockRequest extends AbstractRequest {

  /**
   * @param id the ID or screen_name of the user to block. 
   * @return A {@link Builder} instance.
   */
  public static Builder builder(String id) {
    return new Builder(id);
  }

  CreateBlockRequest(Builder builder) {
    super(builder);
  }

  public static final class Builder extends AbstractRequest.Builder<Builder> {

    /**
     * @param id The ID or screen_name of the user to block.
     */
    Builder(String id) {
      path(String.format("/blocks/create/%s.json", id));
      authorizationRequired(true);
    }

    /* (non-Javadoc)
     * @see net.unto.twitter.methods.Request.Builder#build()
     */
    public CreateBlockRequest build() {
      return new CreateBlockRequest(this);
    }
  }

  /**
   * @return the blocked user when successful.
   */
  public User post() {
    return JsonUtil.newUser(postJson());
  }
}
