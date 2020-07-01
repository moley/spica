package org.spica.fx.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;
import org.spica.fx.Reload;
import org.spica.fx.UiUtils;
import org.spica.fx.clipboard.ClipboardItem;
import org.spica.fx.renderer.TaskTreeItem;
import org.spica.fx.renderer.TaskTreeItemCellFactory;
import org.spica.fx.view.FinishedTasksView;
import org.spica.fx.view.InboxTasksView;
import org.spica.fx.view.NextWeekTasksView;
import org.spica.fx.view.TaskView;
import org.spica.fx.view.TodayTasksView;
import org.spica.fx.view.WeekTasksView;
import org.spica.javaclient.model.TaskInfo;

@Slf4j public class TasksController extends AbstractController {
  public TextField txtSearch;
  public ListView<TaskView> lviProjects;
  public TreeView<TaskTreeItem> treTasks;

  private InboxTasksView inboxTasksView = new InboxTasksView();
  private TodayTasksView todayTasksView = new TodayTasksView();
  private WeekTasksView weekTasksView = new WeekTasksView();
  private NextWeekTasksView nextWeekTasksView = new NextWeekTasksView();
  private FinishedTasksView finishedTasksView = new FinishedTasksView();

  private List<TaskView> allViews = new ArrayList<TaskView>();

  @FXML
  public void initialize () {
    lviProjects.setPlaceholder(new Text("No projects available"));
    treTasks.setShowRoot(false);
    treTasks.setCellFactory(cellFactory -> new TaskTreeItemCellFactory(getActionContext(), new Reload() {
      @Override public void reload() {
        refreshViews();
      }
    }));
    allViews.addAll(Arrays.asList(inboxTasksView, todayTasksView, weekTasksView, nextWeekTasksView, finishedTasksView));

  }

  private void addNewTask (final String newTaskIdentifier) {

    TaskInfo taskInfo = new TaskInfo();
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

    getActionContext().getModel().getTaskInfos().add(taskInfo);

    txtSearch.clear();
    txtSearch.requestFocus();

    getActionContext().saveModel("Added task " + newTaskIdentifier);
    refreshViews();
  }

  private void removeTask (final TaskInfo taskInfo) {
    getActionContext().getModel().getTaskInfos().remove(taskInfo);
    getActionContext().saveModel("Removed task " + taskInfo.getId() + "-" + taskInfo.getName());
    refreshViews();
  }

  public void refreshViews () {
    log.info("refreshViews with " + getActionContext().getModel().getTaskInfos().size() + " tasks");

    List<TaskInfo> taskInfoList = getActionContext().getModel().getTaskInfos();
    for (TaskView nextView: allViews) {
      nextView.renderTasks(taskInfoList);
    }

    TreeItem<TaskTreeItem> rootItem = new TreeItem<TaskTreeItem> (new TaskTreeItem());
    rootItem.setExpanded(true);
    for (TaskView nextView: allViews) {
      TreeItem<TaskTreeItem> viewItem = new TreeItem<TaskTreeItem> (new TaskTreeItem(nextView));
      viewItem.setExpanded(true);
      rootItem.getChildren().add(viewItem);
      for (TaskInfo nextTaskInfo: nextView.getTaskInfos()) {
        viewItem.getChildren().add(new TreeItem<TaskTreeItem> (new TaskTreeItem(nextTaskInfo)));
      }
    }
    treTasks.setRoot(rootItem);

  }

  @Override public void refreshData() {
    log.info("refreshData");

    UiUtils.requestFocus(txtSearch);

    refreshViews();

    /**lviTasks.setCellFactory(cellfactory -> new TaskInfoCellFactory(getActionContext(), new Reload() {
      @Override public void reload() {
        refreshViews();
      }
    }));

    refreshViews();

    lviLists.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TaskView>() {
      @Override public void changed(ObservableValue<? extends TaskView> observable, TaskView oldValue,
          TaskView newValue) {
        if (newValue != null)
          lviTasks.setItems(FXCollections.observableArrayList(newValue.getTaskInfos()));
      }
    });

    if (lviLists.getSelectionModel().isEmpty()) {
      lviLists.getSelectionModel().selectFirst();
    }**/

    treTasks.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override public void handle(KeyEvent event) {
        if (! treTasks.getSelectionModel().isEmpty()) {
          TaskTreeItem taskTreeItem = treTasks.getSelectionModel().getSelectedItem().getValue();

          if (event.getCode().equals(KeyCode.SLASH) && taskTreeItem.isTask()) {
            removeTask(taskTreeItem.getTaskInfo());
          }
        }

      }
    });

    txtSearch.requestFocus();
    txtSearch.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override public void handle(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
          addNewTask(txtSearch.getText());
        }
      }
    });

    treTasks.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent event) {
        if (! treTasks.getSelectionModel().isEmpty()) {
          if (event.getClickCount() == 2) {
            TaskTreeItem taskTreeItem = treTasks.getSelectionModel().getSelectedItem().getValue();
            if (taskTreeItem.isTask()) {
              getActionContext().getModel().setSelectedTaskInfo(taskTreeItem.getTaskInfo());
              stepToPane(Pages.TASKDETAIL);
            }
          }
        }

      }
    });

  }

}
