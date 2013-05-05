package net.unto.twitter.methods;

import java.io.File;

import net.unto.twitter.JsonUtil;
import net.unto.twitter.TwitterUtil;
import net.unto.twitter.TwitterProtos.User;
import net.unto.twitter.UtilProtos.Url.Part;

public final class UpdateProfileImageRequest extends AbstractRequest {

  static final int MAX_IMAGE_SIZE = 700 * 1000; // 700KB
  
  public static Builder builder(File file) {
    return new Builder(file);
  }
  
  UpdateProfileImageRequest(Builder builder)  {
    super(builder);
  }
  
  public static final class Builder extends AbstractRequest.Builder<Builder> {

    Builder(File file) {
      assert(file != null);
      Part part = TwitterUtil.readFileToPart("image", file);
      assert(part.getValue().size() < MAX_IMAGE_SIZE);
      part(part);
      path("/account/update_profile_image.json");
      authorizationRequired(true);
    }
    
    public UpdateProfileImageRequest build() {
      return new UpdateProfileImageRequest(this);
    }
  }

  public User post() {
    return JsonUtil.newUser(postJson());
  }
}
