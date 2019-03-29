package org.spica.devclient;

import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.SpicaProperties;
import org.spica.devclient.ui.MainPageController;
import org.spica.devclient.util.Mask;
import org.spica.devclient.util.MaskLoader;
import org.spica.javaclient.ApiException;
import org.spica.javaclient.Configuration;
import org.spica.javaclient.api.TopicApi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class DevApplicationInternal extends Application {

  private final static Logger LOGGER = LoggerFactory.getLogger(DevApplicationInternal.class);


  public static void main(String[] args) {
    launch(args);
    System.exit(0);
  }

  @Override
  public void start(Stage primaryStage) throws AWTException {


    SpicaProperties spicaProperties = new SpicaProperties();
    String jiraUser = spicaProperties.getValue("spica.jira.user");
    String jiraPassword = spicaProperties.getValue("spica.jira.password");

    DemoData.create();
    Configuration.getDefaultApiClient().setBasePath("http://localhost:8765/api");

    TopicApi topicApi = new TopicApi();
    try {
      topicApi.importTopics(jiraUser, jiraUser, jiraPassword);
    } catch (ApiException e) {
      LOGGER.error("Error importing jira topics: " + e.getResponseBody(), e);
    }


    MaskLoader<MainPageController> mainMaskLoader = new MaskLoader<MainPageController>();
    Mask<MainPageController> mainMask = mainMaskLoader.load("mainpage");
    mainMask.setStage(primaryStage);
    primaryStage.setTitle("SPICA Devclient");
    primaryStage.setMaximized(true);
    primaryStage.setScene(mainMask.getScene());
    primaryStage.initStyle(StageStyle.UNDECORATED);
    primaryStage.show();
    primaryStage.requestFocus();

    Provider provider = Provider.getCurrentProvider(false);
    provider.register(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK), new HotKeyListener() {
      @Override
      public void onHotKey(HotKey hotKey) {
        System.out.println ("Recieved hotkey");

        Platform.runLater(new Runnable() {
          @Override
          public void run() {
            System.out.println ("Recieved hotkey");
            primaryStage.setMaximized(true);
            primaryStage.toFront();
            mainMask.getController().toFront();
          }
        });

      }
    });


  }
}