package net.unto.twitter.methods;

import java.util.List;

import net.unto.twitter.JsonUtil;
import net.unto.twitter.TwitterUtil;
import net.unto.twitter.TwitterProtos.Status;

import org.joda.time.DateTime;

// http://apiwiki.twitter.com/REST+API+Documentation#friendstimeline

public final class FriendsTimelineRequest extends AbstractRequest {
  
  public static Builder builder() {
    return new Builder();
  }
  
  FriendsTimelineRequest(Builder builder) {
    super(builder);
  }
  
  public static final class Builder extends AbstractRequest.Builder<Builder> {

    Builder() {
      path("/statuses/friends_timeline.json");
      authorizationRequired(true);
    }
    
    public FriendsTimelineRequest build() {
      return new FriendsTimelineRequest(this);
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

