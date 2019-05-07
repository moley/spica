package org.spica.devclient.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MaskLoader<T> {

  private static final Logger LOGGER = Logger.getLogger(MaskLoader.class.getName());

  public Mask<T> load (final String name) {
    Mask<T> mask = new Mask<T>();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/screens/" + name + ".fxml"));
    try {
      Parent root = loader.load();
      T controller = loader.getController();

      mask.setNode(root);
      mask.setController(controller);
      //stage.setAlwaysOnTop(true);
      //stage.setResizable(false);
      //stage.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
      //  if (KeyCode.ESCAPE == event.getCode()) {
      //    UiUtils.close(stage);
      //  }
      //});
      //stage.initStyle(StageStyle.UNDECORATED);

      return mask;
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
      throw new IllegalStateException(e);
    }
  }
}
