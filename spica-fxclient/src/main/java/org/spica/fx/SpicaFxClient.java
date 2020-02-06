package org.spica.fx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.spica.cli.actions.StandaloneActionContext;
import org.spica.fx.controllers.AbstractController;
import org.spica.javaclient.actions.ActionContext;

public class SpicaFxClient extends Application {

  private ScreenManager screenManager = new ScreenManager();

  private List<View> views = new ArrayList<View>();

  private StackPane detailNode = new StackPane();

  private HashMap<Button, Node> nodesPerMenu = new HashMap<Button, Node>();





  public static void main(String[] args) {
    launch(SpicaFxClient.class, args);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {


    views.add(new DashboardView());
    views.add(new UserView());

    ActionContext actionContext = new StandaloneActionContext();

    List<AbstractController> controllers = new ArrayList<AbstractController>();



    VBox menu = new VBox();
    menu.setAlignment(Pos.TOP_CENTER);
    menu.setFillWidth(true);
    for (View next: views) {
      AbstractController abstractController = next.getController();
      controllers.add(abstractController);
      abstractController.setActionContext(actionContext);
      Button button = new Button(next.getDisplayname(), Consts.createIcon(next.getIcon(), Consts.ICONSIZE_MENU));
      button.setMaxWidth(Double.MAX_VALUE);
      UiUtils.setStyleClass(button, "menubutton");

      button.setOnMouseClicked(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent event) {
          System.out.println (event.getSource());

          Node currentNode = nodesPerMenu.get(event.getSource());

          currentNode.toFront();
        }
      });

      Parent pane = next.getParent();
      UiUtils.setStyleClass(pane, "rootpane");

      detailNode.getChildren().add(pane);

      nodesPerMenu.put(button, pane);

      menu.getChildren().add(button);
    }

    ResetModelFileThread resetClockThread = new ResetModelFileThread(actionContext.getModel().getCurrentFile(), actionContext, controllers);
    resetClockThread.start();

    ResetTimeThread resetTimeThread = new ResetTimeThread(actionContext, controllers);
    resetTimeThread.start();

    Button btnExit = new Button("Finish day", Consts.createIcon("fa-sign-out", Consts.ICONSIZE_MENU));
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


    boolean isMultiScreenEnvironment = false; //screenManager.isMultiScreenEnvironment();

    primaryStage.setScene(scene);
    primaryStage.setFullScreen(true);
    primaryStage.initStyle(StageStyle.UNDECORATED);
    scene.getStylesheets().add(getClass().getResource("/spica.css").toExternalForm());
    primaryStage.setAlwaysOnTop(isMultiScreenEnvironment);
    primaryStage.setFullScreen(isMultiScreenEnvironment);
    screenManager.layoutOnPrimary(primaryStage);

    primaryStage.show();
  }
}
