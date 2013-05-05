package net.unto.twitter.methods;

import net.unto.twitter.JsonUtil;
import net.unto.twitter.TwitterProtos.Relationship;

/**
 * Returns detailed information about the relationship between two users.
 *
 * <p>Authentication is required unless sourceId or sourceScreenName are set.
 */
public final class ShowFriendshipsRequest extends AbstractRequest {

  public static Builder builder() {
    return new Builder();
  }

  ShowFriendshipsRequest(Builder builder) {
    super(builder);
  }

  public static final class Builder extends AbstractRequest.Builder<Builder> {

    Builder() {
      path(String.format("/friendships/show.json"));
      authorizationRequired(true);
    }

    /**
     * The user_id of the subject user.
     *
     * @param sourceId The user_id of the subject user.
     * @return {@link Builder}
     */
    public Builder sourceId(long sourceId) {
      authorizationRequired(false);
      return parameter("source_id", Long.toString(sourceId));
    }

    /**
     * The screen_name of the subject user.
     *
     * @param sourceScreenName The screen_name of the subject user.
     * @return {@link Builder}
     */
    public Builder sourceScreenName(String sourceScreenName) {
      authorizationRequired(false);
      return parameter("source_screen_name", sourceScreenName);
    }

    /**
     * The user_id of the subject user.
     *
     * @param targetId The user_id of the subject user.
     * @return {@link Builder}
     */
    public Builder targetId(long targetId) {
      return parameter("target_id", Long.toString(targetId));
    }

    /**
     * The screen_name of the subject user.
     *
     * @param targetScreenName The screen_name of the subject user.
     * @return {@link Builder}
     */
    public Builder targetScreenName(String targetScreenName) {
      return parameter("target_screen_name", targetScreenName);
    }
    
    public ShowFriendshipsRequest build() {
      return new ShowFriendshipsRequest(this);
    }
  }

  public Relationship get() {
    return JsonUtil.newRelationship(getJson());
  }
}
