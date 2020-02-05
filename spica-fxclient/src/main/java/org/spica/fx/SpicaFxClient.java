package org.spica.fx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SpicaFxClient extends Application {

  private ScreenManager screenManager = new ScreenManager();

  private List<View> views = new ArrayList<View>();

  private StackPane detailNode = new StackPane();

  private HashMap<Button, Node> nodesPerMenu = new HashMap<Button, Node>();





  public static void main(String[] args) {
    System.setProperty("apple.laf.useScreenMenuBar", "true");
    System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Name");
    System.setProperty("com.apple.mrj.application.apple.menu.about.name", "MyApplication");


    launch(SpicaFxClient.class, args);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {


    views.add(new DashboardView());
    views.add(new UserView());

    VBox menu = new VBox();
    menu.setFillWidth(true);
    for (View next: views) {
      Button button = new Button(next.getName(), Consts.createIcon(next.getIcon(), Consts.ICONSIZE_MENU));
      UiUtils.setCssClass(button, "menubutton");

      button.setOnMouseClicked(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent event) {
          System.out.println (event.getSource());

          Node currentNode = nodesPerMenu.get(event.getSource());

          currentNode.toFront();
        }
      });

      Parent pane = next.getParent();
      pane.getStyleClass().add("rootpane");

      detailNode.getChildren().add(pane);

      nodesPerMenu.put(button, pane);

      menu.getChildren().add(button);
    }

    Button btnExit = new Button("Finish day", Consts.createIcon("fa-sign-out", Consts.ICONSIZE_MENU));
    UiUtils.setCssClass(btnExit, "menubutton");
    btnExit.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent event) {
        System.exit(0);
      }
    });
    menu.getChildren().add(btnExit);


    detailNode.getChildren().get(0).toFront();


    menu.setStyle("-fx-background-color: #777777;-fx-spacing: 30;-fx-padding: 5");



    HBox main = new HBox();
    main.getChildren().add(menu);
    main.getChildren().add(detailNode);
    HBox.setHgrow(detailNode, Priority.ALWAYS);

    Scene scene = new Scene(main);



    primaryStage.setScene(scene);
    primaryStage.initStyle(StageStyle.UNDECORATED);
    primaryStage.setAlwaysOnTop(screenManager.isMultiScreenEnvironment());
    screenManager.layoutOnPrimary(primaryStage);

    primaryStage.show();
  }
}
