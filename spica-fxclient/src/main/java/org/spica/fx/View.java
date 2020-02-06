package org.spica.fx;

import javafx.scene.Parent;
import org.spica.fx.controllers.AbstractController;

public interface View {

  String getId ();

  String getDisplayname();

  Parent getParent();

  String getIcon ();

  AbstractController getController ();

}
