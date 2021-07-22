package org.spica.fx.controllers;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarEvent;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.page.DayPage;
import java.time.LocalDate;
import java.time.LocalTime;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import lombok.extern.slf4j.Slf4j;
import org.spica.javaclient.events.EventParam;
import org.spica.javaclient.events.EventService;
import org.spica.javaclient.model.EventInfo;

@Slf4j
public class EventsController extends AbstractController {

  @FXML private CalendarView calPlanning;

  private Calendar booking;

  @FXML
  public void initialize () {

    Calendar calendar = new Calendar("Booking");

    CalendarSource myCalendarSource = new CalendarSource("My Calendars");
    myCalendarSource.getCalendars().setAll(calendar);
    booking = myCalendarSource.getCalendars().get(0);
    booking.setStyle(Calendar.Style.STYLE2);
    booking.addEventHandler(new EventHandler<CalendarEvent>() {
      @Override public void handle(CalendarEvent event) {

        EventService eventService = getServices().getEventService();
        Entry entry = event.getEntry();
        EventParam eventParam = new EventParam();
        eventParam.setTopic(entry.getTitle());
        eventParam.setFrom(entry.getStartTime());
        eventParam.setUntil(entry.getEndTime());
        eventParam.setDate(LocalDate.from(entry.getStartAsLocalDateTime()));

        log.info("Event " + event.getEventType() + "-"+event.getEntry() + "-" + event.isEntryAdded() + "-" + event.isEntryRemoved());

        if (event.getEventType().equals(CalendarEvent.ENTRY_INTERVAL_CHANGED)) {
          log.info("- Interval changed");
          eventService.updateEvent(entry.getId(), eventParam );
        }

        if (event.getEventType().equals(CalendarEvent.ENTRY_CALENDAR_CHANGED)) {
          if (event.isEntryAdded()) {
            log.info("- Entry added");


          }

          if (event.isEntryRemoved())
            log.info("- Entry removed");
        }

        if (event.getEventType().equals(CalendarEvent.ENTRY_USER_OBJECT_CHANGED)) {
          log.info("- Entry User Object changed");

        }

        return;

      }
    });



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

    calPlanning.getCalendarSources().setAll(myCalendarSource);

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
