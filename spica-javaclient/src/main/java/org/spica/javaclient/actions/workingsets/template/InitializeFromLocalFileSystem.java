package org.spica.javaclient.actions.workingsets.template;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.spica.javaclient.model.WorkingSetSourcePartInfo;

@Slf4j
public class InitializeFromLocalFileSystem implements InitializeFrom {
  @Override public List<WorkingSetSourcePartInfo> initialize(String fromUrl, String branch) {

    ArrayList<WorkingSetSourcePartInfo> workingSetSourcePartInfos = new ArrayList<WorkingSetSourcePartInfo>();

    for (File next: new File (fromUrl).getAbsoluteFile().listFiles()) {
      if (next.isDirectory()) {

        try {
          Git git = Git.open(next);
          Repository repository = git.getRepository();

          String branchFromRepo = repository.getBranch();
          String remoteUrlFromRepo = repository.getConfig().getString("remote", "origin", "url");

          WorkingSetSourcePartInfo workingSetSourcePartInfo = new WorkingSetSourcePartInfo();
          workingSetSourcePartInfo.setId(next.getName());
          workingSetSourcePartInfo.setEnabled(true);
          workingSetSourcePartInfo.setBranch(branchFromRepo);
          workingSetSourcePartInfo.setUrl(remoteUrlFromRepo);
          workingSetSourcePartInfos.add(workingSetSourcePartInfo);
        } catch (IOException e) {
          log.info("Folder " + next.getAbsolutePath() + " seems to be no git folder: " + e.getLocalizedMessage(), e);
        }
      }

    }
    return workingSetSourcePartInfos;
  }
}
