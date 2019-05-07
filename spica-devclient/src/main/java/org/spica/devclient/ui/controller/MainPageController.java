package org.spica.devclient.ui.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.SpicaProperties;
import org.spica.devclient.DurationInfoService;
import org.spica.devclient.actions.FxActionContext;
import org.spica.devclient.actions.FxActionHandler;
import org.spica.devclient.actions.FxActionParamFactory;
import org.spica.devclient.ui.widgets.ActionSearchableItem;
import org.spica.devclient.ui.widgets.SearchableItem;
import org.spica.devclient.ui.widgets.SearchableTextField;
import org.spica.devclient.ui.widgets.UserSearchableItem;
import org.spica.devclient.util.Mask;
import org.spica.devclient.util.MaskLoader;
import org.spica.javaclient.actions.Action;
import org.spica.javaclient.actions.ActionHandler;
import org.spica.javaclient.actions.FoundAction;
import org.spica.javaclient.model.*;
import org.spica.javaclient.timetracker.TimetrackerService;

import java.awt.*;
import java.awt.event.InputEvent;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class MainPageController {

  private final static Logger LOGGER = LoggerFactory.getLogger(MainPageController.class);


  @FXML
  private TextField txtSearch;




  @FXML
  private ToolBar tbaActions;


  @FXML
  private Label lblTime;

  @FXML
  private Label lblCurrentTask;

  @FXML
  private Label lblAdditionalTaskInfos;

  @FXML
  private ProgressBar pbTask;

  @FXML
  private TabPane tpaMain;


  private TimetrackerService timetrackerService = new TimetrackerService();

  private ActionHandler actionHandler = new ActionHandler();





  SimpleStringProperty propertyCurrentTask = new SimpleStringProperty();
  SimpleStringProperty propertyCurrentTaskAdditionalInfo = new SimpleStringProperty();

  SimpleStringProperty propertyCurrentTime = new SimpleStringProperty();

  SpicaProperties spicaProperties;

  private FxActionContext actionContext = new FxActionContext();

  private DurationInfoService durationInfoService = new DurationInfoService();





  private void refreshTimer (ModelCache modelCache) {
    LOGGER.info("refreshTimer called");
    EventInfo openInfo = modelCache.findLastOpenEventFromToday();
    if (openInfo != null)
      LOGGER.info("Open event: " + openInfo.getName() + "-" + openInfo.getReferenceId());
    else
      LOGGER.info("No open event available");

    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        String currentTask = "";

        if (openInfo != null) {
          if (openInfo.getEventType().equals(EventType.PAUSE))
            currentTask = "Pause";
          else if (openInfo.getName() != null)
            currentTask = openInfo.getName();
        }

        propertyCurrentTask.set(currentTask);

        String localTime = LocalTime.now().format(DateTimeFormatter.ofPattern("H:mm"));
        propertyCurrentTime.set(localTime);

        if (openInfo != null) {
          String durationInfo = durationInfoService.getDisplaytext(openInfo);
          lblAdditionalTaskInfos.setText(durationInfo);
        }
        else
          lblAdditionalTaskInfos.setText("");
      }
    });
  }



  @FXML
  void initialize() {

    ModelCacheService modelCacheService = new ModelCacheService();
    ModelCache modelCache = modelCacheService.get();

    spicaProperties = new SpicaProperties();

    pbTask.setVisible(false);

    pbTask.setProgress(0);

    MaskLoader<DashboardController> maskLoaderDashBoard = new MaskLoader<DashboardController>();
    Mask<DashboardController> maskDashboard = maskLoaderDashBoard.load("dashboard");

    MaskLoader<PlanningController> maskLoaderPlanning = new MaskLoader<PlanningController>();
    Mask<PlanningController> maskPlanning = maskLoaderPlanning.load("planning");

    List<SearchableItem> searchableActions = new ArrayList<SearchableItem>();
    ActionHandler actionHandler = new ActionHandler();
    for (Action nextAction: actionHandler.getRegisteredActions()) {
      searchableActions.add(new ActionSearchableItem(nextAction));
    }

    List<SearchableItem> searchableItemsSearch = new ArrayList<SearchableItem>();
    for (UserInfo nextUser: modelCache.getUserInfos()) {
      searchableItemsSearch.add(new UserSearchableItem(nextUser));
    }


    tpaMain.getTabs().add(new Tab("Dashboard", maskDashboard.getNode()));
    tpaMain.getTabs().add(new Tab("Planning", maskPlanning.getNode()));
//    tpaMain.getTabs().add(new Tab("New topic"));
//    tpaMain.getTabs().add(new Tab("Phonecall"));
    SearchableTextField searchableTextField = new SearchableTextField(txtSearch);
    searchableTextField.addSearchStrategy(":", searchableActions, "Command");
    searchableTextField.addSearchStrategy("", searchableItemsSearch, "Search");

    /**TODO txtSearch.focusedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (oldValue.equals(Boolean.TRUE) && newValue.equals(Boolean.FALSE)) {
          txtSearch.setText("Press SHIFT-ALT-SPACE to focus search again");
        }

        if (oldValue.equals(Boolean.FALSE && newValue.equals(Boolean.TRUE))) {
          txtSearch.setText("");
        }

      }
    });**/

    txtSearch.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
          txtSearch.setText("");

          SearchableItem selectedSearchableItem = searchableTextField.getSelectedSearchableItem();
          if (selectedSearchableItem instanceof ActionSearchableItem) {
            ActionSearchableItem actionSearchableItem = (ActionSearchableItem) selectedSearchableItem;
            Action action = actionSearchableItem.getAction();
            FoundAction foundAction = new FoundAction(action, ""); //TODO PARAMETER
            if (! action.getInputParams(actionContext).isEmpty()) {
              FxActionParamFactory actionParamFactory = new FxActionParamFactory(tpaMain);

              actionParamFactory.build(actionContext, foundAction);
            }
            else
              action.execute(actionContext, action.getInputParams(actionContext), foundAction.getParameter());

          }
          else {
            System.out.println ("Find Action on " + selectedSearchableItem.getDisplayname());

          }


        }
      }
    });

    lblCurrentTask.textProperty().bindBidirectional(propertyCurrentTask);
    lblTime.textProperty().bindBidirectional(propertyCurrentTime);


    timetrackerService.setModelCacheService(modelCacheService);

    FxActionHandler fxActionHandler = new FxActionHandler();
    fxActionHandler.createButtons(actionContext, tbaActions, tpaMain);

    Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
      @Override
      public void run() {
        while (true) {
          try {
            // update every 10 seconds
            sleep(4000);
            refreshTimer(modelCache);
          } catch (InterruptedException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
          }

        }
      };
    };

    updateTimeThread.setPriority(Thread.MIN_PRIORITY);
    updateTimeThread.setDaemon(true);
    updateTimeThread.start();

    refreshTimer(modelCache);


  }


  public void toFront () {
    Bounds bounds = txtSearch.localToScreen(txtSearch.getLayoutBounds());
    System.out.println (bounds.getMinX() + "-" + bounds.getMinY());
    txtSearch.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, bounds.getMinX() + 10,
      bounds.getMinY() + 10, bounds.getMinX() + 10, bounds.getMinY(), MouseButton.PRIMARY, 1,
      true, true, true, true, true, true, true, true, true, true, null));

    try {
      Robot robot = new Robot();
      robot.mouseMove((int)bounds.getMinX() + 10,(int) bounds.getMinY() + 10);
      robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    } catch (AWTException e) {
      LOGGER.error(e.getLocalizedMessage(), e);
    }

  }


}
