package org.spica.commons.filestore;

import java.io.File;
import org.spica.commons.SpicaProperties;

public class FilestoreService {

  private SpicaProperties spicaProperties = new SpicaProperties();

  private File filestorepath = new File (SpicaProperties.getSpicaHome(), "filestore");

  public FilestoreService( ){
    if (! filestorepath.exists())
      filestorepath.mkdirs();

  }

  public File file (final String filename) {
    return new File (filestorepath, filename);
  }
}
