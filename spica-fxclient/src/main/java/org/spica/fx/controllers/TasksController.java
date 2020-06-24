package org.spica.fx.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.spica.fx.Mask;
import org.spica.fx.MaskLoader;
import org.spica.fx.filter.InboxTasksView;
import org.spica.fx.filter.TaskView;
import org.spica.fx.renderer.TaskViewCellFactory;
import org.spica.javaclient.model.TaskInfo;

@Slf4j public class TasksController extends AbstractController {
  public TextField txtSearch;
  public ListView<TaskView> lviLists;
  public ListView<TaskInfo> lviTasks;


  private InboxTasksView inboxTasksView = new InboxTasksView();

  @FXML
  public void initialize () {
    lviLists.setCellFactory(cellFactory -> new TaskViewCellFactory());

  }

  private void addNewTask (final String newTaskIdentifier) {

    TaskInfo taskInfo = new TaskInfo();
    taskInfo.setName(newTaskIdentifier);

    getActionContext().getModel().getTaskInfos().add(taskInfo);
    inboxTasksView.renderTasks(getActionContext().getModel().getTaskInfos());

    txtSearch.clear();
    txtSearch.requestFocus();

    getActionContext().saveModel("Added task " + newTaskIdentifier);
    refreshViews();
  }

  private void removeTask (final TaskInfo taskInfo) {
    getActionContext().getModel().getTaskInfos().remove(taskInfo);
    inboxTasksView.renderTasks(getActionContext().getModel().getTaskInfos());

    getActionContext().saveModel("Removed task " + taskInfo.getId() + "-" + taskInfo.getName());
    refreshViews();
  }

  public void refreshViews () {

    lviLists.setItems(FXCollections.observableArrayList(inboxTasksView));
    for (TaskView next: lviLists.getItems()) {
      next.renderTasks(getActionContext().getModel().getTaskInfos());
    }
  }

  @Override public void refreshData() {

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
    lviTasks.setCellFactory(lv -> {
      ListCell<TaskInfo> cell = new ListCell<TaskInfo>() {
        @Override public void updateItem(TaskInfo item, boolean empty) {
          super.updateItem(item, empty);
          if (empty) {
            setText(null);
          } else {
            setText(item.getName());
          }
        }
      };

      cell.setOnDragDropped(new EventHandler<DragEvent>() {
        @Override public void handle(DragEvent event) {

          log.info("Drag in Tasks ListView exited");
          log.info("Url: " + event.getDragboard().getUrl());
          log.info("Files: " + event.getDragboard().getFiles());
          log.info("DragView: " + event.getDragboard().getDragView());
          log.info("String: " + event.getDragboard().getString());
          log.info("Accepting object: " + event.getAcceptingObject());
          log.info("Gesture Source" + event.getGestureSource());
          log.info("Gesture Target" + event.getGestureTarget());
          log.info(event.getSceneX() + "-" + event.getSceneY());
          log.info(event.getSource().toString());

        }
      });

      return cell;
    });

    lviTasks.setOnDragOver(new EventHandler<DragEvent>() {
      @Override public void handle(DragEvent event) {
        event.acceptTransferModes(TransferMode.ANY);
      }
    });

  }

}
