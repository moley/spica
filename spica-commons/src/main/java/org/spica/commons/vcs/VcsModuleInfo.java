package org.spica.commons.vcs;

import org.spica.commons.IDCreator;

public class VcsModuleInfo {

  private String name;

  private String url;

  private String key;

  private final VcsProjectInfo vcsProjectInfo;

  public VcsModuleInfo(final VcsProjectInfo vcsProjectInfo) {
    this.vcsProjectInfo = vcsProjectInfo;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
    if (key == null) {
      IDCreator idCreator = new IDCreator();
      key = idCreator.createID(name);
    }
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public VcsProjectInfo getVcsProjectInfo() {
    return vcsProjectInfo;
  }

  public String toString () {
    return url;
  }

}
