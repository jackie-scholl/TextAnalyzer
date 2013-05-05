package net.unto.twitter.methods;

import net.unto.twitter.JsonUtil;
import net.unto.twitter.TwitterProtos.DirectMessage;

/**
 * Sends a new direct message to the specified user from the authenticating user.
 */
public final class NewDirectMessageRequest extends AbstractRequest {

  public static Builder builder(String user, String text) {
    return new Builder(user, text);
  }

  NewDirectMessageRequest(Builder builder) {
    super(builder);
  }

  public static final class Builder extends AbstractRequest.Builder<Builder> {

    /**
     * @param user The ID or screen name of the recipient user.
     * @param text The text of your direct message.  Keep it under 140 characters.  
     */
    Builder(String user, String text) {
      assert(user != null);
      assert(text != null);
      assert(text.length() <= 140);
      path("/direct_messages/new.json");
      parameter("user", user);
      parameter("text", text);
      authorizationRequired(true);
    }

    public NewDirectMessageRequest build() {
      return new NewDirectMessageRequest(this);
    }
  }

  public DirectMessage post() {
    return JsonUtil.newDirectMessage(postJson());
  }
}
