package net.unto.twitter.methods;

import net.unto.twitter.JsonUtil;
import net.unto.twitter.TwitterProtos.Status;

// http://apiwiki.twitter.com/REST+API+Documentation#show
  
public final class ShowStatusRequest extends AbstractRequest {
  
  public static Builder builder(long id) {
    return new Builder(id);
  }
  
  ShowStatusRequest(Builder builder) {
    super(builder);
  }
  
  public static final class Builder extends AbstractRequest.Builder<Builder> {

    Builder(long id) {
      path(String.format("/statuses/show/%s.json", id));
      authorizationRequired(false);
    }
    
    Builder() {
      authorizationRequired(false);
    }

    public ShowStatusRequest build() {
      return new ShowStatusRequest(this);
    }
  }

  public Status get() {
    return JsonUtil.newStatus(getJson());
  }
}
