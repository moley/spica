package org.spica.fx.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.spica.fx.renderer.TaskInfoCellFactory;
import org.spica.fx.renderer.TaskInfoStringConverter;
import org.spica.javaclient.model.TaskInfo;

public class TaskDetailController extends AbstractController {
  public TextArea txaDescription;
  public TextField txtName;
  public Text txtLinks;
  public ComboBox<TaskInfo> cboParent;
  public Button btnSave;

  @FXML public void initialize() {
    cboParent.setCellFactory(cellfactory -> new TaskInfoCellFactory());
    cboParent.setConverter(new TaskInfoStringConverter());
  }

  @Override public void refreshData() {
    TaskInfo selectedTaskInfo = getApplicationContext().getSelectedTaskInfo();

    txtName.setText(selectedTaskInfo.getName());
    txaDescription.setText(selectedTaskInfo.getDescription());
    if (selectedTaskInfo.getLinks() != null)
      txtLinks.setText(String.join("\n", selectedTaskInfo.getLinks()));

    cboParent.setItems(FXCollections.observableArrayList(getModel().getOtherTaskInfos(selectedTaskInfo)));
    if (selectedTaskInfo.getParentId() != null) {
      TaskInfo parent = getModel().findTaskInfoById(selectedTaskInfo.getParentId());
      cboParent.getSelectionModel().select(parent);
    } else
      cboParent.getSelectionModel().clearSelection();

    cboParent.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      selectedTaskInfo.setParentId(newValue.getId());
      saveModel("Changed parent of task " + selectedTaskInfo.getId() + " to " + newValue.getId());
    });

    btnSave.setOnAction(action -> save());
  }

  public void save() {
    TaskInfo taskInfo = getApplicationContext().getSelectedTaskInfo();
    if (!cboParent.getSelectionModel().isEmpty())
      taskInfo.setParentId(cboParent.getSelectionModel().getSelectedItem().getId());
    taskInfo.setName(txtName.getText());

    saveModel("Save project " + taskInfo.getId());

  }
}
