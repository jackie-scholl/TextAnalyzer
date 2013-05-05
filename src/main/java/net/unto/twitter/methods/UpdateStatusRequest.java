package net.unto.twitter.methods;

import net.unto.twitter.JsonUtil;
import net.unto.twitter.TwitterProtos.Status;

// http://apiwiki.twitter.com/REST+API+Documentation#update
  
/**
 * Updates the authenticating user's status. Requires the status parameter
 * specified below. Request must be a POST. A status update with text identical
 * to the authenticating user's current status will be ignored.
 */
public final class UpdateStatusRequest extends AbstractRequest {

  /**
   * Returns a UpdateStatusRequest.Builder to update the authenticating user's
   * status.
   * 
   * @param status The text of your status update. Should not be more than 140
   *        characters.
   * @return {@link UpdateStatusRequest.Builder}
   */
  public static Builder builder(String status) {
    return new Builder(status);
  }

  UpdateStatusRequest(Builder builder) {
    super(builder);
  }

  public static final class Builder extends AbstractRequest.Builder<Builder> {

    Builder(String status) {
      assert (status != null);
      assert (status.length() > 0);
      assert (status.length() <= 160);
      path("/statuses/update.json");
      parameter("status", status);
      authorizationRequired(true);
    }

    /**
     * The ID of an existing status that the status to be posted is in reply to.
     * This implicitly sets the in_reply_to_user_id attribute of the resulting
     * status to the user ID of the message being replied to. Invalid/missing
     * status IDs will be ignored.
     * 
     * @param inReplyToStatusId The ID of an existing status that the status to
     *        be posted is in reply to. This implicitly sets the
     *        in_reply_to_user_id attribute of the resulting status to the user
     *        ID of the message being replied to. Invalid/missing status IDs
     *        will be ignored.
     * @return {@link UpdateStatusRequest.Builder}
     */
    public Builder inReplyToStatusId(long inReplyToStatusId) {
      return parameter("in_reply_to_status_id", Long
          .toString(inReplyToStatusId));
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.unto.twitter.methods.Request.Builder#build()
     */
    public UpdateStatusRequest build() {
      return new UpdateStatusRequest(this);
    }
  }

  public Status post() {
    return JsonUtil.newStatus(postJson());
  }
}
