package org.spica.devclient.util;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.geometry.Bounds;
import javafx.scene.Node;
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

  public static Node getIcon(final String resourcekey) {
    if (resourcekey != null) {

      if (resourcekey.startsWith("fa-")) {
        String iconname = resourcekey.substring(3).toUpperCase();
        return FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.valueOf(iconname));
      }
      else
        throw new IllegalStateException("Currently only fontawesome icons are supported");
    }
    else
      return null;
  }
}
