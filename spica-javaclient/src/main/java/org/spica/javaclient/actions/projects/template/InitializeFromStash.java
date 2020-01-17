package org.spica.javaclient.actions.projects.template;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.SpicaProperties;
import org.spica.commons.credentials.PasswordMask;
import org.spica.commons.template.StringResolver;
import org.spica.commons.vcs.VcsModuleInfo;
import org.spica.commons.vcs.VcsProjectInfo;
import org.spica.commons.vcs.VersionControlException;
import org.spica.commons.vcs.git.StashAdapter;
import org.spica.javaclient.model.ProjectInfo;
import org.spica.javaclient.model.ProjectSourcePartInfo;

public class InitializeFromStash implements InitializeFrom {

  private final static Logger LOGGER = LoggerFactory.getLogger(InitializeFromStash.class);

  private SpicaProperties spicaProperties = new SpicaProperties();

  private PasswordMask passwordMask = new PasswordMask();

  @Override public void initialize(ProjectInfo projectInfo, String fromUrl, String branch) {
    LOGGER.info("initialize from " + fromUrl);

    String user = spicaProperties.getValue("spica.stash.user");
    String password = spicaProperties.getValue("spica.stash.password");

    String [] tokens = fromUrl.split("/");
    String project = tokens[tokens.length - 1];
    String baseUrl = fromUrl.replace("projects/" + project, "");
    LOGGER.info("Using base url " + baseUrl + " with project " + project + " with user " + user + " and password " + passwordMask.getMaskedPassword(password));
    StashAdapter stashAdapter = new StashAdapter();
    try {
      stashAdapter.login(baseUrl, user, password);
      for (VcsProjectInfo vcsProjectInfo: stashAdapter.getProjects()) {
        System.out.println ("Check " + fromUrl + "<->" + vcsProjectInfo.getName());

        projectInfo.setSourceparts(new ArrayList<ProjectSourcePartInfo>());

        if (fromUrl.toUpperCase().endsWith(vcsProjectInfo.getName().toUpperCase())) {
          for (VcsModuleInfo vcsModuleInfo : vcsProjectInfo.getModules()) {

            ProjectSourcePartInfo projectSourcePartInfo = new ProjectSourcePartInfo();
            projectSourcePartInfo.setId(vcsModuleInfo.getKey());
            projectSourcePartInfo.setUrl(vcsModuleInfo.getUrl());
            projectInfo.getSourceparts().add(projectSourcePartInfo);

            StringResolver stringResolver = new StringResolver();
            stringResolver.replace("project", vcsProjectInfo.getKey());
            stringResolver.replace("repo", vcsModuleInfo.getKey());

            String resolvedBranch = stringResolver.resolve(branch);
            projectSourcePartInfo.setBranch(resolvedBranch);
            System.out.println ("Adding sourcepart " + projectSourcePartInfo.getId() + " - " + projectSourcePartInfo.getUrl() + " - " + projectSourcePartInfo.getBranch());

          }

          return;

        }
      }
    } catch (VersionControlException e) {
      LOGGER.error(e.getLocalizedMessage(), e);
    }

  }
}
