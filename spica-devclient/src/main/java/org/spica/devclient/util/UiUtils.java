package org.spica.devclient.util;

import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class UiUtils {

  public static void close (final Stage stage) {
    stage.fireEvent( new WindowEvent( stage, WindowEvent.WINDOW_CLOSE_REQUEST));
  }

  public static Bounds getBounds (Parent control) {
    if (control == null)
      throw new IllegalStateException("Control must not be null");
    return control.localToScreen(control.getLayoutBounds());
  }
}
