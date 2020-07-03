package org.spica.fx;

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
import java.util.Timer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
import org.spica.commons.SpicaProperties;
import org.spica.fx.clipboard.ClipboardItem;
import org.spica.fx.clipboard.AttachmentService;
import org.spica.fx.controllers.MainController;
import org.spica.fx.controllers.Pages;

@Slf4j
public class JavafxApplication extends Application {

  private static final Logger LOGGER = LoggerFactory.getLogger(JavafxApplication.class);

  // application stage is stored so that it can be shown and hidden based on system tray icon operations.
  private Stage stage;

  private ScreenManager screenManager = new ScreenManager();

  // a timer allowing the tray icon to provide a periodic notification event.
  private Timer notificationTimer = new Timer();

  // format used to display the current time in a tray icon notification.
  private DateFormat timeFormat = SimpleDateFormat.getTimeInstance();

  private MaskLoader<MainController> maskLoader = new MaskLoader<MainController>();
  private Mask<MainController> mask;

  // sets up the javafx application.
  // a tray icon is setup for the icon, but the main stage remains invisible until the user
  // interacts with the tray icon.
  @Override public void start(final Stage stage) throws IOException {

    stage.setTitle("Spica FX Client");
    // stores a reference to the stage.
    this.stage = stage;

    mask = maskLoader.load("main");
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

    mask.getController().refreshData();

    Scene scene = mask.getScene();

    scene.setOnDragOver(new EventHandler<DragEvent>() {
      @Override public void handle(DragEvent event) {
        event.acceptTransferModes(TransferMode.ANY);
      }
    });
    scene.setOnDragDropped(new EventHandler<DragEvent>() {
      @Override public void handle(DragEvent event) {

        ClipboardItem clipboardItem = new ClipboardItem();
        clipboardItem.setFiles(event.getDragboard().getFiles());
        clipboardItem.setString(event.getDragboard().getString());
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

    stage.setScene(scene);

    screenManager.layoutEdged(stage);
    LOGGER
        .info("Stage layouted " + stage.getX() + "-" + stage.getY() + "-" + stage.getWidth() + "-" + stage.getHeight());

  }

  private void paste() {

    Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
    Transferable clipTf = sysClip.getContents(null);
    AttachmentService attachmentService = mask.getController().getApplicationContext().getAttachmentService();

    //from https://stackoverflow.com/questions/20174462/how-to-do-cut-copy-paste-in-java

    if (clipTf != null) {

      if (clipTf.isDataFlavorSupported(DataFlavor.imageFlavor)) {
        try {
          MultiResolutionImage multiResolutionImage = (MultiResolutionImage) clipTf
              .getTransferData(DataFlavor.imageFlavor);
          BufferedImage toolkitImage = (BufferedImage) multiResolutionImage.getResolutionVariants().get(0);
          File imageFile = attachmentService.createAttachment();
          LOGGER.info("Saving image to file " + imageFile.getAbsolutePath());
          ImageIO.write(toolkitImage, "png", imageFile);
          ClipboardItem clipboardItem = new ClipboardItem();
          clipboardItem.setFile(imageFile);
          mask.getController().getApplicationContext().getClipboard().getItems().add(clipboardItem);

        } catch (Exception e) {
          e.printStackTrace();
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

      boolean isTestEnvironment = new File ("build.gradle").exists();


      // set up a system tray icon.
      java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();

      final String icon = isTestEnvironment ? "/spica_test.png": "/spica.png";
      java.awt.Image image = ImageIO.read(getClass().getResource(icon));
      java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image);

      // if the user double-clicks on the tray icon, show the main app stage.
      trayIcon.addActionListener(event -> Platform.runLater(this::showPlanning));

      java.awt.Font defaultFont = java.awt.Font.decode(null);
      java.awt.Font boldFont = defaultFont.deriveFont(java.awt.Font.BOLD);

      // show the main app stage.
      java.awt.MenuItem openDashboard = new java.awt.MenuItem("Show dashboard");
      openDashboard.addActionListener(event -> Platform.runLater(this::showDashboard));
      openDashboard.setFont(boldFont);

      java.awt.MenuItem openPlanning = new java.awt.MenuItem("Show planning");
      openPlanning.addActionListener(event -> Platform.runLater(this::showPlanning));
      openPlanning.setFont(boldFont);

      java.awt.MenuItem openMessages = new java.awt.MenuItem("Show messages");
      openMessages.addActionListener(event -> Platform.runLater(this::showMessages));
      openMessages.setFont(boldFont);

      java.awt.MenuItem openTasks = new java.awt.MenuItem("Show tasks");
      openTasks.addActionListener(event -> Platform.runLater(this::showTasks));
      openTasks.setFont(boldFont);

      java.awt.MenuItem openProjects = new java.awt.MenuItem("Show projects");
      openProjects.addActionListener(event -> Platform.runLater(this::showProjects));
      openProjects.setFont(boldFont);

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

      java.awt.MenuItem hideItem = new java.awt.MenuItem("Hide");
      hideItem.addActionListener(event -> Platform.runLater(this::hide));

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
      popup.add(openDashboard);
      popup.add(openPlanning);
      popup.add(openMessages);
      popup.add(openTasks);
      popup.add(openProjects);
      popup.addSeparator();
      popup.add(startTask);
      popup.add(startPause);
      popup.add(startPhonecall);
      popup.add(finishDay);

      popup.addSeparator();
      popup.add(hideItem);
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

  private void showDashboard() {
    showMask(Pages.DASHBOARD);
  }

  private void showPlanning() {
    showMask(Pages.PLANNING);
  }

  private void showTasks() {
    showMask(Pages.TASKS);
  }

  private void showMessages() {
    showMask(Pages.MESSAGES);
  }

  private void showProjects() {
    showMask(Pages.PROJECTS);
  }

  /**
   * Shows the application stage and ensures that it is brought ot the front of all stages.
   */
  private void showMask(Pages pages) {
    if (stage != null) {
      stage.show();
      stage.toFront();
      mask.getController().stepToPane(pages);
    }
  }

  public static void main(String[] args) throws IOException, java.awt.AWTException {
    // Just launches the JavaFX application.
    // Due to way the application is coded, the application will remain running
    // until the user selects the Exit menu option from the tray icon.
    launch(args);
  }
}
