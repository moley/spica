package org.spica.javaclient;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.junit.jupiter.api.Test;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.WorkingSetInfo;
import org.spica.javaclient.model.WorkingSetSourcePartInfo;
import org.spica.javaclient.services.ModelCacheService;

public class DemoDataCreatorTest {

  public final static String TESTWORKINGSET = "testWorkingSet";


  @Test
  public void createGit () throws Exception {
    File demodataGitDir = new File("build/demodata/gitserver");
    if (demodataGitDir.exists())
      FileUtils.deleteQuietly(demodataGitDir);

    File demodataGit1 = new File(demodataGitDir, "project1");

    File demodataGit2 = new File(demodataGitDir, "project2");

    File srcGit1 = new File(demodataGit1, "src/main/java");
    srcGit1.mkdirs();

    File srcGit2 = new File(demodataGit2, "src/main/java");
    srcGit2.mkdirs();

    File build1 = new File(demodataGit1, "build.gradle");
    build1.createNewFile();
    FileUtils.write(build1, "Hello, test", Charset.defaultCharset());

    File build2 = new File(demodataGit1, "build.gradle");
    build2.createNewFile();
    FileUtils.write(build2, "Hello, test", Charset.defaultCharset());

    Git git = Git.init().setDirectory(demodataGit1).call();
    git.add().addFilepattern(".").call();
    git.commit().setAll(true).setMessage("First commit").call();

    Git git2 = Git.init().setDirectory(demodataGit2).call();
    git2.add().addFilepattern(".").call();
    git2.commit().setAll(true).setMessage("First commit").call();

    ModelCacheService modelCacheService = new ModelCacheService();
    Model model = modelCacheService.load();
    List<WorkingSetInfo> workingSetInfosByQuery = model.findWorkingSetInfosByQuery(TESTWORKINGSET);
    model.getWorkingsetInfos().removeAll(workingSetInfosByQuery);

    WorkingSetInfo workingSetInfoTest = new WorkingSetInfo();
    workingSetInfoTest.setId(UUID.randomUUID().toString());
    workingSetInfoTest.setName(TESTWORKINGSET);
    WorkingSetSourcePartInfo sourcePartInfo1 = new WorkingSetSourcePartInfo();
    sourcePartInfo1.setUrl(demodataGit1.getAbsolutePath());
    sourcePartInfo1.setBranch("master");
    sourcePartInfo1.setEnabled(true);
    sourcePartInfo1.setId(demodataGit1.getName());

    WorkingSetSourcePartInfo sourcePartInfo2 = new WorkingSetSourcePartInfo();
    sourcePartInfo2.setUrl(demodataGit1.getAbsolutePath());
    sourcePartInfo2.setId(demodataGit2.getName());
    sourcePartInfo2.setBranch("master");
    sourcePartInfo2.setEnabled(true);

    workingSetInfoTest.addSourcepartsItem(sourcePartInfo1);
    workingSetInfoTest.addSourcepartsItem(sourcePartInfo2);
    model.getWorkingsetInfos().add(workingSetInfoTest);
    modelCacheService.save(model, "reconfigured testdata");
  }



}
