package org.spica.fxclient.controllers;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.model.TopicInfo;

public class TasksController extends AbstractController {
  public TextField txtSearch;
  public ListView<TopicInfo> lviTasks;

  private static final Logger LOGGER = LoggerFactory.getLogger(MessagesController.class);

  @Override public void refreshData() {

    lviTasks.setItems(FXCollections.observableArrayList(getActionContext().getModel().getTopicInfos()));
    lviTasks.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent event) {
        if (event.getClickCount() == 2) {
          getActionContext().getModel().setSelectedTopicInfo(lviTasks.getSelectionModel().getSelectedItem());
          stepToPane(Pages.TASKDETAIL);
        }

      }
    });
    lviTasks.setCellFactory(lv -> {
      ListCell<TopicInfo> cell = new ListCell<TopicInfo>(){
        @Override
        public void updateItem(TopicInfo item , boolean empty) {
          super.updateItem(item, empty);
          if (empty) {
            setText(null);
          }
          else {
            setText(item.getName());
          }
        }
      };

      cell.setOnDragExited(new EventHandler<DragEvent>() {
        @Override public void handle(DragEvent event) {
          LOGGER.info("Drag in Message ListView exited");
          LOGGER.info("Url: " + event.getDragboard().getUrl());
          LOGGER.info("Files: " + event.getDragboard().getFiles());
          LOGGER.info("DragView: " + event.getDragboard().getDragView());
          LOGGER.info("String: " + event.getDragboard().getString());
          LOGGER.info("Accepting object: " + event.getAcceptingObject());
          LOGGER.info("Gesture Source" + event.getGestureSource());
          LOGGER.info("Gesture Target" + event.getGestureTarget());
          LOGGER.info(event.getSceneX() + "-" + event.getSceneY());
          LOGGER.info(event.getSource().toString());

        }
      });



      return cell ;
    });

  }
}
