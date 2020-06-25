package org.spica.fx.view;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.spica.javaclient.model.TaskInfo;

@Slf4j
public class InboxTasksView extends AbstractTaskView {


  @Override public String getName() {
    return "Inbox";
  }

  public void renderTasks (final List<TaskInfo> taskInfos) {
    log.info("renderTasks " + taskInfos.size());
    getTaskInfos().clear();
    for (TaskInfo next: taskInfos) {
      //TODO If no project or no plan date available
      if (next.getState() == null || ! next.getState().equals(TaskInfo.StateEnum.FINISHED))
        getTaskInfos().add(next);
    }
  }
}
