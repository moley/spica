package org.spica.devclient.ui;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.CalendarView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.devclient.DemoData;
import org.spica.javaclient.model.ProjectInfo;

import java.awt.*;
import java.awt.event.InputEvent;
import java.time.LocalDate;
import java.time.LocalTime;


public class MainPageController {

  private final static Logger LOGGER = LoggerFactory.getLogger(MainPageController.class);


  @FXML
  private TextField txtSearch;

  @FXML
  private ListView<ProjectInfo> lviProjects;

  @FXML
  private CalendarView cviTodayCalendar;





  @FXML
  private void initialize() {
    LOGGER.info("Initialize called (" + DemoData.projectInfos.size() + "-" + DemoData.dashboardInfos.size() + ")");
    lviProjects.setItems(FXCollections.observableArrayList(DemoData.projectInfos));
    Calendar birthdays = new Calendar("Birthdays");
    Calendar holidays = new Calendar("Holidays");

    birthdays.setStyle(Calendar.Style.STYLE1);
    holidays.setStyle(Calendar.Style.STYLE2);

    CalendarSource myCalendarSource = new CalendarSource("My Calendars");
    myCalendarSource.getCalendars().addAll(birthdays, holidays);

    cviTodayCalendar.getCalendarSources().addAll(myCalendarSource);

    cviTodayCalendar.setRequestedTime(LocalTime.now());

    Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
      @Override
      public void run() {
        while (true) {
          Platform.runLater(() -> {
            cviTodayCalendar.setToday(LocalDate.now());
            cviTodayCalendar.setTime(LocalTime.now());
          });

          try {
            // update every 10 seconds
            sleep(10000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }

        }
      };
    };

    updateTimeThread.setPriority(Thread.MIN_PRIORITY);
    updateTimeThread.setDaemon(true);
    updateTimeThread.start();


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
      robot.mousePress(InputEvent.BUTTON1_MASK);
    } catch (AWTException e) {
      e.printStackTrace();
    }

  }


}
