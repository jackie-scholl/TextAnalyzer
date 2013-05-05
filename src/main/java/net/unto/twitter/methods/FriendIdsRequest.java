package net.unto.twitter.methods;

import net.unto.twitter.JsonUtil;

/**
 * Returns an array of numeric IDs for every user the specified user is following.
 */
public final class FriendIdsRequest extends AbstractRequest {

  public static Builder builder() {
    return new Builder();
  }
  
  FriendIdsRequest(Builder builder) {
    super(builder);
  }
  
  public static final class Builder extends AbstractRequest.Builder<Builder> {

    Builder() {
      path("/friends/ids.json");
      authorizationRequired(true);
    }
    
    /**
     *  The ID or screen_name of the user to retrieve the friends ID list for.
     * 
     * @param id  The ID or screen_name of the user to retrieve the friends ID list for.
     * @return {@link FriendIdsRequest}
     */
    public Builder id(String id) {
      assert (id != null);
      path(String.format("/friends/ids/%s.json", id));
      authorizationRequired(false);
      return this;
    }
    
    public FriendIdsRequest build()  {
      return new FriendIdsRequest(this);
    }    
  }
  
  /**
   * Returns an array of numeric IDs for every user the specified user is following.
   * 
   * @return array of user ids
   */
  public long[] get() {
    return JsonUtil.newLongArray(getJson());
  }
}
