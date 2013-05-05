package net.unto.twitter.methods;

import net.unto.twitter.JsonUtil;

/**
 * Returns an array of numeric IDs for every user the specified user is followed by.
 */
public final class FollowerIdsRequest extends AbstractRequest {

  public static Builder builder() {
    return new Builder();
  }
  
  FollowerIdsRequest(Builder builder) {
    super(builder);
  }
  
  public static final class Builder extends AbstractRequest.Builder<Builder> {

    Builder() {
      path("/followers/ids.json");
      authorizationRequired(true);
    }
    
    /**
     *  The ID or screen_name of the user to retrieve the followers ID list for.
     * 
     * @param id  The ID or screen_name of the user to retrieve the followers ID list for.
     * @return {@link FollowerIdsRequest}
     */
    public Builder id(String id) {
      assert (id != null);
      path(String.format("/followers/ids/%s.json", id));
      authorizationRequired(false);
      return this;
    }
    
    public FollowerIdsRequest build()  {
      return new FollowerIdsRequest(this);
    }    
  }
  
  /**
   * Returns an array of numeric IDs for every user the specified user is followed by.
   * 
   * @return array of user ids
   */
  public long[] get() {
    return JsonUtil.newLongArray(getJson());
  }
}
