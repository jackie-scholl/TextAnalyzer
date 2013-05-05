package net.unto.twitter.methods;

import net.unto.twitter.JsonUtil;
import net.unto.twitter.TwitterProtos.User;

/**
 * Favorites the status specified in the ID parameter as the authenticating
 * user. Returns the favorite status when successful.
 */
public final class CreateFavoriteRequest extends AbstractRequest {

  public static Builder builder(long id) {
    return new Builder(id);
  }

  CreateFavoriteRequest(Builder builder) {
    super(builder);
  }

  public static final class Builder extends AbstractRequest.Builder<Builder> {

    /**
     * @param id The ID of the status to favorite.
     */
    Builder(long id) {
      path(String.format("/favorites/create/%s.json", id));
      authorizationRequired(true);
    }

    public CreateFavoriteRequest build() {
      return new CreateFavoriteRequest(this);
    }
  }

  public User post() {
    return JsonUtil.newUser(postJson());
  }
}
