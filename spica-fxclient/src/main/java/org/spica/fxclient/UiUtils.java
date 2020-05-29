package org.spica.fxclient;

import java.util.ArrayList;
import java.util.List;
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

  public static String createMultilineString (final String completeString, final int length) {
    String remainingString = completeString;
    List<String> lines = new ArrayList<String>();
    do {
      if (! remainingString.isEmpty()) {
        lines.add(remainingString.substring(0, length));
        remainingString = remainingString.substring(length);
      }
    } while (remainingString.length() > length);

    if (! remainingString.isEmpty())
      lines.add(remainingString);
    return String.join("\n",lines);
  }

  public static void applyCss (final Scene scene) {
    scene.getStylesheets().add(UiUtils.class.getResource("/adonai.css").toExternalForm());
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
