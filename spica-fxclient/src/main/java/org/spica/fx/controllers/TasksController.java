package org.spica.fx.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import lombok.extern.slf4j.Slf4j;
import org.spica.fx.Reload;
import org.spica.fx.view.FinishedTasksView;
import org.spica.fx.view.InboxTasksView;
import org.spica.fx.view.TaskView;
import org.spica.fx.renderer.TaskInfoCellFactory;
import org.spica.fx.renderer.TaskViewCellFactory;
import org.spica.javaclient.model.TaskInfo;

@Slf4j public class TasksController extends AbstractController {
  public TextField txtSearch;
  public ListView<TaskView> lviLists;
  public ListView<TaskInfo> lviTasks;


  private InboxTasksView inboxTasksView = new InboxTasksView();
  private FinishedTasksView finishedTasksView = new FinishedTasksView();

  @FXML
  public void initialize () {
    lviLists.setCellFactory(cellFactory -> new TaskViewCellFactory());
  }

  private void addNewTask (final String newTaskIdentifier) {

    TaskInfo taskInfo = new TaskInfo();
    taskInfo.setName(newTaskIdentifier);

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

    lviLists.setItems(FXCollections.observableArrayList(inboxTasksView, finishedTasksView));
    for (TaskView next: lviLists.getItems()) {
      next.renderTasks(getActionContext().getModel().getTaskInfos());
    }

    if (! lviLists.getSelectionModel().isEmpty())
      lviTasks.setItems(FXCollections.observableArrayList(lviLists.getSelectionModel().getSelectedItem().getTaskInfos()));
  }

  @Override public void refreshData() {
    log.info("refreshData");

    lviTasks.setCellFactory(cellfactory -> new TaskInfoCellFactory(getActionContext(), new Reload() {
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
    }

    lviTasks.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override public void handle(KeyEvent event) {
        if (event.getCode().equals(KeyCode.SLASH)) {
          removeTask(lviTasks.getSelectionModel().getSelectedItem());
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

    lviTasks.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent event) {
        if (event.getClickCount() == 2) {
          getActionContext().getModel().setSelectedTaskInfo(lviTasks.getSelectionModel().getSelectedItem());
          stepToPane(Pages.TASKDETAIL);
        }

      }
    });




    lviTasks.setOnDragOver(new EventHandler<DragEvent>() {
      @Override public void handle(DragEvent event) {
        event.acceptTransferModes(TransferMode.ANY);
      }
    });

  }

}
