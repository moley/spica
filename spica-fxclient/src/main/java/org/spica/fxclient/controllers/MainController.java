package org.spica.fxclient.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.textfield.CustomTextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.cli.actions.StandaloneActionContext;
import org.spica.fxclient.Consts;

public class MainController extends AbstractController  {
  @FXML private Button btnCloseSearch;
  @FXML private CustomTextField txtFieldSearch;
  @FXML private Button btnSearchUp;
  @FXML private Button btnSearchDown;
  @FXML private Label lblMatches;
  @FXML private CustomTextField txtSearch;
  @FXML private BorderPane paRootPane;
  @FXML private ButtonBar btnMainActions;


  private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

  @FXML
  public void initialize () {
    setActionContext(new StandaloneActionContext());
    setPaRootPane(paRootPane);
    btnCloseSearch.setGraphic(Consts.createIcon("fa-close", 15));

    Label lbl = new Label();
    lbl.setGraphic(Consts.createIcon("fa-search", 15));
    txtFieldSearch.setLeft(lbl);

    btnSearchUp.setGraphic(Consts.createIcon("fa-chevron-up", 15));
    btnSearchDown.setGraphic(Consts.createIcon("fa-chevron-down", 15));




    for (Pages next : Pages.values()) {
      registerPane(next);

      if (next.isMainAction()) {
        Button menuItem = new Button(next.getDisplayname(), Consts.createIcon(next.getIcon(), Consts.ICON_SIZE_TOOLBAR));
        menuItem.setOnAction(event -> stepToPane(next));
        btnMainActions.getButtons().add(menuItem);
      }
    }



  }

  @Override public void refreshData() {
    if (paRootPane.getCenter() == null)
      stepToPane(Pages.PLANNING);
  }
}
