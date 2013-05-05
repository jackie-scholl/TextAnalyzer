package net.unto.twitter;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.unto.twitter.TwitterProtos.DirectMessage;
import net.unto.twitter.TwitterProtos.Relationship;
import net.unto.twitter.TwitterProtos.RateLimitStatus;
import net.unto.twitter.TwitterProtos.Results;
import net.unto.twitter.TwitterProtos.Status;
import net.unto.twitter.TwitterProtos.Trends;
import net.unto.twitter.TwitterProtos.User;
import net.unto.twitter.TwitterProtos.Results.Result;
import net.unto.twitter.TwitterProtos.Trends.Trend;

public abstract class JsonUtil {

  private final static User newUser(JSONObject jsonObject) {
    if (jsonObject == null || jsonObject.isNullObject()) {
      return null;
    }
    User.Builder builder = User.newBuilder();
    builder.setId(jsonObject.optLong("id"));
    builder.setName(jsonObject.optString("name"));
    builder.setScreenName(jsonObject.optString("screen_name"));
    builder.setLocation(jsonObject.optString("location"));
    builder.setDescription(jsonObject.optString("description"));
    builder.setProfile(newUserProfile(jsonObject));
    builder.setUrl(jsonObject.optString("url"));
    builder.setProtected(jsonObject.optBoolean("protected"));
    builder.setFollowersCount(jsonObject.optInt("followers_count"));
    builder.setFriendsCount(jsonObject.optInt("friends_count"));
    builder.setCreatedAt(jsonObject.optString("created_at"));
    builder.setFavoritesCount(jsonObject.optInt("favorites_count"));
    builder.setUtcOffset(jsonObject.optInt("utc_offset"));
    builder.setTimeZone(jsonObject.optString("time_zone"));
    builder.setFollowing(jsonObject.optBoolean("following"));
    builder.setNotifications(jsonObject.optBoolean("notifications"));
    builder.setStatusesCount(jsonObject.optInt("statuses_count"));
    if (jsonObject.has("status")) {
      builder.setStatus(newStatus(jsonObject.optJSONObject("status")));
    }
    return builder.build();
  }

  public final static List<User> newUserList(String jsonString) {
    return newUserList(JSONArray.fromObject(jsonString));
  }

  private final static List<User> newUserList(JSONArray jsonArray) {
    List<User> users = new ArrayList<User>(jsonArray.size());
    for (int i = 0; i < jsonArray.size(); i++) {
      users.add(newUser(jsonArray.getJSONObject(i)));
    }
    return users;
  }

  public final static User newUser(String jsonString) {
    return newUser(JSONObject.fromObject(jsonString));
  }

  private final static User.Profile newUserProfile(JSONObject jsonObject) {
    if (jsonObject == null || jsonObject.isNullObject()) {
      return null;
    }
    User.Profile.Builder builder = User.Profile.newBuilder();
    builder.setImageUrl(jsonObject.optString("profile_image_url"));
    builder
        .setBackgroundColor(jsonObject.optString("profile_background_color"));
    builder.setTextColor(jsonObject.optString("profile_text_color"));
    builder.setLinkColor(jsonObject.optString("profile_link_color"));
    builder.setSidebarFillColor(jsonObject
        .optString("profile_sidebar_fill_color"));
    builder.setSidebarBorderColor(jsonObject
        .optString("profile_sidebar_border_color"));
    return builder.build();
  }

  public final static Status newStatus(String jsonString) {
    return newStatus(JSONObject.fromObject(jsonString));
  }

  private final static Status newStatus(JSONObject jsonObject) {
    if (jsonObject == null || jsonObject.isNullObject()) {
      return null;
    }
    Status.Builder builder = Status.newBuilder();
    builder.setCreatedAt(jsonObject.optString("created_at"));
    builder.setId(jsonObject.optLong("id"));
    builder.setText(jsonObject.optString("text"));
    builder.setSource(jsonObject.optString("source"));
    builder.setTruncated(jsonObject.optBoolean("truncated"));
    builder.setInReplyToStatusId(jsonObject.optLong("in_reply_to_status_id"));
    builder.setInReplyToUserId(jsonObject.optLong("in_reply_to_user_id"));
    builder.setFavorited(jsonObject.optBoolean("favorited"));
    if (jsonObject.has("user")) {
      builder.setUser(newUser(jsonObject.optJSONObject("user")));
    }
    return builder.build();
  }

  public final static List<Status> newStatusList(String jsonString) {
    return newStatusList(JSONArray.fromObject(jsonString));
  }

  private final static List<Status> newStatusList(JSONArray jsonArray) {
    List<Status> statuses = new ArrayList<Status>(jsonArray.size());
    for (int i = 0; i < jsonArray.size(); i++) {
      statuses.add(newStatus(jsonArray.getJSONObject(i)));
    }
    return statuses;
  }

  private final static DirectMessage newDirectMessage(JSONObject jsonObject) {
    if (jsonObject == null || jsonObject.isNullObject()) {
      return null;
    }
    DirectMessage.Builder builder = DirectMessage.newBuilder();
    builder.setId(jsonObject.optLong("id"));
    builder.setText(jsonObject.optString("text"));
    builder.setSenderId(jsonObject.optLong("sender_id"));
    builder.setRecipientId(jsonObject.optLong("recipient_id"));
    builder.setCreatedAt(jsonObject.optString("created_at"));
    builder.setSenderScreenName(jsonObject.optString("sender_screen_name"));
    builder.setRecipientScreenName(jsonObject
        .optString("recipient_screen_name"));
    builder.setSender(newUser(jsonObject.optJSONObject("sender")));
    builder.setRecipient(newUser(jsonObject.optJSONObject("recipient")));
    return builder.build();
  }

  public final static List<DirectMessage> newDirectMessageList(String jsonString) {
    return newDirectMessageList(JSONArray.fromObject(jsonString));
  }

  private final static List<DirectMessage> newDirectMessageList(
      JSONArray jsonArray) {
    List<DirectMessage> directMessages = new ArrayList<DirectMessage>(jsonArray
        .size());
    for (int i = 0; i < jsonArray.size(); i++) {
      directMessages.add(newDirectMessage(jsonArray.getJSONObject(i)));
    }
    return directMessages;
  }

  public final static DirectMessage newDirectMessage(String jsonString) {
    return newDirectMessage(JSONObject.fromObject(jsonString));
  }

  public final static long[] newLongArray(String jsonString) {
    return newLongArray(JSONArray.fromObject(jsonString));
  }

  private final static long[] newLongArray(JSONArray jsonArray) {
    long[] longs = new long[jsonArray.size()];
    for (int i = 0; i < jsonArray.size(); i++) {
      longs[i] = jsonArray.getLong(i);
    }
    return longs;
  }

  public final static Trends newTrends(String jsonString) {
    return newTrends(JSONObject.fromObject(jsonString));
  }

  private final static Trends newTrends(JSONObject jsonObject) {
    if (jsonObject == null || jsonObject.isNullObject()) {
      return null;
    }
    Trends.Builder builder = Trends.newBuilder();
    builder.setAsOf(jsonObject.optString("as_of"));
    if (jsonObject.has("trends")) {
      JSONArray trendsJsonArray = jsonObject.getJSONArray("trends");
      for (int i = 0; i < trendsJsonArray.size(); i++) {
        builder.addTrends(newTrend(trendsJsonArray.getJSONObject(i)));
      }
    }
    return builder.build();
  }

  private final static Trend newTrend(JSONObject jsonObject) {
    if (jsonObject == null) {
      return null;
    }
    Trends.Trend.Builder builder = Trends.Trend.newBuilder();
    builder.setName(jsonObject.optString("name"));
    builder.setUrl(jsonObject.optString("url"));
    return builder.build();
  }

  public final static Results newResults(String jsonString) {
    return newResults(JSONObject.fromObject(jsonString));
  }

  private final static Results newResults(JSONObject jsonObject) {
    if (jsonObject == null || jsonObject.isNullObject()) {
      return null;
    }
    Results.Builder builder = Results.newBuilder();
    builder.setCompletedIn(jsonObject.optDouble("completed_in"));
    builder.setMaxId(jsonObject.optLong("max_id"));
    builder.setNextPage(jsonObject.optString("next_page"));
    builder.setPage(jsonObject.optInt("page"));
    builder.setQuery(jsonObject.optString("query"));
    builder.setRefreshUrl(jsonObject.optString("refresh_url"));
    if (jsonObject.has("results")) {
      JSONArray resultsJsonArray = jsonObject.getJSONArray("results");
      for (int i = 0; i < resultsJsonArray.size(); i++) {
        builder.addResults(newResult(resultsJsonArray.getJSONObject(i)));
      }
    }
    return builder.build();
  }

  private final static Result newResult(JSONObject jsonObject) {
    if (jsonObject == null || jsonObject.isNullObject()) {
      return null;
    }
    Results.Result.Builder builder = Results.Result.newBuilder();
    builder.setCreatedAt(jsonObject.optString("created_at"));
    builder.setFromUserId(jsonObject.optLong("from_user_id"));
    builder.setFromUser(jsonObject.optString("from_user"));
    builder.setId(jsonObject.optLong("id"));
    builder.setIsoLanguageCode(jsonObject.optString("iso_language_code"));
    builder.setProfileImageUrl(jsonObject.optString("profile_image_url"));
    builder.setSource(jsonObject.optString("source"));
    builder.setText(jsonObject.optString("text"));
    builder.setToUserId(jsonObject.optLong("to_user_id"));
    builder.setToUser(jsonObject.optString("to_user"));
    builder.setResultsPerPage(jsonObject.optInt("results_per_page"));
    builder.setSinceId(jsonObject.optLong("since_id"));
    return builder.build();
  }

  public static RateLimitStatus newRateLimitStatus(String jsonString) {
    return newRateLimitStatus(JSONObject.fromObject(jsonString));
  }

  private final static RateLimitStatus newRateLimitStatus(JSONObject jsonObject) {
    if (jsonObject == null || jsonObject.isNullObject()) {
      return null;
    }
    RateLimitStatus.Builder builder = RateLimitStatus.newBuilder();
    builder.setHourlyLimit(jsonObject.optInt("hourly_limit"));
    builder.setResetTime(jsonObject.optString("reset_time"));
    builder.setResetTimeInSeconds(jsonObject.optLong("reset_time_in_seconds"));
    builder.setRemainingHits(jsonObject.optLong("remaining_hits"));
    return builder.build();
  }

  public static Relationship newRelationship(String jsonString) {
    return newRelationship(JSONObject.fromObject(jsonString));
  }

  private final static Relationship newRelationship(JSONObject jsonObject) {
    if (jsonObject == null || jsonObject.isNullObject() || !jsonObject.has("relationship")) {
      return null;
    }
    JSONObject relationshipObject = jsonObject.getJSONObject("relationship");
    Relationship.Builder builder = Relationship.newBuilder();
    if (relationshipObject.has("source")) {
      builder.setSource(newRelationshipUser(relationshipObject.optJSONObject("source")));
    }
    if (relationshipObject.has("target")) {
      builder.setTarget(newRelationshipUser(relationshipObject.optJSONObject("target")));
    }
    return builder.build();
  }

  private final static Relationship.User newRelationshipUser(JSONObject jsonObject) {
    if (jsonObject == null || jsonObject.isNullObject()) {
      return null;
    }
    Relationship.User.Builder builder = Relationship.User.newBuilder();
    builder.setId(jsonObject.optLong("id"));
    builder.setScreenName(jsonObject.optString("screen_name"));
    builder.setFollowing(jsonObject.optBoolean("following"));
    builder.setFollowedBy(jsonObject.optBoolean("followed_by"));
    builder.setNotificationsEnabled(jsonObject.optBoolean("notifications_enabled"));
    builder.setBlocking(jsonObject.optBoolean("blocking"));
    return builder.build();
  }
}
