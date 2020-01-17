package org.spica.javaclient.actions.projects.template;

import org.spica.javaclient.model.ProjectInfo;

public interface InitializeFrom {

  void initialize (final ProjectInfo projectInfo, String fromUrl, final String branch);
}
