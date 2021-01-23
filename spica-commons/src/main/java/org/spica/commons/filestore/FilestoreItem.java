package org.spica.commons.filestore;

import java.io.File;

public class FilestoreItem {

  private File file;

  public FilestoreItem (final File file) {
    this.file = file;
  }

  public String getName () {
    return this.file.getName();
  }

  public String getAbsolutePath () {
    return this.file.getAbsolutePath();
  }
}
