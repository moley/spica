package org.spica.devclient.util;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;

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

  public static Node getFirstEditableNode (Parent root) {
    for (Node next: getAllNodes(root)) {
      if (next instanceof TextInputControl || next instanceof ComboBox) {
        System.out.println ("First ediable node is " + next);
        return next;
      }
    }

    throw new IllegalStateException("No exitable node found on root " + root);
  }

  public static ArrayList<Node> getAllNodes(Parent root) {
    ArrayList<Node> nodes = new ArrayList<Node>();
    addAllDescendents(root, nodes);
    return nodes;
  }

  private static void addAllDescendents(Parent parent, ArrayList<Node> nodes) {
    for (Node node : parent.getChildrenUnmodifiable()) {
      nodes.add(node);
      if (node instanceof Parent)
        addAllDescendents((Parent)node, nodes);
    }
  }
}
