package org.spica.fx;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UiUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(UiUtils.class);


  public static void applyCss (final Scene scene) {
    scene.getStylesheets().addAll(
       // UiUtils.class.getResource("/com/jfoenix/assets/css/jfoenix-design.css").toExternalForm(),
        // UiUtils.class.getResource("/com/jfoenix/assets/css/jfoenix-fonts.css").toExternalForm(),
        UiUtils.class.getResource("/css/spica.css").toExternalForm());
  }

  public static void close (final Window stage) {
    stage.fireEvent( new WindowEvent( stage, WindowEvent.WINDOW_CLOSE_REQUEST));
  }

  public static Bounds getBounds (Parent control) {
    if (control == null)
      throw new IllegalArgumentException("Argument 'control' must not be null");
    return control.localToScreen(control.getLayoutBounds());
  }


  public static void hideOnEsc (final Stage stage) {
    stage.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent escEvent) -> {
      if (KeyCode.ESCAPE == escEvent.getCode()) {
        escEvent.consume();
        stage.close();
      }
    });

  }

  public static void requestFocus (final Node node) {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        node.requestFocus();
      }
    });
  }

  public static void setStyleClass(final Node node, final String cssClass) {
    node.getStyleClass().clear();
    node.getStyleClass().add(cssClass);
  }

  public static void hideOnFocusLost (final Stage stage) {
    stage.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
      if (! isNowFocused) {
        stage.hide();
      }
    });
  }




}
