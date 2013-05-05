package net.unto.twitter.methods;

import net.unto.twitter.JsonUtil;
import net.unto.twitter.TwitterProtos.RateLimitStatus;

public final class RateLimitStatusRequest extends AbstractRequest {
  
  public static Builder builder() {
    return new Builder();
  }
  
  RateLimitStatusRequest(Builder builder) {
    super(builder);
  }
  
  public static final class Builder extends AbstractRequest.Builder<Builder> {

    Builder() {
      path("/account/rate_limit_status.json");
      authorizationRequired(false);
    }
    
    public RateLimitStatusRequest build() {
      return new RateLimitStatusRequest(this);
    }
  }
  
  public RateLimitStatus get() {
    return JsonUtil.newRateLimitStatus(getJson());
  }
}

