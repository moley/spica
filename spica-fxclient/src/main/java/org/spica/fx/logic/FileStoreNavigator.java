package org.spica.fx.logic;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemUtils;
import org.spica.commons.filestore.FilestoreItem;

@Slf4j
public class FileStoreNavigator {

  public void open (FilestoreItem filestoreItem) {
    log.info("Open " + filestoreItem.getAbsolutePath());
    if (SystemUtils.IS_OS_MAC) {
      try {
        String command = "open " + filestoreItem.getAbsolutePath().replace(" ", "%20");
        log.info("Command: '" + command + "'");
        Process process = Runtime.getRuntime().exec(command);
        int returncode = process.waitFor();
        if (returncode != 0)
          log.error("Command '" + command + "' returned " + returncode);
      } catch (IOException e) {
        log.error(e.getLocalizedMessage(), e);
      } catch (InterruptedException e) {
        log.error(e.getLocalizedMessage(), e);
      }
    }
    else {
      log.error("FileStoreNavigator for other os not yet implemented");
    }
  }
}
