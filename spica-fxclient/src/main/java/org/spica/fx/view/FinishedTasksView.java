package org.spica.fx.view;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.spica.javaclient.model.TaskInfo;

@Slf4j
public class FinishedTasksView extends AbstractTaskView {


  @Override public String getName() {
    return "Finished";
  }

  public void renderTasks (final List<TaskInfo> taskInfos) {
    log.info("renderTasks " + taskInfos.size());
    getTaskInfos().clear();
    for (TaskInfo next: taskInfos) {
      //TODO If no project or no plan date available
      if (next.getState() != null && next.getState().equals(TaskInfo.StateEnum.FINISHED))
        getTaskInfos().add(next);
    }
  }
}
