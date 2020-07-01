package org.spica.fx.renderer;

import org.spica.fx.view.TaskView;
import org.spica.javaclient.model.TaskInfo;

public class TaskTreeItem {



  private TaskView taskView;

  private TaskInfo taskInfo;

  public TaskTreeItem () {

  }

  public boolean isTask () {
    return taskInfo != null;
  }

  public TaskTreeItem (final TaskView taskView) {
    this.taskView = taskView;
  }

  public TaskTreeItem (final TaskInfo taskInfo) {
    this.taskInfo = taskInfo;
  }

  public String toString () {
    if (taskView != null)
      return taskView.getName();
    else if (taskInfo != null)
      return taskInfo.getName();
    else
      return "Todos";
  }

  public TaskView getTaskView() {
    return taskView;
  }

  public TaskInfo getTaskInfo() {
    return taskInfo;
  }
}
