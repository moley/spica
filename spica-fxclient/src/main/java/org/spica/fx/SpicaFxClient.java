package org.spica.fx;

import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SpicaFxClient extends Application {

  private ScreenManager screenManager = new ScreenManager();

  public static void main(String[] args) {
    launch(SpicaFxClient.class, args);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {

    VBox vbox = new VBox();
    vbox.getChildren().add(new Label("Hello world"));

    Scene scene = new Scene(vbox);

    primaryStage.setScene(scene);
    primaryStage.initStyle(StageStyle.UNDECORATED);
    primaryStage.setAlwaysOnTop(screenManager.isMultiScreenEnvironment());
    screenManager.layoutOnPrimary(primaryStage);

    primaryStage.show();
  }
}
