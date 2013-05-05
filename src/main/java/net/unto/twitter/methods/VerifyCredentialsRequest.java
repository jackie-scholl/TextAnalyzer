package net.unto.twitter.methods;

import net.unto.twitter.JsonUtil;
import net.unto.twitter.TwitterProtos.User;

/**
 * Returns a representation of the requesting user if authentication was successful.
 */
public final class VerifyCredentialsRequest extends AbstractRequest {

  public static Builder builder() {
    return new Builder();
  }
  
  VerifyCredentialsRequest(Builder builder) {
    super(builder);
  }
  
  public static final class Builder extends AbstractRequest.Builder<Builder> {

    Builder() {
      path("/account/verify_credentials.json");
      authorizationRequired(true);
    }
    
    public VerifyCredentialsRequest build()  {
      return new VerifyCredentialsRequest(this);
    }    
  }
  
  /**
   * Returns a representation of the requesting user if authentication was successful.
   * 
   * @return array of user ids
   */
  public User get() {
    // TODO(dewitt): This should do something smart if the credentials fail
    return JsonUtil.newUser(getJson());
  }
}
