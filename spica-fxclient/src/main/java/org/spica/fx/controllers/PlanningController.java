package org.spica.fx.controllers;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import javafx.application.Platform;
import javafx.fxml.FXML;

public class PlanningController extends AbstractController {

  @FXML private CalendarView calPlanning;

  @FXML
  public void initialize () {

    Calendar planning = new Calendar("Planning");
    Calendar booking = new Calendar("Booking");

    planning.setStyle(Calendar.Style.STYLE1);
    booking.setStyle(Calendar.Style.STYLE2);

    CalendarSource myCalendarSource = new CalendarSource("My Calendars");
    myCalendarSource.getCalendars().setAll(planning, booking);
    calPlanning.setShowPrintButton(false);
    calPlanning.setShowAddCalendarButton(false);
    calPlanning.setShowSourceTray(false);
    calPlanning.setShowSourceTrayButton(false);
    calPlanning.setTransitionsEnabled(true);
    calPlanning.setShowSearchField(false);

    Entry entry1 = new Entry();
    entry1.setTitle("Make jenkins great again");
    entry1.setInterval(LocalDateTime.now().minus(2, ChronoUnit.HOURS), LocalDateTime.now());

    Entry entry2 = new Entry();
    entry2.setTitle("Make gradle great again");
    entry2.setInterval(LocalDateTime.now(), LocalDateTime.now().plus(2, ChronoUnit.HOURS));

    planning.addEntries(entry1, entry2);

    Entry entry11 = new Entry();
    entry11.setTitle("Doing something different");
    entry11.setInterval(LocalDateTime.now().minus(3, ChronoUnit.HOURS), LocalDateTime.now());

    Entry entry21 = new Entry();
    entry21.setTitle("Make nothing");
    entry21.setInterval(LocalDateTime.now(), LocalDateTime.now().plus(1, ChronoUnit.HOURS));

    booking.addEntries(entry11, entry21);


    calPlanning.getCalendarSources().addAll(myCalendarSource);

    calPlanning.setRequestedTime(LocalTime.now());

    Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
      @Override
      public void run() {
        while (true) {
          Platform.runLater(() -> {
            calPlanning.setToday(LocalDate.now());
            calPlanning.setTime(LocalTime.now());
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

  @Override public void refreshData() {
    getMainController().refreshData();

  }
}
