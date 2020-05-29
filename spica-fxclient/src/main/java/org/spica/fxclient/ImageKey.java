package org.spica.fxclient;

import java.util.Objects;

public class ImageKey {
  private String name;

  public String getName() {
    return name;
  }

  public int getIconSize() {
    return iconSize;
  }

  private int iconSize;

  public ImageKey (final String name, final int iconSize) {
    this.name = name;
    this.iconSize = iconSize;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ImageKey imageKey = (ImageKey) o;
    return iconSize == imageKey.iconSize &&
        Objects.equals(name, imageKey.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, iconSize);
  }
}
