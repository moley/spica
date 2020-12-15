package org.spica.javaclient.actions.workingsets.template;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.SpicaProperties;
import org.spica.commons.credentials.PasswordMask;
import org.spica.commons.template.StringResolver;
import org.spica.commons.vcs.VcsBranchInfo;
import org.spica.commons.vcs.VcsModuleInfo;
import org.spica.commons.vcs.VcsProjectInfo;
import org.spica.commons.vcs.VersionControlException;
import org.spica.commons.vcs.git.StashAdapter;
import org.spica.javaclient.model.WorkingSetInfo;
import org.spica.javaclient.model.WorkingSetSourcePartInfo;

@Slf4j
public class InitializeFromStash implements InitializeFrom {

  private SpicaProperties spicaProperties = new SpicaProperties();

  private PasswordMask passwordMask = new PasswordMask();

  @Override public List<WorkingSetSourcePartInfo> initialize(String fromUrl, String branch) {
    log.info("initialize from " + fromUrl);

    String user = spicaProperties.getValue("spica.stash.user");
    String password = spicaProperties.getValue("spica.stash.password");

    List<WorkingSetSourcePartInfo> sourcePartInfos = new ArrayList<WorkingSetSourcePartInfo>();

    String [] tokens = fromUrl.split("/");
    String project = tokens[tokens.length - 1];
    String baseUrl = fromUrl.replace("projects/" + project, "");
    log.info("Using base url " + baseUrl + " with project " + project + " with user " + user + " and password " + passwordMask.getMaskedPassword(password));
    StashAdapter stashAdapter = new StashAdapter();
    try {
      stashAdapter.login(baseUrl, user, password);
      for (VcsProjectInfo vcsProjectInfo: stashAdapter.getProjects()) {
        if (fromUrl.toUpperCase().endsWith(vcsProjectInfo.getKey().toUpperCase())) {
          for (VcsModuleInfo vcsModuleInfo : vcsProjectInfo.getModules()) {

            StringResolver stringResolver = new StringResolver();
            stringResolver.replace("project", vcsProjectInfo.getKey());
            stringResolver.replace("projectLower", vcsProjectInfo.getKey().toLowerCase());
            stringResolver.replace("repo", vcsModuleInfo.getKey());
            stringResolver.replace("repoLower", vcsModuleInfo.getKey().toLowerCase());

            String resolvedBranch = stringResolver.resolve(branch);

            for (VcsBranchInfo nextBranchInfo: stashAdapter.getBranches(vcsModuleInfo)) {
              System.out.print(".");
              if (nextBranchInfo.getName().equals(resolvedBranch)) {
                WorkingSetSourcePartInfo projectSourcePartInfo = new WorkingSetSourcePartInfo();
                projectSourcePartInfo.setId(vcsModuleInfo.getKey());
                projectSourcePartInfo.setUrl(vcsModuleInfo.getUrl());
                projectSourcePartInfo.setBranch(resolvedBranch);
                sourcePartInfos.add(projectSourcePartInfo);
              }

            }



          }



        }
      }
      System.out.println (".");
    } catch (VersionControlException e) {
      log.error(e.getLocalizedMessage(), e);
    }

    return sourcePartInfos;

  }
}
