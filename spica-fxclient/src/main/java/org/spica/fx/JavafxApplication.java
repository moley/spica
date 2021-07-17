package org.spica.fx;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.recovery.ResilientFileOutputStream;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.awt.image.MultiResolutionImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Timer;
import java.util.UUID;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.filestore.FilestoreService;
import org.spica.fx.clipboard.ClipboardItem;
import org.spica.fx.controllers.MainController;
import org.spica.fx.controllers.Pages;

@Slf4j public class JavafxApplication extends Application {

  private static final Logger LOGGER = LoggerFactory.getLogger(JavafxApplication.class);

  // application stage is stored so that it can be shown and hidden based on system tray icon operations.
  private Stage stage;

  private ScreenManager screenManager = new ScreenManager();

  // a timer allowing the tray icon to provide a periodic notification event.
  private Timer notificationTimer = new Timer();

  private MaskLoader<MainController> maskLoader = new MaskLoader<MainController>();
  private Mask<MainController> mask;

  // sets up the javafx application.
  // a tray icon is setup for the icon, but the main stage remains invisible until the user
  // interacts with the tray icon.
  @Override public void start(final Stage stage) throws IOException {

    LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

    for (ch.qos.logback.classic.Logger logger : context.getLoggerList()) {
      for (Iterator<Appender<ILoggingEvent>> index = logger.iteratorForAppenders(); index.hasNext(); ) {
        Appender<ILoggingEvent> appender = index.next();

        if (appender instanceof FileAppender) {
          FileAppender<ILoggingEvent> fa = (FileAppender<ILoggingEvent>) appender;
          ResilientFileOutputStream rfos = (ResilientFileOutputStream) fa.getOutputStream();
          File file = rfos.getFile();

          System.out.println(file.getAbsolutePath());
        }
      }
    }

    stage.setTitle("Spica FX Client");
    // stores a reference to the stage.
    this.stage = stage;

    mask = maskLoader.load("main");
    mask.getController().setStage(stage);

    /**TODO enable again
     try {
     GlobalScreen.registerNativeHook();
     } catch (NativeHookException ex) {
     System.err.println("There was a problem registering the native hook.");
     System.err.println(ex.getMessage());

     System.exit(1);
     }

     GlobalScreen.addNativeKeyListener(new NativeKeyListener() {
    @Override public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

    if ((((nativeKeyEvent.getModifiers() & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK)) && nativeKeyEvent
    .getKeyChar() == 's') {
    Platform.runLater(new Runnable() {
    @Override public void run() {
    paste();
    showTasks();
    }
    });
    }

    System.out.println("Key Typed: " + nativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode()));

    }

    @Override public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {

    }

    @Override public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

    }
    });**/

    // instructs the javafx system not to exit implicitly when the last application window is shut.
    Platform.setImplicitExit(false);

    // sets up the tray icon (using awt code run on the swing thread).
    javax.swing.SwingUtilities.invokeLater(this::addAppToTray);

    // out stage will be translucent, so give it a transparent style.
    stage.initStyle(StageStyle.UNDECORATED);

    mask.getController().init();

    Scene scene = mask.getScene();

    scene.setOnDragOver(new EventHandler<DragEvent>() {
      @Override public void handle(DragEvent event) {
        LOGGER.info("onDragOver called");
        event.acceptTransferModes(TransferMode.ANY);
      }
    });
    scene.setOnDragDropped(new EventHandler<DragEvent>() {
      @Override public void handle(DragEvent event) {
        LOGGER.info("OnDragDropped called");

        ClipboardItem clipboardItem = new ClipboardItem();
        clipboardItem.setFiles(event.getDragboard().getFiles());
        clipboardItem.setString(event.getDragboard().getString());
        if (event.getDragboard().getUrl() != null)
          clipboardItem.setUrl(event.getDragboard().getUrl());

        mask.getController().getApplicationContext().getClipboard().getItems().add(clipboardItem);
      }
    });

    scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override public void handle(KeyEvent event) {
        LOGGER.info("onKeyPressed of scene");

        //TODO other operationsystems use CTRL+V
        KeyCodeCombination pasteKeyCodeCombination = new KeyCodeCombination(KeyCode.V, KeyCombination.META_DOWN);
        if (pasteKeyCodeCombination.match(event)) {
          paste();
        }
      }
    });

    // this dummy app just hides itself when the app screen is clicked.
    // a real app might have some interactive UI and a separate icon which hides the app window.
    scene.setFill(Color.TRANSPARENT);
    //stage.setAlwaysOnTop(true);

    stage.setScene(scene);

    showMask(Pages.DIARY, false); //TODO step to DASHBOARD by default as soon as exists

    screenManager.layoutEdged(stage, false);
    LOGGER.info("Stage layouted " + stage.getX() + "-" + stage.getY() + "-" + stage.getWidth() + "-" + stage.getHeight());

  }

  private void paste() {

    Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
    Transferable clipTf = sysClip.getContents(null);
    FilestoreService filestoreService = mask.getController().getActionContext().getServices().getFilestoreService();

    //from https://stackoverflow.com/questions/20174462/how-to-do-cut-copy-paste-in-java

    if (clipTf != null) {

      if (clipTf.isDataFlavorSupported(DataFlavor.imageFlavor)) {
        try {
          MultiResolutionImage multiResolutionImage = (MultiResolutionImage) clipTf
              .getTransferData(DataFlavor.imageFlavor);
          BufferedImage toolkitImage = (BufferedImage) multiResolutionImage.getResolutionVariants().get(0);
          File imageFile = filestoreService.file("Clipboard" + UUID.randomUUID() + ".png").getFile();
          LOGGER.info("Saving image to file " + imageFile.getAbsolutePath());
          ImageIO.write(toolkitImage, "png", imageFile);
          ClipboardItem clipboardItem = new ClipboardItem();
          clipboardItem.setFile(imageFile);
          mask.getController().getApplicationContext().getClipboard().getItems().add(clipboardItem);

        } catch (Exception e) {
          throw new IllegalStateException(e);
        }
      }

      if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
        try {
          String ret = (String) clipTf.getTransferData(DataFlavor.stringFlavor);
          ClipboardItem clipboardItem = new ClipboardItem();
          clipboardItem.setString(ret);
          mask.getController().getApplicationContext().getClipboard().getItems().add(clipboardItem);

        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * Sets up a system tray icon for the application.
   */
  private void addAppToTray() {
    try {
      // ensure awt toolkit is initialized.
      java.awt.Toolkit.getDefaultToolkit();

      // app requires system tray support, just exit if there is no support.
      if (!java.awt.SystemTray.isSupported()) {
        System.out.println("No system tray support, application exiting.");
        Platform.exit();
      }

      boolean isTestEnvironment = new File("build.gradle").exists();

      // set up a system tray icon.
      java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();

      final String icon = isTestEnvironment ? "/spica_test.png" : "/spica.png";
      java.awt.Image image = ImageIO.read(getClass().getResource(icon));
      java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image);

      java.awt.Font defaultFont = java.awt.Font.decode(null);
      java.awt.Font boldFont = defaultFont.deriveFont(java.awt.Font.BOLD);

      // show the main app stage.
      java.awt.MenuItem startTask = new java.awt.MenuItem("Start task");
      startTask.addActionListener(event -> Platform.runLater(this::startTask));
      startTask.setFont(boldFont);

      java.awt.MenuItem startPause = new java.awt.MenuItem("Start pause");
      startPause.addActionListener(event -> Platform.runLater(this::startTask));
      startPause.setFont(boldFont);

      java.awt.MenuItem startPhonecall = new java.awt.MenuItem("Start phonecall");
      startPhonecall.addActionListener(event -> Platform.runLater(this::startTask));
      startPhonecall.setFont(boldFont);

      java.awt.MenuItem finishDay = new java.awt.MenuItem("Finish day");
      finishDay.addActionListener(event -> Platform.runLater(this::startTask));
      finishDay.setFont(boldFont);

      // to really exit the application, the user must go to the system tray icon
      // and select the exit option, this will shutdown JavaFX and remove the
      // tray icon (removing the tray icon will also shut down AWT).
      java.awt.MenuItem exitItem = new java.awt.MenuItem("Exit");
      exitItem.addActionListener(event -> {
        notificationTimer.cancel();

        tray.remove(trayIcon);
        Platform.exit();
        System.exit(0);
      });

      // setup the popup menu for the application.
      final java.awt.PopupMenu popup = new java.awt.PopupMenu();
      popup.addSeparator();
      popup.add(startTask);
      popup.add(startPause);
      popup.add(startPhonecall);
      popup.add(finishDay);

      popup.addSeparator();
      popup.add(exitItem);
      trayIcon.setPopupMenu(popup);

      // create a timer which periodically displays a notification message.
      /**notificationTimer.schedule(
       new TimerTask() {
      @Override public void run() {
      javax.swing.SwingUtilities.invokeLater(() ->
      trayIcon.displayMessage(
      "hello",
      "The time is now " + timeFormat.format(new Date()),
      java.awt.TrayIcon.MessageType.INFO
      )
      );
      }
      },
       5_000,
       60_000
       );**/

      // add the application tray icon to the system tray.
      tray.add(trayIcon);
    } catch (AWTException | IOException e) {
      System.out.println("Unable to init system tray");
      e.printStackTrace();
    }
  }

  private void hide() {
    stage.hide();
  }

  private void startTask() {
    LOGGER.info("start task");

  }

  private void startPause() {
    LOGGER.info("start pause");
  }

  private void telephoneCall() {
    LOGGER.info("telephone call");

  }


  /**
   * Shows the application stage and ensures that it is brought ot the front of all stages.
   */
  private void showMask(Pages pages) {
    showMask(pages, true);
  }

  /**
   * Shows the application stage and ensures that it is brought ot the front of all stages.
   */
  private void showMask(Pages pages, final boolean foldedOut) {
    if (stage != null) {
      stage.show();
      stage.toFront();
      mask.getController().stepToPane(pages, foldedOut);
    }
  }

  public static void main(String[] args) throws IOException, java.awt.AWTException {
    // Just launches the JavaFX application.
    // Due to way the application is coded, the application will remain running
    // until the user selects the Exit menu option from the tray icon.
    launch(args);
  }
}
