package net.unto.twitter.methods;

import java.util.List;

import net.unto.twitter.JsonUtil;
import net.unto.twitter.TwitterUtil;
import net.unto.twitter.TwitterProtos.Status;

import org.joda.time.DateTime;

// http://apiwiki.twitter.com/REST+API+Documentation#usertimeline
  
public final class UserTimelineRequest extends AbstractRequest {
  
  public static Builder builder() {
    return new Builder();
  }
  
  UserTimelineRequest(Builder builder) {
    super(builder);
  }
  
  public static final class Builder extends AbstractRequest.Builder<Builder> {

    Builder() {
      path("/statuses/user_timeline.json");
      authorizationRequired(true);
    }
    
    public UserTimelineRequest build() {
      return new UserTimelineRequest(this);
    }
    
    public Builder id(String id) {
      assert (id != null);
      path(String.format("/statuses/user_timeline/%s.json", id));
      return this;
    }

    public Builder since(DateTime since) {
      assert (since != null);
      return parameter("since", TwitterUtil.toString(since));
    }

    public Builder sinceId(long sinceId) {
      return parameter("since_id", Long.toString(sinceId));
    }

    public Builder count(int count) {
      return parameter("count", Integer.toString(count));
    }

    public Builder page(int page) {
      return parameter("page", Integer.toString(page));
    }
    
  }
  
  public List<Status> get() {
    return JsonUtil.newStatusList(getJson());
  }
}

