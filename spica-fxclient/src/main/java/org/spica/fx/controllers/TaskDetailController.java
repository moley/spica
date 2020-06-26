package org.spica.fx.controllers;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.spica.javaclient.model.TaskInfo;

public class TaskDetailController extends AbstractController {
  public TextArea txaDescription;
  public TextField txtSummary;
  public Text txtLinks;

  @Override public void refreshData() {
    TaskInfo topicInfo = getActionContext().getModel().getSelectedTaskInfo();

    txtSummary.setText(topicInfo.getName());
    txaDescription.setText(topicInfo.getDescription());
    if (topicInfo.getLinks() != null)
      txtLinks.setText(String.join("\n", topicInfo.getLinks()));
  }
}
