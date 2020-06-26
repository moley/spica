package org.spica.fx.controllers;

import com.jfoenix.controls.JFXBadge;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.CustomTextField;
import org.spica.cli.actions.StandaloneActionContext;
import org.spica.fx.ApplicationContext;
import org.spica.fx.Consts;
import org.spica.fx.clipboard.ClipboardItem;
import org.spica.javaclient.Configuration;
import org.spica.javaclient.actions.Api;
import org.spica.javaclient.auth.HttpBasicAuth;

@Slf4j
public class MainController extends AbstractController  {
  @FXML private JFXBadge badClipboard;
  @FXML private Button btnClipboard;
  @FXML private Button btnCloseSearch;
  @FXML private CustomTextField txtFieldSearch;
  @FXML private Button btnSearchUp;
  @FXML private Button btnSearchDown;
  @FXML private Label lblMatches;
  @FXML private CustomTextField txtSearch;
  @FXML private BorderPane paRootPane;
  @FXML private ButtonBar btnMainActions;

  private ListView<ClipboardItem> lviClipboardItems = new ListView<ClipboardItem>();

  @FXML
  public void initialize () {
    setPaRootPane(paRootPane);
    btnCloseSearch.setGraphic(Consts.createIcon("fa-close", 15));

    Label lbl = new Label();
    lbl.setGraphic(Consts.createIcon("fa-search", 15));
    txtFieldSearch.setLeft(lbl);


    PopOver popOver = new PopOver(lviClipboardItems);
    btnClipboard.setOnMouseEntered(mouseEvent -> {
      //Show PopOver when mouse enters label
      popOver.show(btnClipboard);
    });

    btnClipboard.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent event) {
        if (event.getClickCount() == 2) {
          getApplicationContext().getClipboard().clear();

        }
      }
    });

    btnClipboard.setOnMouseExited(mouseEvent -> {
      //Hide PopOver when mouse exits label
      popOver.hide();
    });


    btnSearchUp.setGraphic(Consts.createIcon("fa-chevron-up", 15));
    btnSearchDown.setGraphic(Consts.createIcon("fa-chevron-down", 15));

    btnClipboard.setGraphic(Consts.createIcon("fa-clipboard", 15));

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

    StandaloneActionContext standaloneActionContext = new StandaloneActionContext();
    setActionContext(standaloneActionContext);

    setApplicationContext(new ApplicationContext());
    getApplicationContext().getClipboard().getItems().addListener(new ListChangeListener<ClipboardItem>() {
      @Override public void onChanged(Change<? extends ClipboardItem> c) {
        badClipboard.setText(String.valueOf(getApplicationContext().getClipboard().getItems().size()));
        badClipboard.setEnabled(! getApplicationContext().getClipboard().getItems().isEmpty());
        badClipboard.refreshBadge();
        lviClipboardItems.setItems(getApplicationContext().getClipboard().getItems());

      }
    });


    try {
      String serverUrl = standaloneActionContext.getProperties().getValueOrDefault("spica.cli.serverurl", "http://localhost:8765/api");
      Configuration.getDefaultApiClient().setBasePath(serverUrl);

      /**String username = standaloneActionContext.getProperties().getValueNotNull("spica.cli.username");
      String password = standaloneActionContext.getProperties().getValueNotNull("spica.cli.password");
      HttpBasicAuth httpBasicAuth = (HttpBasicAuth) Configuration.getDefaultApiClient().getAuthentication("basicAuth");
      httpBasicAuth.setUsername(username);
      httpBasicAuth.setPassword(password);**/

      standaloneActionContext.refreshServer();
    } catch (Exception e) {
      String message = "Error while synchronization with server: " + e.getLocalizedMessage();
      log.error(message, e);
    }


    if (paRootPane.getCenter() == null)
      stepToPane(Pages.PLANNING);
  }
}
