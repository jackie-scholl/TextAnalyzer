package net.unto.twitter.methods;

import java.io.File;

import net.unto.twitter.JsonUtil;
import net.unto.twitter.TwitterUtil;
import net.unto.twitter.TwitterProtos.User;
import net.unto.twitter.UtilProtos.Url.Part;

public final class UpdateProfileBackgroundImageRequest extends AbstractRequest {

  static final int MAX_IMAGE_SIZE = 800 * 1000; // 800KB
  
  public static Builder builder(File file) {
    return new Builder(file);
  }
  
  UpdateProfileBackgroundImageRequest(Builder builder)  {
    super(builder);
  }
  
  public static final class Builder extends AbstractRequest.Builder<Builder> {

    Builder(File file) {
      assert(file != null);
      Part part = TwitterUtil.readFileToPart("image", file);
      assert(part.getValue().size() < MAX_IMAGE_SIZE);
      part(part);
      path("/account/update_profile_background_image.json");
      authorizationRequired(true);
    }
    
    public Builder tile(boolean tile) {
      return parameter("tile", Boolean.toString(tile));
    }

    public UpdateProfileBackgroundImageRequest build() {
      return new UpdateProfileBackgroundImageRequest(this);
    }
  }

  public User post() {
    return JsonUtil.newUser(postJson());
  }
}
