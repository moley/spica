package org.spica.fx;

import javafx.scene.Parent;
import org.spica.fx.controllers.AbstractController;

public abstract class AbstractView<T extends AbstractController> implements View  {

  private Mask<T> mask;


  public Mask<T> getMask () {
    if (mask != null)
      return mask;
    else {
      MaskLoader<T> maskLoader = new MaskLoader<>();
      mask = maskLoader.load(getId());
      return mask;
    }
  }

  public T getController () {
    return getMask().getController();
  }

  public Parent getParent () {
    Mask mask = getMask();
    return mask.getScene().getRoot();

  }


}
