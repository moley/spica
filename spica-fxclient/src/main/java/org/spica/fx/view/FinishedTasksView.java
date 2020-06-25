package org.spica.fx.view;

import java.util.List;
import org.spica.javaclient.model.TaskInfo;

public class FinishedTasksView extends AbstractTaskView {


  @Override public String getName() {
    return "Finished";
  }

  public void renderTasks (final List<TaskInfo> taskInfos) {
    getTaskInfos().clear();
    for (TaskInfo next: taskInfos) {
      //TODO If no project or no plan date available
      if (next.getState() != null && next.getState().equals(TaskInfo.StateEnum.FINISHED))
        getTaskInfos().add(next);
    }
  }
}
