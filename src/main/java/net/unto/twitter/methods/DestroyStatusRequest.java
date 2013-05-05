package net.unto.twitter.methods;

import net.unto.twitter.JsonUtil;
import net.unto.twitter.TwitterProtos.Status;

// http://apiwiki.twitter.com/REST+API+Documentation#destroy
  
public final class DestroyStatusRequest extends AbstractRequest {
  
  public static Builder builder(long id) {
    return new Builder(id);
  }
  
  DestroyStatusRequest(Builder builder)  {
    super(builder);
  }
  
  public static final class Builder extends AbstractRequest.Builder<Builder> {

    Builder(long id) {
      path(String.format("/statuses/destroy/%s.json", id));
      authorizationRequired(true);
    }
    
    public DestroyStatusRequest build() {
      return new DestroyStatusRequest(this);
    }
  }

  public Status post() {
    return JsonUtil.newStatus(postJson());
  }
}
