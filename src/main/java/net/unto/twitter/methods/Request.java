package net.unto.twitter.methods;

import net.unto.twitter.HttpManager;
import net.unto.twitter.UtilProtos.Url;

public interface Request {
  
  public static interface Builder {
    Builder httpManager(HttpManager httpManager);
    Builder host(String host);
    Builder port(int port);
    Builder scheme(Url.Scheme scheme);
    Request build();
  }
  
  Url toUrl();
}
