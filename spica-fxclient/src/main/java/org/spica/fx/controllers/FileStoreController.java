package org.spica.fx.controllers;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import lombok.extern.slf4j.Slf4j;
import org.spica.commons.filestore.FilestoreItem;
import org.spica.commons.filestore.FilestoreService;
import org.spica.fx.logic.FileStoreNavigator;
import org.spica.fx.renderer.FilestoreItemCellFactory;

@Slf4j
public class FileStoreController extends AbstractController {
  @FXML
  private ListView<FilestoreItem> lviFileStoreItems;

  private FilestoreService filestoreService;

  @FXML
  public void initialize () {
    lviFileStoreItems.setCellFactory(cell->new FilestoreItemCellFactory());

  }

  @Override public void refreshData() {
    getMainController().refreshData();
    filestoreService = getActionContext().getServices().getFilestoreService();
    lviFileStoreItems.setItems(FXCollections.observableArrayList(filestoreService.getItems()));
    lviFileStoreItems.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent event) {
        if (event.getClickCount() == 2) {
          FileStoreNavigator fileStoreNavigator = new FileStoreNavigator();
          fileStoreNavigator.open(lviFileStoreItems.getSelectionModel().getSelectedItem());
        }
      }
    });
  }
}
