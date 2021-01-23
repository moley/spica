package org.spica.commons.filestore;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.spica.commons.SpicaProperties;

@Slf4j
public class FilestoreService {

  private File filestorepath = new File (SpicaProperties.getSpicaHome(), "filestore");

  public FilestoreService( ){
    if (! filestorepath.exists())
      filestorepath.mkdirs();

  }

  public File file (final String filename) {
    return new File (filestorepath, filename);
  }

  public FilestoreItem item (final String filename) {
    for (FilestoreItem next: getItems()) {
      if (next.getName().equals(filename) || next.getAbsolutePath().equals(filename))
        return next;
    }

    throw new IllegalStateException("Filestoreitem with filename " + filename + " not found");
  }

  public File addAutoImport (final File fromImport) {
    try {
      File target = file (fromImport.getName());
      Files.move(fromImport.toPath(), target.toPath());
      return target;
    } catch (IOException e) {
      log.error(e.getLocalizedMessage(), e);
      throw new IllegalStateException(e);
    }
  }

  public List<FilestoreItem> getItems () {
    File [] children = filestorepath.listFiles();
    if (children == null)
      return Arrays.asList();
    else {
      List<FilestoreItem> items = new ArrayList<>();
      for (File next: children)
        items.add(new FilestoreItem(next));
      return items;
    }


  }
}
