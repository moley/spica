package org.spica.fx.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import lombok.extern.slf4j.Slf4j;
import org.spica.fx.renderer.ProjectInfoTreeCellFactory;
import org.spica.javaclient.model.ProjectInfo;

@Slf4j
public class ProjectsController extends AbstractController {
  public TreeView<ProjectInfo> treProjects;
  public TextField txtSearch;

  @FXML
  public void initialize () {
    treProjects.setShowRoot(false);



  }

  private void addNewProject (final String newProjectIdentifier) {

    ProjectInfo parent = null;

    if (! treProjects.getSelectionModel().isEmpty() && treProjects.getSelectionModel().getSelectedItem().getValue().getName() != null)
      parent = treProjects.getSelectionModel().getSelectedItem().getValue();

    ProjectInfo projectInfo = new ProjectInfo();
    projectInfo.setId(UUID.randomUUID().toString());
    projectInfo.setName(newProjectIdentifier);
    projectInfo.setParentId(parent != null ? parent.getId(): null);

    getModel().getProjectInfos().add(projectInfo);

    txtSearch.clear();
    txtSearch.requestFocus();

    saveModel("Added project " + newProjectIdentifier);
    refreshViews();
  }

  private void removeProject (final ProjectInfo projectInfo) {
    getModel().getProjectInfos().remove(projectInfo);
    saveModel("Removed project " + projectInfo.getId() + "-" + projectInfo.getName());
    refreshViews();
  }

  public void refreshViews () {
    HashMap<String, TreeItem<ProjectInfo>> model = new HashMap<>();
    log.info("refreshViews with " + getModel().getTaskInfos().size() + " tasks");

    TreeItem<ProjectInfo> rootItem = new TreeItem<>();
    rootItem.setExpanded(true);
    List<ProjectInfo> projectInfoList = getModel().getProjectInfos();
    for (ProjectInfo next: projectInfoList) {
      TreeItem<ProjectInfo> treeItem = new TreeItem<>(next);
      treeItem.setExpanded(true);
      model.put(next.getId(), treeItem);
    }

    for (ProjectInfo next: projectInfoList) {
      TreeItem<ProjectInfo> treeItem = model.get(next.getId());
      if (next.getParentId() != null) {
        TreeItem<ProjectInfo> parentTreeItem = model.get(next.getParentId());
        parentTreeItem.getChildren().add(treeItem);
      }
      else {
        rootItem.getChildren().add(treeItem);
      }
    }

    treProjects.setRoot(rootItem);
  }

  @Override public void refreshData() {
    getMainController().refreshData();

    treProjects.setCellFactory(cellfactory -> new ProjectInfoTreeCellFactory(getActionContext(), this::refreshViews));
    refreshViews();

    txtSearch.requestFocus();
    txtSearch.setOnKeyPressed(event -> {
      if (event.getCode().equals(KeyCode.ENTER)) {
        addNewProject(txtSearch.getText());
      }
    });

    treProjects.setOnKeyPressed(event -> {
      if (! treProjects.getSelectionModel().isEmpty()) {
        ProjectInfo selectedProject = treProjects.getSelectionModel().getSelectedItem().getValue();

        if (event.getCode().equals(KeyCode.SLASH) && selectedProject.getName() != null) {
          removeProject(selectedProject);
        }
      }

    });

    treProjects.setOnMouseClicked(event -> {
      if (! treProjects.getSelectionModel().isEmpty()) {
        if (event.getClickCount() == 2) {
          ProjectInfo selectedProject = treProjects.getSelectionModel().getSelectedItem().getValue();
          getApplicationContext().setSelectedProjectInfo(selectedProject);
          stepToPane(Pages.PROJECTDETAIL);
        }
      }

    });


  }
}
