package org.spica.javaclient.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class DownloadService {

  public void download (final File localPath, final String name, final String url, final boolean executable) throws IOException {
    if (! localPath.exists())
      localPath.mkdirs();

    File toFile = new File (localPath, name);

    InputStream in = new URL(url).openStream();
    Files.copy(in, toFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

    if (executable)
      toFile.setExecutable(true);
  }
}
