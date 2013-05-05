package net.unto.twitter.methods;

import net.unto.twitter.JsonUtil;
import net.unto.twitter.TwitterProtos.User;

public final class UpdateProfileRequest extends AbstractRequest {

  public static Builder builder() {
    return new Builder();
  }

  UpdateProfileRequest(Builder builder) {
    super(builder);
  }

  public static final class Builder extends AbstractRequest.Builder<Builder> {

    Builder() {
      path("/account/update_profile.json");
      authorizationRequired(true);
    }

    /**
     * Maximum of 20 characters.
     * 
     * @param name Maximum of 20 characters.
     * @return Builder
     */
    public Builder name(String name) {
      assert (name != null);
      assert (name.length() <= 20);
      return parameter("name", name);
    }

    /**
     * Maximum of 40 characters. Must be a valid email address.
     * 
     * @param email Maximum of 40 characters. Must be a valid email address.
     * @return Builder
     */
    public Builder email(String email) {
      assert (email != null);
      assert (email.length() <= 40);
      return parameter("email", email);
    }

    /**
     * Maximum of 100 characters. Will be prepended with "http://" if not
     * present.
     * 
     * @param url Maximum of 100 characters. Will be prepended with "http://" if
     *        not present.
     * @return Builder
     */
    public Builder url(String url) {
      assert (url != null);
      assert (url.length() <= 100);
      return parameter("url", url);
    }

    /**
     * Maximum of 30 characters. The contents are not normalized or geocoded in
     * any way.
     * 
     * @param location Maximum of 30 characters. The contents are not normalized
     *        or geocoded in any way.
     * @return Builder
     */
    public Builder location(String location) {
      assert (location != null);
      assert (location.length() <= 30);
      return parameter("location", location);
    }

    /**
     * Maximum of 160 characters.
     * 
     * @param description Maximum of 160 characters.
     * @return Builder
     */
    public Builder description(String description) {
      assert (description != null);
      assert (description.length() <= 160);
      return parameter("description", description);
    }

    public UpdateProfileRequest build() {
      return new UpdateProfileRequest(this);
    }
  }

  public User post() {
    return JsonUtil.newUser(postJson());
  }
}
