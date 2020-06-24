package org.spica.fx.filter;

import java.util.List;
import org.spica.javaclient.model.TaskInfo;

public interface TaskView {

  public String getName ();

  public int getNumberOfTasks ();

  public List<TaskInfo> getTaskInfos();

  public void renderTasks (final List<TaskInfo> taskInfos);
}
