package org.spica.fx.logic;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.spica.commons.filestore.FilestoreItem;

@Slf4j
public class FileStoreNavigator {

  public void open (FilestoreItem filestoreItem) {
    try {
      Runtime.getRuntime().exec("open " + filestoreItem.getAbsolutePath());
    } catch (IOException e) {
      log.error(e.getLocalizedMessage(), e);
    }
  }
}
