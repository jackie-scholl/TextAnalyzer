package net.unto.twitter.methods;

import java.util.List;

import net.unto.twitter.JsonUtil;
import net.unto.twitter.TwitterUtil;
import net.unto.twitter.TwitterProtos.Status;

import org.joda.time.DateTime;

// http://apiwiki.twitter.com/REST+API+Documentation#replies
  
public final class RepliesRequest extends AbstractRequest {
  
  public static Builder builder() {
    return new Builder();
  }
  
  RepliesRequest(Builder builder) {
    super(builder);
  }
  
  public static final class Builder extends AbstractRequest.Builder<Builder> {

    Builder() {
      path("/statuses/replies.json");
      authorizationRequired(true);
    }
    
    public RepliesRequest build() {
      return new RepliesRequest(this);
    }

    public Builder since(DateTime since) {
      assert (since != null);
      return parameter("since", TwitterUtil.toString(since));
    }

    public Builder sinceId(long sinceId) {
      return parameter("since_id", Long.toString(sinceId));
    }

    public Builder page(int page) {
      return parameter("page", Integer.toString(page));
    }    
  }
  
  public List<Status> get() {
    return JsonUtil.newStatusList(getJson());
  }
}

