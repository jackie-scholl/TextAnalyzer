package net.unto.twitter.methods;

import java.util.regex.Pattern;

import net.unto.twitter.JsonUtil;
import net.unto.twitter.TwitterProtos.User;


public final class UpdateProfileColorsRequest extends AbstractRequest {

  static final Pattern VALID_HEXCOLOR_PATTERN = Pattern
      .compile("[\\p{XDigit}]{3}|[\\p{XDigit}]{6}");

  public static Builder builder() {
    return new Builder();
  }

  UpdateProfileColorsRequest(Builder builder) {
    super(builder);
  }

  public static final class Builder extends AbstractRequest.Builder<Builder> {

    Builder() {
      path("/account/update_profile_colors.json");
      authorizationRequired(true);
    }

    private boolean hasValid = false;

    private void validateHexColor(String hexColor) {
      if (isValidHexColor(hexColor)) {
        hasValid = true;
      } else {
        throw new IllegalArgumentException(String.format(
            "Invalid hex color string %s. Colors must be in the "
                + "form 'hhh' or 'hhhhhh'", hexColor));
      }
    }

    public Builder profileBackgroundColor(String profileBackgroundColor) {
      validateHexColor(profileBackgroundColor);
      return parameter("profile_background_color", profileBackgroundColor);
    }

    public Builder profileTextColor(String profileTextColor) {
      validateHexColor(profileTextColor);
      return parameter("profile_text_color", profileTextColor);
    }

    public Builder profileLinkColor(String profileLinkColor) {
      validateHexColor(profileLinkColor);
      return parameter("profile_link_color", profileLinkColor);
    }

    public Builder profileSidebarFillColor(String profileSidebarFillColor) {
      validateHexColor(profileSidebarFillColor);
      return parameter("profile_sidebar_fill_color", profileSidebarFillColor);
    }

    public Builder profileSidebarBorderColor(String profileSidebarBorderColor) {
      validateHexColor(profileSidebarBorderColor);
      return parameter("profile_sidebar_border_color",
          profileSidebarBorderColor);
    }

    public UpdateProfileColorsRequest build() {
      if (!hasValid) {
        throw new IllegalStateException(
            "One or more of profileBackgroundColor, profileTextColor, "
                + "profileLinkColor, profileSidebarFillColor, "
                + "profileSidebarBorderColor must be set.");
      }
      return new UpdateProfileColorsRequest(this);
    }

    /**
     * Returns true if the string matches the pattern hhh or hhhhhh.
     * 
     * @param hexColor A string of 3 or 6 hex characters.
     * @return true if the string matches the pattern hhh or hhhhhh.
     */
    boolean isValidHexColor(String hexColor) {
      return VALID_HEXCOLOR_PATTERN.matcher(hexColor).matches();
    }

  }

  public User post() {
    return JsonUtil.newUser(postJson());
  }
}
