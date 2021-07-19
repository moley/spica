package org.spica.fx.controllers;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarEvent;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.page.DayPage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import lombok.extern.slf4j.Slf4j;
import org.spica.javaclient.model.EventInfo;

@Slf4j
public class DiaryController extends AbstractController {

  @FXML private CalendarView calPlanning;

  private Calendar booking = new Calendar("Booking");

  @FXML
  public void initialize () {



    booking.setStyle(Calendar.Style.STYLE2);
    booking.addEventHandler(new EventHandler<CalendarEvent>() {
      @Override public void handle(CalendarEvent event) {
        log.info("Event " + event.getEventType() + "-"+event.getEntry());

        return;

      }
    });

    CalendarSource myCalendarSource = new CalendarSource("My Calendars");
    myCalendarSource.getCalendars().setAll(booking);
    calPlanning.setShowPrintButton(false);
    calPlanning.setShowAddCalendarButton(false);
    calPlanning.setShowSourceTray(false);
    calPlanning.setShowSourceTrayButton(false);
    calPlanning.setTransitionsEnabled(true);
    calPlanning.setShowSearchField(false);
    calPlanning.getDayPage().getAgendaView().setVisible(false);
    calPlanning.getDayPage().getDetailedDayView().setVisible(false);
    calPlanning.getDayPage().getYearMonthView().setVisible(false);
    calPlanning.getDayPage().setDayPageLayout(DayPage.DayPageLayout.DAY_ONLY);







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

    booking.clear();

    for (EventInfo nextEvent: getModel().getEventInfosReal()) {
      Entry entry = new Entry();
      entry.setTitle(nextEvent.getName());
      entry.setInterval(nextEvent.getStart(),nextEvent.getStop());
      entry.setId(nextEvent.getId());
      booking.addEntry(entry);
    }

  }
}
