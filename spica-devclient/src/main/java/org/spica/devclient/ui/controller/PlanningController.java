package org.spica.devclient.ui.controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.calendarfx.view.DateControl;
import com.calendarfx.view.page.DayPage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.SpicaProperties;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.EventType;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.ModelCacheService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;

public class PlanningController {

    private final static Logger LOGGER = LoggerFactory.getLogger(PlanningController.class);


    @FXML
    private DayPage cviDayPage;

    Calendar plannedCalendar = new Calendar("Planned");
    Calendar realCalendar = new Calendar("Done");

    private Collection<Entry<?>> plannedEntries = new ArrayList<Entry<?>>();

    private Collection<Entry<?>> realEntries = new ArrayList<Entry<?>>();

    SpicaProperties spicaProperties;

    ModelCacheService modelCacheService = new ModelCacheService();
    ModelCache modelCache = modelCacheService.get();


    private Entry toEntry(final EventInfo eventInfo, final Calendar calendar) {
        LocalDateTime until = eventInfo.getStop() != null ? eventInfo.getStop() : LocalDateTime.now();
        if (eventInfo.getName() == null)
            throw new IllegalStateException("Name must not be null for event " + eventInfo.getId());
        if (eventInfo.getStart() == null)
            throw new IllegalStateException("Start must not be null for event " + eventInfo.getId());
        Entry entry = new Entry(eventInfo.getName(), new Interval(eventInfo.getStart(), until));
        entry.setCalendar(calendar);
        return entry;
    }

    private void refreshTimer(ModelCache modelCache) {
        LOGGER.info("refreshTimer called");
        realCalendar.removeEntries(realEntries);
        realEntries.clear();
        for (EventInfo next : modelCache.getEventInfosReal()) {
            if (!next.getEventType().equals(EventType.PAUSE)) {
                LOGGER.info("refresh " + next.getName() + " from " + next.getStart() + " until " + next.getStop());
                realEntries.add(toEntry(next, realCalendar));
            } else
                LOGGER.info("do not refresh " + next.getName());
        }

        EventInfo openInfo = modelCache.findLastOpenEventFromToday();
        if (openInfo != null)
            LOGGER.info("Open event: " + openInfo.getName() + "-" + openInfo.getReferenceId());
        else
            LOGGER.info("No open event available");

        realCalendar.addEntries(realEntries);


    }

    @FXML
    void initialize() {

        spicaProperties = new SpicaProperties();


        ModelCacheService modelCacheService = new ModelCacheService();
        ModelCache modelCache = modelCacheService.get();


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
                Button btn = new Button("Show"); //TODO Logic for popup
                btn.setMinHeight(1000);
                btn.setMinWidth(1000);
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
                        sleep(4000);
                        refreshTimer(modelCache);
                    } catch (InterruptedException e) {
                        LOGGER.error(e.getLocalizedMessage(), e);
                    }

                }
            }

            ;
        };

        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();

        refreshTimer(modelCache);


    }
}
