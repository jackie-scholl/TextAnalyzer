package net.unto.twitter.methods;

import java.util.Map;

import net.unto.twitter.JsonUtil;
import net.unto.twitter.TwitterProtos.Device;
import net.unto.twitter.TwitterProtos.User;

import com.google.common.collect.ImmutableMap;

/**
 * Sets which device Twitter delivers updates to for the authenticating user.
 */
public final class UpdateDeliveryDeviceRequest extends AbstractRequest {

  private final static Map<Device, String> DEVICE_NAMES = 
    new ImmutableMap.Builder<Device, String>()
      .put(Device.NONE, "none")
      .put(Device.SMS, "sms")
      .put(Device.IM, "im")
      .build();

  public static Builder builder(Device device) {
    return new Builder(device);
  }

  UpdateDeliveryDeviceRequest(Builder builder) {
    super(builder);
  }

  public static final class Builder extends AbstractRequest.Builder<Builder> {

    Builder(Device device) {
      path("/account/update_delivery_device.json");
      authorizationRequired(true);
      parameter("device", DEVICE_NAMES.get(device));
    }

    public UpdateDeliveryDeviceRequest build() {
      return new UpdateDeliveryDeviceRequest(this);
    }
  }

  /**
   * Sets which device Twitter delivers updates to for the authenticating user.
   */
  public User post() {
    return JsonUtil.newUser(postJson());
  }
}
