package org.spica;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.spica.commons.SpicaProperties;

@Slf4j
public abstract class ModelBasedTest {

  @Before
  public void beforeTest () throws IOException {

    SpicaProperties.close();
    File tmpPath = Files.createTempDirectory(UUID.randomUUID().toString() + "-" + String.valueOf(System.currentTimeMillis())).toFile();
    log.info("beforeTest called and uses temp path " + tmpPath.getAbsolutePath());
    SpicaProperties.setSpicaHome(tmpPath);
  }

  @After
  public void afterTest () {
    log.info("afterTest called");
    SpicaProperties.close();
  }
}
