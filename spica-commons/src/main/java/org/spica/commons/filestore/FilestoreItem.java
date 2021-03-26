package org.spica.commons.filestore;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import org.apache.commons.io.FileUtils;

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

  public File getFile() {
    return file;
  }

  public boolean exists () {
    return file.exists();
  }

  public byte[] getByteArray () {
    if (! exists())
      return null;
    try {
      return FileUtils.readFileToByteArray(file);
    } catch (IOException e) {
      return null;
    }
  }

  public String getByteArrayBase64() {
    byte [] array = getByteArray();
    if (array == null)
      return null;

    return Base64.getEncoder().withoutPadding().encodeToString(array);
  }
}
