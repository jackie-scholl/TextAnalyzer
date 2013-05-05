package net.unto.twitter.methods;

import java.util.Map;

import net.unto.twitter.JsonUtil;
import net.unto.twitter.TwitterProtos.Geocode;
import net.unto.twitter.TwitterProtos.Results;

import com.google.common.collect.ImmutableMap;

public final class SearchRequest extends AbstractRequest {

  private final static Map<Geocode.Unit, String> UNIT_NAMES = new ImmutableMap.Builder<Geocode.Unit, String>()
      .put(Geocode.Unit.MILES, "mi").put(Geocode.Unit.KILOMETERS, "km").build();

  public static Builder builder(String query) {
    return new Builder(query);
  }

  SearchRequest(Builder builder) {
    super(builder);
  }

  public static final class Builder extends AbstractRequest.Builder<Builder> {

    Builder(String query) {
      assert (query != null);
      // From the docs: "Query length is limited to 140 characters"
      assert (query.length() <= 140);
      path("/search.json");
      parameter("q", query);
      authorizationRequired(false);
    }

    /**
     * Restricts tweets to the given language, given by an ISO 639-1 code.
     * 
     * @param lang Restricts tweets to the given language, given by an ISO 639-1
     *        code
     * @return Builder
     */
    public Builder lang(String lang) {
      assert (lang != null);
      return parameter("lang", lang);
    }

    /**
     * The number of tweets to return per page, up to a max of 100
     * 
     * @param resultsPerPage the number of tweets to return per page, up to a
     *        max of 100
     * @return Builder
     */
    public Builder resultsPerPage(int resultsPerPage) {
      assert (resultsPerPage >= 0);
      assert (resultsPerPage <= 100);
      return parameter("rpp", Integer.toString(resultsPerPage));
    }

    /**
     * The page number (starting at 1) to return, up to a max of roughly 1500
     * results
     * 
     * @param page The page number (starting at 1) to return, up to a max of
     *        roughly 1500 results
     * @return Builder
     */
    public Builder page(int page) {
      assert (page >= 1);
      return parameter("page", Integer.toString(page));
    }

    public Builder geocode(Geocode geocode) {
      return parameter("geocode", String.format("%s,%s,%s%s", geocode
          .getLatitude(), geocode.getLongitude(), geocode.getRadius(),
          UNIT_NAMES.get(geocode.getUnit())));
    }

    /**
     * When "true", adds "<user>:" to the beginning of the tweet.
     * 
     * @param showUser when "true", adds "<user>:" to the beginning of the
     *        tweet.
     * @return Builder
     */
    public Builder showUser(boolean showUser) {
      return parameter("show_user", Boolean.toString(showUser));
    }

    /**
     * Returns tweets with status ids greater than the given id.
     * 
     * @param sinceId Returns tweets with status ids greater than the given id.
     * @return Builder
     */
    public Builder sinceId(long sinceId) {
      return parameter("since_id", Long.toString(sinceId));
    }

    /**
     * Returns tweets send by the given screen name.
     * 
     * @param screenName Returns tweets send by the given screen name.
     * @return Builder
     */
    public Builder fromScreenName(String screenName) {
      assert (screenName != null);
      return parameter("from", screenName);
    }

    /**
     * Returns tweets send to the given screen name.
     * 
     * @param screenName Returns tweets send to the given screen name.
     * @return Builder
     */
    public Builder toScreenName(String screenName) {
      assert (screenName != null);
      return parameter("to", screenName);
    }

    /**
     * Returns tweets up to and including maxId.
     * 
     * @param maxId Returns tweets up to and including maxId
     * @return Builder
     */
    public Builder maxId(long maxId) {
      return parameter("max_id", Long.toString(maxId));
    }

    public SearchRequest build() {
      // Set the host last, so as not to be overwritten by Api.setDefaults.
      // TODO(dewitt): This will prevent all users from overriding
      // 'search.twitter.com'
      host("search.twitter.com");
      return new SearchRequest(this);
    }
  }

  public Results get() {
    return JsonUtil.newResults(getJson());
  }
}
