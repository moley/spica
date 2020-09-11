package org.spica.fx;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScreenManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(ScreenManager.class);


  private Screen primary;

  private List<Screen> externalScreens = new ArrayList<>();

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

  public boolean isMultiScreenEnvironment () {
    return externalScreens.size() > 0;
  }

  public Screen getExternalOrPrimaryScreen () {
    return ! externalScreens.isEmpty() ? externalScreens.get(0) : primary;
  }

  public Screen getPrimary () {
    return primary;
  }

  public void layoutOnExternalOrPrimary(final Stage stage) {
    Screen externalOrPrimary = getExternalOrPrimaryScreen();
    stage.setX(externalOrPrimary.getVisualBounds().getMinX());
    stage.setY(externalOrPrimary.getVisualBounds().getMinY());
    stage.setWidth(externalOrPrimary.getVisualBounds().getWidth());
    stage.setHeight(externalOrPrimary.getVisualBounds().getHeight());
  }

  public void layoutOnPrimary(final Stage stage) {
    Screen primary = getPrimary();
    stage.setX(primary.getVisualBounds().getMinX());
    stage.setY(primary.getVisualBounds().getMinY());
    stage.setWidth(primary.getVisualBounds().getWidth());
    stage.setHeight(primary.getVisualBounds().getHeight());
  }

  public void layoutEdged (final Stage stage) {
    Screen primary = getExternalOrPrimaryScreen();
    stage.setX(primary.getBounds().getMaxX() - 1405);
    stage.setY(primary.getBounds().getMinY() + 40);
    stage.setWidth(1400);
    stage.setHeight(primary.getBounds().getMaxY()- 80);
  }





}
