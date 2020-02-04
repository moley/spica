package org.spica.fx;

public abstract class AbstractView<T> implements View  {

  public Mask<T> getMask (final String mask) {
    MaskLoader<T> maskLoader = new MaskLoader<>();
    return maskLoader.load(mask);
  }
}
