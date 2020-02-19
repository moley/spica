package org.spica.fx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.cli.actions.StandaloneActionContext;
import org.spica.commons.SpicaProperties;
import org.spica.commons.mail.MailReciever;
import org.spica.fx.controllers.AbstractFxController;
import org.spica.javaclient.Configuration;
import org.spica.javaclient.mail.MailImporter;
import org.spica.javaclient.services.ModelCacheService;

public class SpicaFxClient extends Application {

  private final static Logger LOGGER = LoggerFactory.getLogger(SpicaFxClient.class);

  private ScreenManager screenManager = new ScreenManager();

  private List<View> views = new ArrayList<View>();

  private StackPane detailNode = new StackPane();

  private HashMap<Button, Node> nodesPerMenu = new HashMap<Button, Node>();

  public static void main(String[] args) {
    launch(SpicaFxClient.class, args);
  }

  @Override public void start(Stage primaryStage) throws IOException {

    LOGGER.info("Starting spica fx client");

    views.add(new DashboardView());
    views.add(new UsersView());
    views.add(new MeView());

    StandaloneActionContext actionContext = new StandaloneActionContext();
    String serverUrl = actionContext.getProperties()
        .getValueOrDefault("spica.cli.serverurl", "http://localhost:8765/api");

    Configuration.getDefaultApiClient().setBasePath(serverUrl);

    actionContext.refreshServer();

    List<AbstractFxController> controllers = new ArrayList<AbstractFxController>();

    VBox menu = new VBox();
    menu.setAlignment(Pos.TOP_CENTER);
    menu.setFillWidth(true);
    for (View next : views) {
      try {
        AbstractFxController abstractFxController = next.getController();
        controllers.add(abstractFxController);
        abstractFxController.setActionContext(actionContext);
        Button button = new Button(next.getDisplayname(), Consts.createIcon(next.getIcon(), Consts.ICONSIZE_MENU));
        button.setMaxWidth(Double.MAX_VALUE);
        UiUtils.setStyleClass(button, "menubutton");

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
          @Override public void handle(MouseEvent event) {
            System.out.println(event.getSource());

            Node currentNode = nodesPerMenu.get(event.getSource());

            currentNode.toFront();
          }
        });

        Parent pane = next.getParent();
        UiUtils.setStyleClass(pane, "rootpane");

        detailNode.getChildren().add(pane);

        nodesPerMenu.put(button, pane);

        menu.getChildren().add(button);
      } catch (Exception e) {
        LOGGER.error("Error loading view " + next.getDisplayname() + ":" + e.getLocalizedMessage(), e);
      }
    }

    ReloadModelFileThread resetClockThread = new ReloadModelFileThread(actionContext.getModel().getCurrentFile(),
        actionContext, controllers);
    resetClockThread.start();

    SpicaProperties spicaProperties = actionContext.getProperties();
    if (spicaProperties.getValue(MailReciever.PROPERTY_MAIL_POP_HOST) != null &&
        spicaProperties.getValue(MailReciever.PROPERTY_MAIL_POP_PORT) != null) {

      RecieveMailsFileThread recieveMailsFileThread = new RecieveMailsFileThread(actionContext);
      recieveMailsFileThread.start();
    }
    else
      LOGGER.warn("Recieving mails is not started because no host or port was configured");

    ResetTimeThread resetTimeThread = new ResetTimeThread(actionContext, controllers);
    resetTimeThread.start();

    Button btnExit = new Button("Exit", Consts.createIcon("fa-sign-out", Consts.ICONSIZE_MENU));
    UiUtils.setStyleClass(btnExit, "menubutton");

    btnExit.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent event) {
        System.exit(0);
      }
    });
    btnExit.setMaxWidth(Double.MAX_VALUE);
    menu.getChildren().add(btnExit);
    UiUtils.setStyleClass(menu, "menu");

    detailNode.getChildren().get(0).toFront();

    HBox main = new HBox();
    main.getChildren().add(menu);
    main.getChildren().add(detailNode);
    HBox.setHgrow(detailNode, Priority.ALWAYS);

    Scene scene = new Scene(main);

    boolean isMultiScreenEnvironment = false;

    primaryStage.setScene(scene);
    primaryStage.setFullScreen(true);
    primaryStage.initStyle(StageStyle.UNDECORATED);
    scene.getStylesheets().add(getClass().getResource("/spica.css").toExternalForm());
    primaryStage.setAlwaysOnTop(isMultiScreenEnvironment);
    primaryStage.setFullScreen(isMultiScreenEnvironment);
    if (ModelCacheService.isDefault())
      screenManager.layoutOnPrimary(primaryStage);
    else
      screenManager.layoutOnExternalOrPrimary(primaryStage);

    primaryStage.show();
  }
}
