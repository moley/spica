package org.spica.fx.clipboard;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class ClipboardItem {

  private URL url;

  private List<File> files;

  private String string;

  public String getString() {
    return string;
  }

  public void setString(String string) {
    this.string = string;
  }

  public List<File> getFiles() {
    return files;
  }

  public void setFile (final File file) {
    this.files = Arrays.asList(file);
  }

  public void setFiles(List<File> files) {
    this.files = files;
  }

  public URL getUrl() {
    return url;
  }

  public void setUrl(String url) {
    try {
      this.url = new URL(url);
    } catch (MalformedURLException e) {
      throw new IllegalStateException("Cannot set a url " + url, e);
    }
  }

  public String toString () {
    if (url != null)
      return url.toString();
    else if (files != null) {
      return files.toString();
    }
    else
      return string;

  }
}
