package net.unto.twitter.methods;

import java.util.List;

import net.unto.twitter.JsonUtil;
import net.unto.twitter.TwitterUtil;
import net.unto.twitter.TwitterProtos.DirectMessage;

import org.joda.time.DateTime;

// http://apiwiki.twitter.com/REST+API+Documentation#directmessages

/**
 * Returns a list of the 20 most recent direct messages sent to the
 * authenticating user.
 */
public final class DirectMessagesRequest extends AbstractRequest {

  public static Builder builder() {
    return new Builder();
  }

  DirectMessagesRequest(Builder builder) {
    super(builder);
  }

  public static final class Builder extends AbstractRequest.Builder<Builder> {

    Builder() {
      path("/direct_messages.json");
      authorizationRequired(true);
    }

    public DirectMessagesRequest build() {
      return new DirectMessagesRequest(this);
    }

    /**
     * Narrows the resulting list of direct messages to just those sent after
     * the specified HTTP-formatted date, up to 24 hours old.
     * 
     * @param since Narrows the resulting list of direct messages to just those
     *        sent after the specified HTTP-formatted date, up to 24 hours old.
     * @return {@link Builder}
     */
    public Builder since(DateTime since) {
      assert (since != null);
      return parameter("since", TwitterUtil.toString(since));
    }

    /**
     * Returns only sent direct messages with an ID greater than (that is, more
     * recent than) the specified ID.
     * 
     * @param sinceId Returns only sent direct messages with an ID greater than
     *        (that is, more recent than) the specified ID.
     * @return {@link Builder}
     */
    public Builder sinceId(long sinceId) {
      return parameter("since_id", Long.toString(sinceId));
    }

    /**
     * Retrieves the 20 next most recent direct messages sent
     * 
     * @param page Retrieves the 20 next most recent direct messages sent
     * @return {@link Builder}
     */
    public Builder page(int page) {
      return parameter("page", Integer.toString(page));
    }

  }

  public List<DirectMessage> get() {
    return JsonUtil.newDirectMessageList(getJson());
  }
}
