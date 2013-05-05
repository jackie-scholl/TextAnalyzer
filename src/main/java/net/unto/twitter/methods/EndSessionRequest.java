package net.unto.twitter.methods;

/**
 * Ends the session of the authenticating user.
 */
public final class EndSessionRequest extends AbstractRequest {

  public static Builder builder() {
    return new Builder();
  }
  
  EndSessionRequest(Builder builder) {
    super(builder);
  }
  
  public static final class Builder extends AbstractRequest.Builder<Builder> {

    Builder() {
      path("/account/end_session.json");
      authorizationRequired(true);
    }
    
    public EndSessionRequest build()  {
      return new EndSessionRequest(this);
    }    
  }
  
  /**
   * Ends the session of the authenticating user.
   */
  public void post() {
    postJson();
    httpManager.clearCredentials();
  }
}
