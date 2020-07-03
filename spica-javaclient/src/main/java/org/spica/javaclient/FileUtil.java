package org.spica.javaclient;

import java.io.File;

public class FileUtil {
  public String getRelativeName(File rootPath, File next) {
    return next.getAbsolutePath().substring(rootPath.getAbsolutePath().length() + 1);
  }
}
