package net.unto.twitter.methods;

import net.unto.twitter.JsonUtil;
import net.unto.twitter.TwitterProtos.DirectMessage;
  
public final class DestroyDirectMessageRequest extends AbstractRequest {
  
  public static Builder builder(long id) {
    return new Builder(id);
  }
  
  DestroyDirectMessageRequest(Builder builder)  {
    super(builder);
  }
  
  public static final class Builder extends AbstractRequest.Builder<Builder> {

    Builder(long id) {
      path(String.format("/direct_messages/destroy/%s.json", id));
      authorizationRequired(true);
    }
    
    public DestroyDirectMessageRequest build() {
      return new DestroyDirectMessageRequest(this);
    }
  }

  public DirectMessage post() {
    return JsonUtil.newDirectMessage(postJson());
  }
}
