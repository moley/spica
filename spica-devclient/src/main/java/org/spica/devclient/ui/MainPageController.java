package org.spica.devclient.ui;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.DateControl;
import com.calendarfx.view.VirtualGrid;
import com.calendarfx.view.page.DayPage;
import com.calendarfx.view.popover.EntryPopOverContentPane;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
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
  private DayPage cviDayPage;





  @FXML
  void initialize() {
    lviProjects.setItems(FXCollections.observableArrayList(DemoData.projectInfos));
    Calendar plannedCalendar = new Calendar("Planned");
    Calendar realCalendar = new Calendar("Done");

    plannedCalendar.setStyle(Calendar.Style.STYLE1);
    realCalendar.setStyle(Calendar.Style.STYLE2);

    //TODO plannedCalendar.addEntries(DemoData.plannedEntries);
    //TODO realCalendar.addEntries(DemoData.realEntries);

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
          } catch (InterruptedException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
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
      robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    } catch (AWTException e) {
      LOGGER.error(e.getLocalizedMessage(), e);
    }

  }


}
