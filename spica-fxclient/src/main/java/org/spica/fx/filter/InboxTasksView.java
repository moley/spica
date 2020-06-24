package org.spica.fx.filter;

import java.util.List;
import org.spica.javaclient.model.TaskInfo;

public class InboxTasksView extends AbstractTaskView {


  @Override public String getName() {
    return "Inbox";
  }

  public void renderTasks (final List<TaskInfo> taskInfos) {
    getTaskInfos().clear();
    for (TaskInfo next: taskInfos) {
      //TODO If no project or no plan date available
      getTaskInfos().add(next);
    }
  }
}
