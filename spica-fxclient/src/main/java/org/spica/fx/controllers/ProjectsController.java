package org.spica.fx.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.extern.slf4j.Slf4j;
import org.spica.fx.Reload;
import org.spica.fx.renderer.ProjectTreeCellFactory;
import org.spica.fx.renderer.TaskTreeItem;
import org.spica.javaclient.model.ProjectInfo;

@Slf4j
public class ProjectsController extends AbstractController {
  public TreeView<ProjectInfo> treProjects;
  public TextField txtSearch;

  @FXML
  public void initialize () {
    treProjects.setShowRoot(true);



  }

  private void addNewProject (final String newProjectIdentifier) {

    ProjectInfo parent = null;

    if (! treProjects.getSelectionModel().isEmpty() && treProjects.getSelectionModel().getSelectedItem().getValue().getName() != null)
      parent = treProjects.getSelectionModel().getSelectedItem().getValue();

    ProjectInfo projectInfo = new ProjectInfo();
    projectInfo.setId(UUID.randomUUID().toString());
    projectInfo.setName(newProjectIdentifier);
    projectInfo.setParent(parent);

    getActionContext().getModel().getProjectInfos().add(projectInfo);

    txtSearch.clear();
    txtSearch.requestFocus();

    getActionContext().saveModel("Added project " + newProjectIdentifier);
    refreshViews();
  }

  private void removeProject (final ProjectInfo projectInfo) {
    getActionContext().getModel().getProjectInfos().remove(projectInfo);
    getActionContext().saveModel("Removed project " + projectInfo.getId() + "-" + projectInfo.getName());
    refreshViews();
  }

  public void refreshViews () {
    HashMap<ProjectInfo, TreeItem<ProjectInfo>> model = new HashMap<ProjectInfo, TreeItem<ProjectInfo>>();
    log.info("refreshViews with " + getActionContext().getModel().getTaskInfos().size() + " tasks");

    TreeItem<ProjectInfo> rootItem = new TreeItem<ProjectInfo> ();
    rootItem.setExpanded(true);
    List<ProjectInfo> projectInfoList = getActionContext().getModel().getProjectInfos();
    for (ProjectInfo next: projectInfoList) {
      TreeItem<ProjectInfo> treeItem = new TreeItem<ProjectInfo>(next);
      treeItem.setExpanded(true);
      model.put(next, treeItem);
    }

    for (ProjectInfo next: projectInfoList) {
      TreeItem<ProjectInfo> treeItem = model.get(next);
      if (next.getParent() != null) {
        TreeItem<ProjectInfo> parentTreeItem = model.get(next.getParent());
        parentTreeItem.getChildren().add(treeItem);
      }
      else {
        rootItem.getChildren().add(treeItem);
      }
    }

    treProjects.setRoot(rootItem);
  }

  @Override public void refreshData() {
    treProjects.setCellFactory(cellfactory -> new ProjectTreeCellFactory(getActionContext(), new Reload() {
      @Override public void reload() {
        refreshViews();
      }
    }));
    refreshViews();

    txtSearch.requestFocus();
    txtSearch.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override public void handle(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
          addNewProject(txtSearch.getText());
        }
      }
    });

    treProjects.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override public void handle(KeyEvent event) {
        if (! treProjects.getSelectionModel().isEmpty()) {
          ProjectInfo selectedProject = treProjects.getSelectionModel().getSelectedItem().getValue();

          if (event.getCode().equals(KeyCode.SLASH) && selectedProject.getName() != null) {
            removeProject(selectedProject);
          }
        }

      }
    });


  }
}
