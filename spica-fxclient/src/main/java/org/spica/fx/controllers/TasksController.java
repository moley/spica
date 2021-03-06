package org.spica.fx.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;
import org.spica.fx.UiUtils;
import org.spica.fx.clipboard.ClipboardItem;
import org.spica.fx.renderer.ProjectInfoCellFactory;
import org.spica.fx.renderer.ProjectInfoStringConverter;
import org.spica.fx.renderer.TaskTreeItem;
import org.spica.fx.renderer.TaskTreeItemCellFactory;
import org.spica.fx.view.FinishedTasksView;
import org.spica.fx.view.InboxTasksView;
import org.spica.fx.view.NextWeekTasksView;
import org.spica.fx.view.TaskView;
import org.spica.fx.view.TodayTasksView;
import org.spica.fx.view.WeekTasksView;
import org.spica.javaclient.model.ProjectInfo;
import org.spica.javaclient.model.TaskInfo;

@Slf4j
public class TasksController extends AbstractController {

  @FXML private TextField txtSearch;
  @FXML private TreeView<TaskTreeItem> treTasks;
  @FXML private ComboBox<ProjectInfo> cboProjects;

  private final InboxTasksView inboxTasksView = new InboxTasksView();
  private final TodayTasksView todayTasksView = new TodayTasksView();
  private final WeekTasksView weekTasksView = new WeekTasksView();
  private final NextWeekTasksView nextWeekTasksView = new NextWeekTasksView();
  private final FinishedTasksView finishedTasksView = new FinishedTasksView();

  private final List<TaskView> allViews = new ArrayList<>();

  @FXML
  public void initialize () {
    cboProjects.setPlaceholder(new Text("No projects available"));
    cboProjects.setCellFactory(cellfactory -> new ProjectInfoCellFactory());
    cboProjects.setConverter(new ProjectInfoStringConverter());
    treTasks.setShowRoot(false);
    treTasks.setCellFactory(cellFactory -> new TaskTreeItemCellFactory(getActionContext(), this::refreshViews));
    allViews.addAll(Arrays.asList(inboxTasksView, todayTasksView, weekTasksView, nextWeekTasksView, finishedTasksView));

  }



  private void addNewTask (final String newTaskIdentifier) {

    TaskInfo taskInfo = new TaskInfo();
    taskInfo.setId(UUID.randomUUID().toString());
    taskInfo.setName(newTaskIdentifier);
    String description = "";
    for (ClipboardItem next: getApplicationContext().getClipboard().getItems()) {
      if (next.getFiles() != null) {
        for (File nextFile: next.getFiles())
          taskInfo.addLinksItem(nextFile.getAbsolutePath());
      }
      else
        description += "see " + next.toString() + " \n";
    }
    taskInfo.setDescription(description);
    getApplicationContext().getClipboard().clear();

    getModel().getTaskInfos().add(taskInfo);

    txtSearch.clear();
    txtSearch.requestFocus();

    saveModel("Added task " + newTaskIdentifier);
    refreshViews();
  }

  private void removeTask (final TaskInfo taskInfo) {
    getModel().getTaskInfos().remove(taskInfo);
    saveModel("Removed task " + taskInfo.getId() + "-" + taskInfo.getName());
    refreshViews();
  }

  public void refreshViews () {
    log.info("refreshViews with " + getModel().getTaskInfos().size() + " tasks");

    ProjectInfo selectedProject = cboProjects.getSelectionModel().getSelectedItem();

    List<TaskInfo> taskInfoList = getModel().getTaskInfos();
    for (TaskView nextView: allViews) {
      nextView.renderTasks(taskInfoList);
    }

    TreeItem<TaskTreeItem> rootItem = new TreeItem<>(new TaskTreeItem());
    rootItem.setExpanded(true);
    for (TaskView nextView: allViews) {
      TreeItem<TaskTreeItem> viewItem = new TreeItem<>(new TaskTreeItem(nextView));
      viewItem.setExpanded(true);
      rootItem.getChildren().add(viewItem);
      for (TaskInfo nextTaskInfo: nextView.getTaskInfos()) {

        List<ProjectInfo> associatedProjects = getModel().getProjectsOfTask (nextTaskInfo);
        boolean associatedProjectsMatch = false;
        for (ProjectInfo next: associatedProjects) {
          if (next.getId().equals(selectedProject.getId())) {
            associatedProjectsMatch = true;
            break;
          }
        }

        if (selectedProject == null || selectedProject.getName().equals("All") || (nextTaskInfo.getProjectId() != null && associatedProjectsMatch))
          viewItem.getChildren().add(new TreeItem<>(new TaskTreeItem(nextTaskInfo)));
      }
    }
    treTasks.setRoot(rootItem);

  }

  @Override public void refreshData() {
    log.info("refreshData");

    getMainController().refreshData();

    UiUtils.requestFocus(txtSearch);

    ObservableList<ProjectInfo> scopeList = FXCollections.observableArrayList(getActionContext().getModel().getProjectInfos());
    ProjectInfo allScope = new ProjectInfo();
    allScope.setName("All");
    scopeList.add(0, allScope);
    cboProjects.setItems(scopeList);
    cboProjects.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) -> refreshViews());
    cboProjects.getSelectionModel().selectFirst();

    refreshViews();

    treTasks.setOnKeyPressed(event -> {
      if (! treTasks.getSelectionModel().isEmpty()) {
        TaskTreeItem taskTreeItem = treTasks.getSelectionModel().getSelectedItem().getValue();

        if (event.getCode().equals(KeyCode.SLASH) && taskTreeItem.isTask()) {
          removeTask(taskTreeItem.getTaskInfo());
        }
      }

    });

    txtSearch.requestFocus();
    txtSearch.setOnKeyPressed(event -> {
      if (event.getCode().equals(KeyCode.ENTER)) {
        addNewTask(txtSearch.getText());
      }
    });

    treTasks.setOnMouseClicked(event -> {
      if (! treTasks.getSelectionModel().isEmpty()) {
        if (event.getClickCount() == 2) {
          TaskTreeItem taskTreeItem = treTasks.getSelectionModel().getSelectedItem().getValue();
          if (taskTreeItem.isTask()) {
            getApplicationContext().setSelectedTaskInfo(taskTreeItem.getTaskInfo());
            //TODO stepToPane(Pages.TASKDETAIL);
          }
        }
      }

    });

  }

}
