package org.spica.fxclient.controllers;

import javafx.scene.control.Label;
import org.spica.javaclient.model.TopicInfo;

public class TaskDetailController extends AbstractController {
  public Label lblDetails;

  @Override public void refreshData() {
    TopicInfo topicInfo = getActionContext().getModel().getSelectedTopicInfo();
    lblDetails.setText("Details of task " + topicInfo.getName() + " - " + topicInfo.getDescription());

  }
}
