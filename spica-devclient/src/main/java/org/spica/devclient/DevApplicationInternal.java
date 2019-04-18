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
import org.spica.javaclient.ApiClient;
import org.spica.javaclient.api.UserApi;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.ModelCacheService;
import org.spica.devclient.ui.controller.MainPageController;
import org.spica.devclient.util.Mask;
import org.spica.devclient.util.MaskLoader;
import org.spica.javaclient.ApiException;
import org.spica.javaclient.Configuration;
import org.spica.javaclient.api.TopicApi;
import org.spica.javaclient.model.TopicContainerInfo;
import org.spica.javaclient.model.UserInfo;

import javax.swing.*;
import java.awt.AWTException;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;

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

    ModelCacheService modelCacheService = new ModelCacheService();
    ModelCache modelCache = new ModelCache();
    Configuration.getDefaultApiClient().setBasePath("http://localhost:8765/api");

    TopicApi topicApi = new TopicApi();
    try {
      TopicContainerInfo topicContainerInfo = topicApi.importTopics(jiraUser, jiraUser, jiraPassword);
      modelCache.setTopicInfos(topicContainerInfo.getTopics());
      modelCacheService.set(modelCache);
      LOGGER.info("Imported " + topicContainerInfo.getTopics().size() + " topics");
    } catch (ApiException e) {
      LOGGER.error("Error importing jira topics: " + e.getResponseBody(), e);
    }


    //to refresh the users from external systems call 'curl http://localhost:8765/api/user/refresh'
    UserApi userApi = new UserApi();
    try {
      ApiClient apiClient = userApi.getApiClient();
      apiClient.setReadTimeout(100000);
      apiClient.setConnectTimeout(100000);
      userApi.setApiClient(apiClient);
      List<UserInfo> userInfoList = userApi.getUsers();
      modelCache.setUserInfos(userInfoList);
      LOGGER.info("Imported " + userInfoList.size() + " users");
      modelCacheService.set(modelCache);

    } catch (ApiException e) {
      LOGGER.error("Error importing users: " + e.getResponseBody(), e);
    }


    MaskLoader<MainPageController> mainMaskLoader = new MaskLoader<MainPageController>();
    Mask<MainPageController> mainMask = mainMaskLoader.load("mainpage");
    mainMask.setStage(primaryStage);
    mainMask.setSize(1600, 1200);
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