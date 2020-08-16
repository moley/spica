package org.spica.fx.view;

import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.spica.javaclient.model.TaskInfo;

@Slf4j
public class TodayTasksView extends AbstractTaskView {


  @Override public String getName() {
    return "Planned today";
  }

  public void renderTasks (final List<TaskInfo> taskInfos) {
    log.info("renderTasks " + taskInfos.size());
    getTaskInfos().clear();
    for (TaskInfo next: taskInfos) {
      //TODO If no project or no plan date available
      if (next.getTaskState() == null || ! next.getTaskState().equals(TaskInfo.TaskStateEnum.FINISHED)) {
        if (next.getPlannedDate() != null && next.getPlannedDate().equals(LocalDate.now()))
          getTaskInfos().add(next);
      }
    }
  }
}
