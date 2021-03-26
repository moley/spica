package org.spica.commons.filestore;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.spica.commons.SpicaProperties;

@Slf4j
public class FilestoreService {

  private File filestorepath = new File (SpicaProperties.getSpicaHome(), "filestore");

  public final static String CONTEXT_SCREENSHOT = "screenshot";
  public final static String CONTEXT_ICON = "icon";

  public FilestoreService( ){
    log.info("Initialize filestoreservice in " + filestorepath.getAbsolutePath());
    if (! filestorepath.exists())
      filestorepath.mkdirs();

  }

  public FilestoreItem file (final String filename) {
    return new FilestoreItem(new File (filestorepath, filename));
  }

  public void saveObjectItem (final Object idable, final String context, final File source) {
    FilestoreItem filestoreItem = objectItem(idable, context);
    try {
      log.info("Copy file " + source.getAbsolutePath() + " to " + filestoreItem.getAbsolutePath());
      FileUtils.copyFile(source, filestoreItem.getFile());
    } catch (IOException e) {
      throw new IllegalStateException("Could not save " + source.getAbsolutePath() + " to " + filestoreItem.getAbsolutePath() + ":" + e.getLocalizedMessage(), e);
    }
  }

  public FilestoreItem objectItem (final Object idable, final String context) {
    try {
      Method getIdMethod = idable.getClass().getMethod("getId");
      return new FilestoreItem(new File (filestorepath, idable.getClass().getSimpleName() + "_" + getIdMethod.invoke(idable) + "_" + context));
    } catch (NoSuchMethodException e) {
      throw new IllegalArgumentException("Object " + idable + " from type " + idable.getClass().getName() + " does not contain an getId() method");
    } catch (IllegalAccessException e) {
      throw new IllegalArgumentException("Object " + idable + " from type " + idable.getClass().getName() + " does not contain a callable getId() method");
    } catch (InvocationTargetException e) {
      throw new IllegalArgumentException("Calling method getId() of object " + idable + " from type " + idable.getClass().getName() + " fails: " + e.getLocalizedMessage(), e);
    }

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
      File target = file (fromImport.getName()).getFile();
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
