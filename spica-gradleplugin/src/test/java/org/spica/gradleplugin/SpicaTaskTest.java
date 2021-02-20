package org.spica.gradleplugin;

import java.util.ArrayList;
import java.util.List;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.spica.javaclient.StandaloneActionContext;
import org.spica.javaclient.model.TaskInfo;

public class SpicaTaskTest {

  @Test
  public void trigger () {

    List<String> ids = new ArrayList<String>();
    Project project = ProjectBuilder.builder().build();
    project.getPlugins().apply(SpicaPlugin.class);
    SpicaTask spicaTask = project.getTasks().create("demo", SpicaTask.class);
    spicaTask.doLast(new Action<Task>() {
      @Override public void execute(Task task) {
        SpicaTask theTask = (SpicaTask) task;
        StandaloneActionContext standaloneActionContext = theTask.getContext();
        for (TaskInfo next: standaloneActionContext.getModel().getTaskInfos()) {
          ids.add(next.getId() + "-" + next.getName());

        }


      }
    });

    for (Action nextAction: spicaTask.getActions()) {
      nextAction.execute(spicaTask);
    }

    Assert.assertTrue ("No ids could be recieved from model", ids.size() > 0);
  }
}
