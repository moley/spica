package org.spica.commons.vcs;

import java.util.ArrayList;
import java.util.List;
import org.spica.commons.IDCreator;

public class VcsProjectInfo {

  private String name;

  private String key;

  private final List<VcsModuleInfo> modules = new ArrayList<VcsModuleInfo>();

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

  public List<VcsModuleInfo> getModules() {
    return modules;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }


}
