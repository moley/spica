package org.spica.fx;

import javafx.scene.Parent;
import org.spica.fx.controllers.AbstractFxController;

public interface View {

  String getId ();

  String getDisplayname();

  Parent getParent();

  String getIcon ();

  AbstractFxController getController ();

}
