package net.unto.twitter.methods;

import java.util.List;

import net.unto.twitter.JsonUtil;
import net.unto.twitter.TwitterProtos.Status;

/**
 * Returns the 20 most recent favorite statuses for the authenticating user or
 * user specified by the ID parameter in the requested format.
 */
public final class FavoritesRequest extends AbstractRequest {

  public static Builder builder() {
    return new Builder();
  }

  FavoritesRequest(Builder builder) {
    super(builder);
  }

  public static final class Builder extends AbstractRequest.Builder<Builder> {

    Builder() {
      path("/favorites.json");
      authorizationRequired(true);
    }

    /**
     * The ID or screen name of the user for whom to request a list of favorite
     * statuses.
     * 
     * @param id The ID or screen name of the user for whom to request a list of
     *        favorite statuses.
     * @return {@link FavoritesRequest}
     */
    public Builder id(String id) {
      assert (id != null);
      path(String.format("/favorites/%s.json", id));
      authorizationRequired(false);
      return this;
    }

    /**
     * Retrieves the 20 next most recent favorite statuses.
     * 
     * @param page Retrieves the 20 next most recent favorite statuses.
     * @return {@link FavoritesRequest}
     */
    public Builder page(int page) {
      return parameter("page", Integer.toString(page));
    }

    public FavoritesRequest build() {
      return new FavoritesRequest(this);
    }
  }

  /**
   * Returns the 20 most recent favorite statuses for the authenticating user or
   * user specified by the ID parameter in the requested format.
   * 
   * @return {@link List} of {@link Status}
   */
  public List<Status> get() {
    return JsonUtil.newStatusList(getJson());
  }
}
