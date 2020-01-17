package org.spica.commons.vcs;

public class VcsBranchInfo {

  private String name;

  private VcsModuleInfo vcsModuleInfo;

  private boolean defaultBranch;

  private String latestChangeset;


  public VcsBranchInfo(final VcsModuleInfo vcsModuleInfo) {
    this.vcsModuleInfo = vcsModuleInfo;
  }

  public VcsModuleInfo getVcsModuleInfo() {
    return vcsModuleInfo;
  }

  public boolean isDefaultBranch() {
    return defaultBranch;
  }

  public void setDefaultBranch(boolean defaultBranch) {
    this.defaultBranch = defaultBranch;
  }

  public String getLatestChangeset() {
    return latestChangeset;
  }

  public void setLatestChangeset(String latestChangeset) {
    this.latestChangeset = latestChangeset;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String toString () {
    return getName();
  }

}
