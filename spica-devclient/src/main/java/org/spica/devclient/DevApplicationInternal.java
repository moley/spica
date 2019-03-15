package org.spica.devclient;

import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.spica.devclient.ui.MainPageController;
import org.spica.devclient.util.Mask;
import org.spica.devclient.util.MaskLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class DevApplicationInternal extends Application {

  public static void main(String[] args) {
    launch(args);
    System.exit(0);
  }

  @Override
  public void start(Stage primaryStage) throws AWTException {

    DemoData.create();


    MaskLoader<MainPageController> mainMaskLoader = new MaskLoader<MainPageController>();
    Mask<MainPageController> mainMask = mainMaskLoader.load("mainpage");
    mainMask.setStage(primaryStage);
    primaryStage.setTitle("SPICA Devclient");
    primaryStage.setScene(mainMask.getScene());
    primaryStage.initStyle(StageStyle.UNDECORATED);
    primaryStage.show();
    primaryStage.requestFocus();


    Provider provider = Provider.getCurrentProvider(false);
    provider.register(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.ALT_MASK | InputEvent.SHIFT_MASK), new HotKeyListener() {
      @Override
      public void onHotKey(HotKey hotKey) {
        System.out.println ("Recieved hotkey");

        Platform.runLater(new Runnable() {
          @Override
          public void run() {
            System.out.println ("Recieved hotkey");
            primaryStage.toFront();
            mainMask.getController().toFront();
          }
        });

      }
    });


  }
}