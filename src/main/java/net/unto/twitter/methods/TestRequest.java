package net.unto.twitter.methods;

/**
 * Returns the string "ok" in the requested format with a 200 OK HTTP status
 * code.
 */
public final class TestRequest extends AbstractRequest {

  public static Builder builder() {
    return new Builder();
  }

  TestRequest(Builder builder) {
    super(builder);
  }

  public static final class Builder extends AbstractRequest.Builder<Builder> {

    Builder() {
      path("/help/test.json");
      authorizationRequired(false);
    }

    public TestRequest build() {
      return new TestRequest(this);
    }
  }

  public String get() {
    return getJson();
  }
}
