package net.unto.twitter;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import net.unto.twitter.UtilProtos.Url.Part;

import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.common.collect.ImmutableMap;
import com.google.protobuf.ByteString;

public abstract class TwitterUtil {

  private final static Map<String, String> CONTENT_TYPES = 
    new ImmutableMap.Builder<String, String>()
      .put("abs", "audio/x-mpeg")
      .put("ai", "application/postscript")
      .put("aif", "audio/x-aiff")
      .put("aifc", "audio/x-aiff")
      .put("aiff", "audio/x-aiff")
      .put("aim", "application/x-aim")
      .put("art", "image/x-jg")
      .put("asf", "video/x-ms-asf")
      .put("asx", "video/x-ms-asf")
      .put("au", "audio/basic")
      .put("avi", "video/x-msvideo")
      .put("avx", "video/x-rad-screenplay")
      .put("bcpio", "application/x-bcpio")
      .put("bin", "application/octet-stream")
      .put("bmp", "image/bmp")
      .put("body", "text/html")
      .put("cdf", "application/x-cdf")
      .put("cer", "application/x-x509-ca-cert")
      .put("class", "application/java")
      .put("cpio", "application/x-cpio")
      .put("csh", "application/x-csh")
      .put("css", "text/css")
      .put("dib", "image/bmp")
      .put("doc", "application/msword")
      .put("dtd", "text/plain")
      .put("dv", "video/x-dv")
      .put("dvi", "application/x-dvi")
      .put("eps", "application/postscript")
      .put("etx", "text/x-setext")
      .put("exe", "application/octet-stream")
      .put("gif", "image/gif")
      .put("gtar", "application/x-gtar")
      .put("gz", "application/x-gzip")
      .put("hdf", "application/x-hdf")
      .put("hqx", "application/mac-binhex40")
      .put("htc", "text/x-component")
      .put("htm", "text/html")
      .put("html", "text/html")
      .put("ief", "image/ief")
      .put("jad", "text/vnd.sun.j2me.app-descriptor")
      .put("jar", "application/java-archive")
      .put("java", "text/plain")
      .put("jnlp", "application/x-java-jnlp-file")
      .put("jpe", "image/jpeg")
      .put("jpeg", "image/jpeg")
      .put("jpg", "image/jpeg")
      .put("js", "text/javascript")
      .put("jsf", "text/plain")
      .put("jspf", "text/plain")
      .put("kar", "audio/x-midi")
      .put("latex", "application/x-latex")
      .put("m3u", "audio/x-mpegurl")
      .put("mac", "image/x-macpaint")
      .put("man", "application/x-troff-man")
      .put("me", "application/x-troff-me")
      .put("mid", "audio/x-midi")
      .put("midi", "audio/x-midi")
      .put("mif", "application/x-mif")
      .put("mov", "video/quicktime")
      .put("movie", "video/x-sgi-movie")
      .put("mp1", "audio/x-mpeg")
      .put("mp2", "audio/x-mpeg")
      .put("mp3", "audio/x-mpeg")
      .put("mpa", "audio/x-mpeg")
      .put("mpe", "video/mpeg")
      .put("mpeg", "video/mpeg")
      .put("mpega", "audio/x-mpeg")
      .put("mpg", "video/mpeg")
      .put("mpv2", "video/mpeg2")
      .put("ms", "application/x-wais-source")
      .put("nc", "application/x-netcdf")
      .put("oda", "application/oda")
      .put("pbm", "image/x-portable-bitmap")
      .put("pct", "image/pict")
      .put("pdf", "application/pdf")
      .put("pgm", "image/x-portable-graymap")
      .put("pic", "image/pict")
      .put("pict", "image/pict")
      .put("pls", "audio/x-scpls")
      .put("png", "image/png")
      .put("pnm", "image/x-portable-anymap")
      .put("pnt", "image/x-macpaint")
      .put("ppm", "image/x-portable-pixmap")
      .put("ps", "application/postscript")
      .put("psd", "image/x-photoshop")
      .put("qt", "video/quicktime")
      .put("qti", "image/x-quicktime")
      .put("qtif", "image/x-quicktime")
      .put("ras", "image/x-cmu-raster")
      .put("rgb", "image/x-rgb")
      .put("rm", "application/vnd.rn-realmedia")
      .put("roff", "application/x-troff")
      .put("rtf", "application/rtf")
      .put("rtx", "text/richtext")
      .put("sh", "application/x-sh")
      .build();

  public final static DateTimeFormatter TWITTER_DATE_FORMATTER = DateTimeFormat
      .forPattern("EEE MMM dd HH:mm:ss Z yyyy")
      .withLocale(Locale.US);

  public final static DateTime parseTwitterDateTimeString(String twitterDateString) {
    try {
      return TWITTER_DATE_FORMATTER.parseDateTime(twitterDateString);
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException(String.format("Could not parse date string '%s'",
          twitterDateString));
    }
  }
  
  public final static String toString(DateTime dateTime) {
    return dateTime.toString(TWITTER_DATE_FORMATTER);
  }

  public final static String getExtension(String filename) {
    int index = filename.lastIndexOf('.');
    return ((index >= 0) || (index < filename.length())) ? 
        filename.substring(index + 1) : 
        null;
  }

  public final static String guessContentType(File file) {
    String extension = getExtension(file.getName());
    return ((extension != null) && CONTENT_TYPES.containsKey(extension)) ?
        CONTENT_TYPES.get(extension) :
        FilePart.DEFAULT_CONTENT_TYPE;
  }

  public final static String guessCharset(File file) {
    return FilePart.DEFAULT_CHARSET;
  }

  public final static byte[] readFileToByteArray(File file) {
    try {
      return FileUtils.readFileToByteArray(file);
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public final static Part readFileToPart(String name, File file) {
    assert(name != null);
    assert(file != null);
    byte[] value = readFileToByteArray(file);
    String contentType = guessContentType(file);
    String charset = guessCharset(file);
    return Part.newBuilder()
      .setName(name)
      .setFilename(file.getName())
      .setValue(ByteString.copyFrom(value))
      .setContentType(contentType)
      .setCharset(charset)
      .build();
  }
}
