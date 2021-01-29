package org.spica.fx.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.spica.fx.renderer.ProjectInfoCellFactory;
import org.spica.fx.renderer.ProjectInfoStringConverter;
import org.spica.javaclient.model.ProjectInfo;

public class ProjectDetailController extends AbstractController{

  public TextField txtName;
  public ComboBox<ProjectInfo> cboParent;
  public Button btnSave;


  @FXML
  public void initialize () {
    cboParent.setCellFactory(cellfactory -> new ProjectInfoCellFactory());
    cboParent.setConverter(new ProjectInfoStringConverter());
  }

  @Override public void refreshData() {
    getMainController().refreshData();

    ProjectInfo selectedProjectInfo = getApplicationContext().getSelectedProjectInfo();

    txtName.setText(selectedProjectInfo.getName());

    cboParent.setItems(FXCollections.observableArrayList(getModel().getOtherProjectInfos(selectedProjectInfo)));
    if (selectedProjectInfo.getParentId() != null) {
      ProjectInfo parent = getModel().findProjectInfoById(selectedProjectInfo.getParentId());
      cboParent.getSelectionModel().select(parent);
    }
    else
      cboParent.getSelectionModel().clearSelection();

    cboParent.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> selectedProjectInfo.setParentId(newValue.getId()));

    btnSave.setOnAction(event -> save());

  }

  public void save () {
    ProjectInfo projectInfo = getApplicationContext().getSelectedProjectInfo();
    if (! cboParent.getSelectionModel().isEmpty())
      projectInfo.setParentId(cboParent.getSelectionModel().getSelectedItem().getId());
    projectInfo.setName(txtName.getText());

    saveModel("Save project " + projectInfo.getId());


  }
}
