package org.spica.commons.services.bitbucket;

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

  /**
   * connect to the bitbucket server with the credentials from spica properties
   * Needs properties {@link BitbucketService#PROPERTY_BITBUCKET_URL}, {@link BitbucketService#PROPERTY_BITBUCKET_USER} and
   * {@link BitbucketService#PROPERTY_BITBUCKET_PASSWORD} to be set
   *
   * @throws VersionControlException exception when communicating with bitbucket server
   */
  public void connectToServer() throws VersionControlException {
    SpicaProperties spicaProperties = new SpicaProperties();
    stashAdapter.login(spicaProperties.getValue(PROPERTY_BITBUCKET_URL),
        spicaProperties.getValue(PROPERTY_BITBUCKET_USER),
        spicaProperties.getValue(PROPERTY_BITBUCKET_PASSWORD));
  }

  /**
   * get a list of all projects from the connected server
   * @return projects
   * @throws VersionControlException on error
   */
  public List<VcsProjectInfo> getProjects () throws VersionControlException {
    return stashAdapter.getProjects();
  }

  /**
   * get a list of branches of module
   *
   * @param vcsModuleInfo   module to get branches from
   *
   * @return list of branches
   */
  public List<VcsBranchInfo> getBranches(VcsModuleInfo vcsModuleInfo) {
    return stashAdapter.getBranches(vcsModuleInfo);
  }

  /**
   * get last change infos of branch
   * @param vcsBranchInfo branch to recieve infos for
   * @return last change informations
   */
  public LastChangeInfo getLastChangeInfo (VcsBranchInfo vcsBranchInfo) {
    return stashAdapter.getLastChangeInfo(vcsBranchInfo);
  }

  /**
   * Checkout the given branch to targetPath
   *
   * @param branch      branch to be checked out
   * @param targetPath  path to check the branch out
   */
  public void checkout(VcsBranchInfo branch, File targetPath) {
    stashAdapter.checkout(branch, targetPath);
  }

  /**
   * Checkout the given branch from the given reository url
   * @param url         repository url
   * @param branch      branch to checkout
   * @param targetPath  into this path
   */
  public void checkout(String url, String branch, File targetPath) {
    stashAdapter.checkout(url, branch, targetPath);
  }




}
