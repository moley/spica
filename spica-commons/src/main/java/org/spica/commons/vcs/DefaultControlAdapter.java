package org.spica.commons.vcs;

public abstract class DefaultControlAdapter implements VersionControlAdapter {

  @Override
  public String toString () {
    return getClass().getName();
  }
}
