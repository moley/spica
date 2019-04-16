package org.spica.devclient.ui.controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.calendarfx.view.DateControl;
import com.calendarfx.view.page.DayPage;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.SpicaProperties;
import org.spica.devclient.actions.FxActionContext;
import org.spica.devclient.actions.FxActionHandler;
import org.spica.devclient.actions.FxActionParamFactory;
import org.spica.javaclient.actions.Action;
import org.spica.javaclient.actions.ActionHandler;
import org.spica.javaclient.actions.FoundAction;
import org.spica.javaclient.actions.GotoJiraAction;
import org.spica.javaclient.actions.params.InputParams;
import org.spica.javaclient.model.*;
import org.spica.javaclient.timetracker.TimetrackerService;

import java.awt.*;
import java.awt.event.InputEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;


public class MainPageController {

  private final static Logger LOGGER = LoggerFactory.getLogger(MainPageController.class);


  @FXML
  private TextField txtSearch;

  @FXML
  private ListView<TopicInfo> lviTopics;

  @FXML
  private DayPage cviDayPage;

  @FXML
  private ToolBar tbaActions;


  @FXML
  private Label lblTime;

  @FXML
  private Label lblCurrentTask;

  @FXML
  private Label lblCurrentTaskAdditionalInfos;

  @FXML
  private ProgressBar pbTask;


  private TimetrackerService timetrackerService = new TimetrackerService();

  private ActionHandler actionHandler = new ActionHandler();

  private Collection<Entry<?>> plannedEntries = new ArrayList<Entry<?>>();

  private Collection<Entry<?>> realEntries = new ArrayList<Entry<?>>();

  Calendar plannedCalendar = new Calendar("Planned");
  Calendar realCalendar = new Calendar("Done");

  SimpleStringProperty propertyCurrentTask = new SimpleStringProperty();
  SimpleStringProperty propertyCurrentTaskAdditionalInfo = new SimpleStringProperty();

  SimpleStringProperty propertyCurrentTime = new SimpleStringProperty();

  SpicaProperties spicaProperties;

  private FxActionContext actionContext = new FxActionContext();



  private Entry toEntry (final EventInfo eventInfo) {
    LocalDateTime until = eventInfo.getStop() != null ? eventInfo.getStop() : LocalDateTime.now();
    Entry entry = new Entry(eventInfo.getName(), new Interval(eventInfo.getStart(), until));
    return entry;
  }

  private void refreshTimer (ModelCache modelCache) {
    realCalendar.removeEntries(realEntries);
    realEntries.clear();
    for (EventInfo next: modelCache.getEventInfosReal()) {
      if (! next.getEventType().equals(EventType.PAUSE)) {
        LOGGER.info("refresh " + next.getName() + " from " + next.getStart() + " until " + next.getStop());
        realEntries.add(toEntry(next));
      }
      else
        LOGGER.info("do not refresh " + next.getName());
    }

    EventInfo openInfo = modelCache.findLastOpenEventFromToday();
    if (openInfo != null)
      LOGGER.info("Open event: " + openInfo.getName() + "-" + openInfo.getReferenceId());
    else
      LOGGER.info("No open event available");

    realCalendar.addEntries(realEntries);

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

        if (openInfo != null && openInfo.getReferenceId() != null) {
          TopicInfo topicInfo = modelCache.findTopicInfoById(openInfo.getReferenceId());
          lviTopics.getSelectionModel().select(topicInfo);
        }
      }
    });
  }



  @FXML
  void initialize() {

    spicaProperties = new SpicaProperties();

    pbTask.setProgress(0);

    txtSearch.focusedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (oldValue.equals(Boolean.TRUE) && newValue.equals(Boolean.FALSE)) {
          txtSearch.setText("Press SHIFT-ALT-SPACE to focus search again");
        }

        if (oldValue.equals(Boolean.FALSE && newValue.equals(Boolean.TRUE))) {
          txtSearch.setText("");
        }

      }
    });

    txtSearch.setOnKeyReleased(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
          FoundAction foundAction = actionHandler.findAction(txtSearch.getText());
          Action action = foundAction.getAction();

          if (! action.getInputParams().isEmpty()) {
            FxActionParamFactory actionParamFactory = new FxActionParamFactory();
            actionParamFactory.build(actionContext, foundAction);
          }
          else
            action.execute(actionContext, action.getInputParams(), foundAction.getParameter());
        }

      }
    });

    lblCurrentTask.textProperty().bindBidirectional(propertyCurrentTask);
    lblTime.textProperty().bindBidirectional(propertyCurrentTime);

    ModelCacheService modelCacheService = new ModelCacheService();
    ModelCache modelCache = modelCacheService.get();
    timetrackerService.setModelCacheService(modelCacheService);

    FxActionHandler fxActionHandler = new FxActionHandler();
    fxActionHandler.createButtons(actionContext, tbaActions);



    lviTopics.setCellFactory(new Callback<ListView<TopicInfo>, ListCell<TopicInfo>>() {
      @Override
      public ListCell<TopicInfo> call(ListView<TopicInfo> studentListView) {
        return new ListCell<TopicInfo>() {
          @Override
          protected void updateItem(TopicInfo topicInfo, boolean empty) {
            super.updateItem(topicInfo, empty);
            if (empty || topicInfo == null) {
              setText(null);
              setGraphic(null);
            } else {
              if (topicInfo.getExternalSystemKey() != null)
                setText(topicInfo.getExternalSystemKey() + ": " + topicInfo.getName());
              else
                setText(topicInfo.getName());
            }

          }
        };
      }
    });
    lviTopics.setItems(FXCollections.observableArrayList(modelCache.getTopicInfos()));

    lviTopics.setOnMouseReleased(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        // Your action here

        if (event.getClickCount() == 2) {
          FoundAction gotoJiraAction = actionHandler.findAction(GotoJiraAction.class);
          gotoJiraAction.getAction().execute(actionContext, new InputParams(),"");
        }
        else if (event.getClickCount() == 1) {

          TopicInfo newValue = lviTopics.getSelectionModel().getSelectedItem();

          System.out.println("Selected item: " + newValue.getId() + "-" + newValue.getName());
          timetrackerService.startWorkOnTopic(newValue);

          refreshTimer(modelCache);
        }

      }
    });



    plannedCalendar.setStyle(Calendar.Style.STYLE1);
    realCalendar.setStyle(Calendar.Style.STYLE2);

    //TODO plannedCalendar.addEntries(ModelCache.plannedEntries);
    //TODO realCalendar.addEntries(ModelCache.realEntries);

    CalendarSource myCalendarSource = new CalendarSource("My Calendars");
    myCalendarSource.getCalendars().addAll(plannedCalendar, realCalendar);

    cviDayPage.getCalendarSources().addAll(myCalendarSource);
    cviDayPage.setShowDayPageLayoutControls(false);
    cviDayPage.setShowNavigation(false);
    cviDayPage.setEntryDetailsPopOverContentCallback(new Callback<DateControl.EntryDetailsPopOverContentParameter, Node>() {
      @Override
      public Node call(DateControl.EntryDetailsPopOverContentParameter entryDetailsPopOverContentParameter) {
        /**Button btn = new Button("Show"); //TODO Logic for popup
        btn.setMinHeight(1000);
        btn.setMinWidth(1000);**/
        return null;
      }
    });


    cviDayPage.setDayPageLayout(DayPage.DayPageLayout.DAY_ONLY);
    cviDayPage.setShowLayoutButton(true);

    cviDayPage.setRequestedTime(LocalTime.now());

    Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
      @Override
      public void run() {
        while (true) {
          Platform.runLater(() -> {
            cviDayPage.setToday(LocalDate.now());
            cviDayPage.setTime(LocalTime.now());
          });

          try {
            // update every 10 seconds
            sleep(10000);
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
