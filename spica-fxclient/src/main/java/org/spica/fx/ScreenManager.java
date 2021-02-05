package org.spica.fx;

import java.util.ArrayList;
import java.util.List;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScreenManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(ScreenManager.class);


  private Screen primary;

  private List<Screen> externalScreens = new ArrayList<>();

  private final static double HALF_WIDTH = 110;

  public ScreenManager() {
    primary = Screen.getPrimary();

    for (Screen next: Screen.getScreens()) {
      if (! primary.equals(next))
        externalScreens.add(next);
    }

    if (externalScreens.size() > 1)
      throw new IllegalStateException("Multi screen setups with more than one external monitors are currently not supported");

    LOGGER.info("Primary screen:  " + primary.toString() + "(" + primary.getBounds() + ")");
    for (Screen nextExternalScreen: externalScreens) {
      LOGGER.info("External screen:  " + nextExternalScreen.toString() + "(" + nextExternalScreen.getBounds() + ")");
    }

  }

  public double getFullWidth () {
    return getExternalOrPrimaryScreen().getBounds().getWidth();
  }

  public double getHalfWidth () {
    return HALF_WIDTH;
  }

  public Screen getExternalOrPrimaryScreen () {
    return ! externalScreens.isEmpty() ? externalScreens.get(0) : primary;
  }

  public void layoutEdged (final Stage stage, final boolean resize) {
    Screen primary = getExternalOrPrimaryScreen();
    stage.setX(primary.getBounds().getMinX());
    stage.setY(primary.getBounds().getMinY());
    if (resize) {
      stage.setWidth(getFullWidth());
      stage.setHeight(primary.getBounds().getMaxY());
    }
  }





}
