package org.spica.devclient.ui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.devclient.DemoData;
import org.spica.devclient.model.DashboardInfo;
import org.spica.javaclient.model.ProjectInfo;

import java.awt.*;
import java.awt.event.InputEvent;


public class MainPageController {

  private final static Logger LOGGER = LoggerFactory.getLogger(MainPageController.class);


  @FXML
  private TextField txtSearch;

  @FXML
  private ListView<ProjectInfo> lviProjects;

  @FXML
  private ListView<DashboardInfo> lviDashboard;


  @FXML
  private void initialize() {
    LOGGER.info("Initialize called (" + DemoData.projectInfos.size() + "-" + DemoData.dashboardInfos.size() + ")");
    lviProjects.setItems(FXCollections.observableArrayList(DemoData.projectInfos));
    lviDashboard.setItems(FXCollections.observableArrayList(DemoData.dashboardInfos));

  }


  public void toFront () {
    Bounds bounds = txtSearch.localToScreen(txtSearch.getLayoutBounds());
    System.out.println (bounds.getMinX() + "-" + bounds.getMinY());
    txtSearch.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, bounds.getMinX() + 10,
      bounds.getMinY() + 10, bounds.getMinX() + 10, bounds.getMinY(), MouseButton.PRIMARY, 1,
      true, true, true, true, true, true, true, true, true, true, null));

    try {
      Robot robot = new Robot();
      robot.mouseMove((int)bounds.getMinX() + 10,(int) bounds.getMinY() + 10);
      robot.mousePress(InputEvent.BUTTON1_MASK);
    } catch (AWTException e) {
      e.printStackTrace();
    }

  }

}
