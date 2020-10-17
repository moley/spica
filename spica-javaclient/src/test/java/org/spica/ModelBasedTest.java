package org.spica;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.spica.commons.SpicaProperties;
import org.spica.javaclient.StandaloneActionContext;
import org.spica.javaclient.actions.ActionContext;

@Slf4j
public abstract class ModelBasedTest {

  @Before
  public void beforeTest () throws IOException {
    SpicaProperties.close();
  }

  public ActionContext createNewActionContext () {
    try {
      SpicaProperties.setSpicaHome(Files.createTempDirectory(UUID.randomUUID().toString() + "-" + String.valueOf(System.currentTimeMillis())).toFile());
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
    ActionContext actionContext = new StandaloneActionContext();

    return actionContext;
  }

  @After
  public void afterTest () {
    log.info("afterTest called");
    SpicaProperties.close();
  }
}
