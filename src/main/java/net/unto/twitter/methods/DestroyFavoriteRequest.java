package net.unto.twitter.methods;

import net.unto.twitter.JsonUtil;
import net.unto.twitter.TwitterProtos.User;

/**
 * Un-favorites the status specified in the ID parameter as the authenticating
 * user. Returns the un-favorited status in the requested format when
 * successful.
 */
public final class DestroyFavoriteRequest extends AbstractRequest {

  public static Builder builder(long id) {
    return new Builder(id);
  }

  DestroyFavoriteRequest(Builder builder) {
    super(builder);
  }

  public static final class Builder extends AbstractRequest.Builder<Builder> {

    /**
     * @param id The ID of the status to un-favorite.
     */
    Builder(long id) {
      path(String.format("/favorites/destroy/%s.json", id));
      authorizationRequired(true);
    }

    public DestroyFavoriteRequest build() {
      return new DestroyFavoriteRequest(this);
    }
  }

  public User post() {
    return JsonUtil.newUser(postJson());
  }
}
