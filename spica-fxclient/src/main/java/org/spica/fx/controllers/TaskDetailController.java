package org.spica.fx.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    TaskInfo taskInfo = getModel().getSelectedTaskInfo();

    txtName.setText(taskInfo.getName());
    txaDescription.setText(taskInfo.getDescription());
    if (taskInfo.getLinks() != null)
      txtLinks.setText(String.join("\n", taskInfo.getLinks()));

    cboParent.setItems(FXCollections.observableArrayList(getModel().getOtherTaskInfos()));
    if (taskInfo.getParentId() != null) {
      TaskInfo parent = getModel().findTaskInfoById(taskInfo.getParentId());
      cboParent.getSelectionModel().select(parent);
    } else
      cboParent.getSelectionModel().clearSelection();

    cboParent.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TaskInfo>() {
      @Override public void changed(ObservableValue<? extends TaskInfo> observable, TaskInfo oldValue,
          TaskInfo newValue) {
        taskInfo.setParentId(newValue.getId());
        saveModel("Changed parent of task " + taskInfo.getId() + " to " + newValue.getId());
      }
    });

    btnSave.setOnAction(action -> save());
  }

  public void save() {
    TaskInfo taskInfo = getModel().getSelectedTaskInfo();
    if (!cboParent.getSelectionModel().isEmpty())
      taskInfo.setParentId(cboParent.getSelectionModel().getSelectedItem().getId());
    taskInfo.setName(txtName.getText());

    saveModel("Save project " + taskInfo.getId());

  }
}
