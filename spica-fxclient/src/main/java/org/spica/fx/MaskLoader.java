package org.spica.fx;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MaskLoader<T> {

  private static final Logger LOGGER = LoggerFactory.getLogger(MaskLoader.class);

  public Mask<T> load (final String name) {
    LOGGER.info("load mask " + name);
    Mask<T> mask = new Mask<T>();

    String locationAsString = "/screens/" + name + ".fxml";
    URL location = getClass().getResource(locationAsString);
    if (location == null)
      throw new IllegalStateException("Could not find location " + locationAsString);

    FXMLLoader loader = new FXMLLoader(location);
    try {
      Parent root = loader.load();
      T controller = loader.getController();

      Scene scene = new Scene(root);
      Stage stage = new Stage();
      stage.setScene(scene);
      mask.setStage(stage);
      mask.setScene(scene);
      mask.setController(controller);
      stage.setAlwaysOnTop(true);
      stage.setResizable(false);
      stage.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
        if (KeyCode.ESCAPE == event.getCode()) {
          UiUtils.close(stage);
        }
      });
      stage.initStyle(StageStyle.UNDECORATED);

      return mask;
    } catch (IOException e) {
      LOGGER.error(e.getLocalizedMessage(), e);
      throw new IllegalStateException(e);
    }
  }
}
