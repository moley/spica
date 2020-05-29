package org.spica.javaclient.services;

import java.io.File;
import java.util.List;
import org.spica.commons.SpicaProperties;
import org.spica.commons.vcs.LastChangeInfo;
import org.spica.commons.vcs.VcsBranchInfo;
import org.spica.commons.vcs.VcsModuleInfo;
import org.spica.commons.vcs.VcsProjectInfo;
import org.spica.commons.vcs.VersionControlException;
import org.spica.commons.vcs.git.StashAdapter;

public class BitbucketService {

  public final static String PROPERTY_BITBUCKET_URL = "spica.bitbucket.url";
  public final static String PROPERTY_BITBUCKET_USER = "spica.bitbucket.user";
  public final static String PROPERTY_BITBUCKET_PASSWORD = "spica.bitbucket.password";

  private StashAdapter stashAdapter = new StashAdapter();

  public void connectToBitbucket () throws VersionControlException {
    SpicaProperties spicaProperties = new SpicaProperties();
    stashAdapter.login(spicaProperties.getValue(PROPERTY_BITBUCKET_URL),
        spicaProperties.getValue(PROPERTY_BITBUCKET_USER),
        spicaProperties.getValue(PROPERTY_BITBUCKET_PASSWORD));
  }

  public List<VcsProjectInfo> getProjects () throws VersionControlException {
    return stashAdapter.getProjects();
  }

  public List<VcsBranchInfo> getBranches(VcsModuleInfo vcsModuleInfo) {
    return stashAdapter.getBranches(vcsModuleInfo);
  }

  public LastChangeInfo getLastChangeInfo (VcsBranchInfo vcsBranchInfo) {
    return stashAdapter.getLastChangeInfo(vcsBranchInfo);
  }

  public void checkout(VcsBranchInfo branch, File targetPath) {
    stashAdapter.checkout(branch, targetPath);
  }




}
