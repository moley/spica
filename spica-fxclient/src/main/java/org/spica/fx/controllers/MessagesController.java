package org.spica.fx.controllers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.fx.renderer.MessageContainerInfoCellFactory;
import org.spica.javaclient.model.MessagecontainerInfo;

public class MessagesController extends AbstractController {

  private static final Logger LOGGER = LoggerFactory.getLogger(MessagesController.class);


  @FXML private ListView<MessagecontainerInfo> lviMessages;
  @FXML private TextArea txaNewMessage;

  private final ObjectProperty<ListCell<String>> dragSource = new SimpleObjectProperty<>();

  @FXML public void initialize() {
    lviMessages.setCellFactory(cellfactory -> new MessageContainerInfoCellFactory());
    lviMessages.setOnDragOver(new EventHandler<DragEvent>() {
      @Override public void handle(DragEvent event) {
        event.acceptTransferModes(TransferMode.ANY);
      }
    });

    lviMessages.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent event) {
        if (event.getClickCount() == 2) {
          getModel().setSelectedMessageContainer(lviMessages.getSelectionModel().getSelectedItem());
          stepToPane(Pages.MESSAGEDIALOG);
        }

      }
    });
  }

  @Override public void refreshData() {
    lviMessages.setItems(FXCollections.observableArrayList(getModel().getMessagecontainerInfos()));


  }
}
