package org.spica.fx.controllers;

import javafx.scene.control.Label;
import org.spica.javaclient.model.TaskInfo;

public class TaskDetailController extends AbstractController {
  public Label lblDetails;

  @Override public void refreshData() {
    TaskInfo topicInfo = getActionContext().getModel().getSelectedTaskInfo();
    lblDetails.setText("Details of task " + topicInfo.getName() + " - " + topicInfo.getDescription());

  }
}
