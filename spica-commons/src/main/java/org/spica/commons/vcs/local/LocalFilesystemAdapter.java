package org.spica.commons.vcs.local;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.vcs.VcsBranchInfo;
import org.spica.commons.vcs.DefaultControlAdapter;
import org.spica.commons.vcs.LastChangeInfo;
import org.spica.commons.vcs.VcsModuleInfo;
import org.spica.commons.vcs.VcsProjectInfo;
import org.spica.commons.vcs.VersionControlException;

/**
 * Uses a local path as version control
 * Two layouts are allowed:
 * <p>
 * project1
 * module1
 * branch xyz
 * branch foo
 * <p>
 * or
 * <p>
 * project1
 * module1
 * module2
 * <p>
 * The used layout is automatically detected via checking if under module1 there are non folder entries.
 */
public class LocalFilesystemAdapter extends DefaultControlAdapter {

  private Logger log = LoggerFactory.getLogger(LocalFilesystemAdapter.class);


  private File projectUrl;


  @Override
  public void login(String url, String user, String password) throws VersionControlException {
    if (url == null)
      throw new VersionControlException("url must be not null (credentials" + user + " - " + password + ")");

    projectUrl = new File(url).getAbsoluteFile();

    if (!projectUrl.exists())
      throw new VersionControlException("Local repository " + projectUrl.getAbsolutePath() + " does not exist");

  }

  @Override
  public List<VcsProjectInfo> getProjects() {

    VcsProjectInfo project = new VcsProjectInfo();
    project.setName(projectUrl.getName());
    List<VcsProjectInfo> vcsProjectInfoList = new ArrayList<VcsProjectInfo>();
    vcsProjectInfoList.add(project);

    log.info("Fetching next project: " + project.getName());

    if (projectUrl.listFiles() != null) {
      for (File next : projectUrl.listFiles()) {
        if (isValidFolder(next)) {
          VcsModuleInfo nextModule = new VcsModuleInfo(project);
          nextModule.setName(next.getName());
          nextModule.setKey(next.getName());
          nextModule.setUrl(next.getAbsolutePath());
          project.getModules().add(nextModule);
          log.info("Fetching next repo " + next.getName() + "-" + next.getAbsolutePath());

        }
      }
    }

    return vcsProjectInfoList;
  }

  @Override
  public String getName() {
    return "Local";
  }

  boolean isValidFolder(final File folder) {
    return folder.isDirectory() && !folder.getName().equals("build");
  }

  @Override
  public List<VcsBranchInfo> getBranches(VcsModuleInfo repository) {

    File repositoryUrl = new File(repository.getUrl());

    List<VcsBranchInfo> vcsBranchInfos = new ArrayList<VcsBranchInfo>();

    boolean isWithBranches = isWithBranches(repositoryUrl);

    if (isWithBranches) {
      for (File next : repositoryUrl.listFiles()) {
        if (isValidFolder(next)) {
          VcsBranchInfo vcsBranchInfo = new VcsBranchInfo(repository);
          vcsBranchInfo.setName(next.getName());
          vcsBranchInfos.add(vcsBranchInfo);
        }
      }
    } else {
      VcsBranchInfo vcsBranchInfo = new VcsBranchInfo(repository);
      vcsBranchInfo.setName("master");
      vcsBranchInfos.add(vcsBranchInfo);
    }

    return vcsBranchInfos;
  }

  @Override public LastChangeInfo getLastChangeInfo(VcsBranchInfo vcsBranchInfo) {
    return new LastChangeInfo();
  }

  @Override
  public void checkout(VcsBranchInfo branch, File targetPath) {
    VcsModuleInfo vcsModuleInfo = branch.getVcsModuleInfo();
    File branchDir = new File (vcsModuleInfo.getUrl());
    try {
      FileUtils.copyDirectory(branchDir, targetPath);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }

  }

  private boolean isWithBranches(File url) {
    for (File next : url.listFiles()) {
      if (next.isFile() && !next.getName().startsWith("."))
        return false;
    }

    return true;
  }




}
