package org.spica.commons.vcs;

import java.io.File;
import java.util.List;


/**
 * Definition of a adapter to a version control system (like CVS, Subversion, Git, Mercurial...)
 */
public interface VersionControlAdapter {

  /**
   * Login to the version control system
   *
   * @param url       url to access version control system
   * @param user      user to access version control system
   * @param password  password to access version control system
   *
   * @throws VersionControlException if an exception occurred while accessing version control system
   */
  void login (final String url, final String user, final String password) throws VersionControlException;

  /**
   * get list of projects which are contained in the logged in version control system
   *
   * @return list of projects
   *
   * @throws VersionControlException if an exception occurred while reading projects of version control system
   */
  List<VcsProjectInfo> getProjects () throws VersionControlException;


  /**
   * gets the name of the version control system
   * @return name
   */
  String getName ();

  /**
   * gets the branches that are found in the parameterized module
   *
   * @param vcsModuleInfo  module
   * @return list of branches
   */
  List<VcsBranchInfo> getBranches (final VcsModuleInfo vcsModuleInfo);

  /**
   * gets last change info on the given branch
   * @param vcsBranchInfo  branchInfo
   * @return  last change info
   */
  LastChangeInfo getLastChangeInfo (VcsBranchInfo vcsBranchInfo);

  /**
   * checks out the branch into the target path
   *
   * @param branch      branch to check out
   * @param targetPath  path to checkout the branch
   */
  void checkout (VcsBranchInfo branch, File targetPath);









}
