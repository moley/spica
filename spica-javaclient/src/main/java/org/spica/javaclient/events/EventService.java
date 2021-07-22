package org.spica.javaclient.events;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.spica.commons.DateUtils;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.EventType;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.TaskInfo;
import org.spica.javaclient.model.UserInfo;
import org.spica.javaclient.services.ModelCacheService;

/**
 * This services does a lot of timetracking things.
 * Also saves the model at the end with a dedicated save notice
 */
public class EventService {

    private DateUtils dateUtils = new DateUtils();

    private ModelCacheService modelCacheService;

    private Model getModelCache () {
        return modelCacheService.load();
    }

    public boolean isPause () {
        Model model = getModelCache();
        EventInfo lastEventInfo = model.findLastOpenEventFromToday();
        return lastEventInfo != null && lastEventInfo.getEventType().equals(EventType.PAUSE);
    }

    public void startPause () {
        stopLastOpenEvent(null);
        EventInfo pauseEvent = new EventInfo();
        pauseEvent.setId(UUID.randomUUID().toString());
        pauseEvent.setStart(LocalDateTime.now());
        pauseEvent.setEventType(EventType.PAUSE);
        getModelCache().getEventInfosReal().add(pauseEvent);
    }

    public void updateEvent(final String id, final EventParam eventParam) {

        EventInfo eventInfo = getModelCache().findEventInfoRealById(id);
        if (eventInfo == null)
            throw new IllegalStateException("No event found with id " + id);
        eventInfo.setName(eventParam.getTopic());

        eventInfo.setStart(LocalDateTime.of(eventParam.getDate(), eventParam.getFrom()));
        eventInfo.setStop(LocalDateTime.of(eventParam.getDate(), eventParam.getUntil()));

        modelCacheService.save(getModelCache(), "Updated booking " + id + " with (" + eventInfo.getName() + ", " + eventInfo.getStart() + ", " + eventInfo.getStop());

    }

    public String togglePause () {
        if (isPause()) {
            stopPause();
            return "Pause is stopped at " + dateUtils.getDateAndTimeAsString(LocalDateTime.now());
        } else {
            startPause();
            return "Pause is started at " + dateUtils.getDateAndTimeAsString(LocalDateTime.now());
        }
    }

    private void stopLastOpenEvent(LocalDateTime stopTime) {
        EventInfo eventInfo = getModelCache().findLastOpenEventFromToday();
        if (eventInfo != null) {
            eventInfo.setStop(stopTime != null ? stopTime: LocalDateTime.now());
        }
    }

    public void stopPause () {
        stopLastOpenEvent(null);

        Model model = getModelCache();

        List<EventInfo> eventInfoListToday = model.getEventInfosRealToday();
        if (eventInfoListToday.size() < 2 )
            throw new IllegalStateException("You cannot stop a pause with less than two events. Seems your day started with a pause, which is invalid");

        restartLastRealEvent(model, EventType.PAUSE);
    }

    private void restartLastRealEvent (final Model model, final EventType eventType) {
        EventInfo pauseInfo = model.getEventInfosRealToday().get(model.getEventInfosRealToday().size() - 1);
        if (! pauseInfo.getEventType().equals(eventType))
            throw new IllegalStateException("Your last event is not a started " + eventType.toString() + ". You cannot stop a " + pauseInfo.getEventType().toString() + " at this point");

        if (model.getEventInfosRealToday().size() > 1) {
            EventInfo lastEventInfo = model.getEventInfosRealToday().get(model.getEventInfosRealToday().size() - 2);
            EventInfo newStartedEvent = new EventInfo();
            newStartedEvent.setId(UUID.randomUUID().toString());
            newStartedEvent.setStart(LocalDateTime.now());
            newStartedEvent.setEventType(lastEventInfo.getEventType());
            newStartedEvent.setName(lastEventInfo.getName());
            newStartedEvent.setReferenceId(lastEventInfo.getReferenceId());
            model.getEventInfosReal().add(newStartedEvent);
        }
        modelCacheService.save(model, "restart last real event" + eventType.name());

    }

    public List<String> createEvent (EventParam eventParam) {
        List<String> output = new ArrayList<String>();
        if (eventParam == null)
            throw new IllegalArgumentException("Param timetrackerCreationParam must not be null");

        eventParam.validate();

        Model model = getModelCache();
        EventInfo newStartedEvent = new EventInfo();
        newStartedEvent.setId(UUID.randomUUID().toString());
        newStartedEvent.setStart(eventParam.getFromAsLocalDateTime());
        newStartedEvent.setStop(eventParam.getUntilAsLocalDateTime());
        newStartedEvent.setEventType(eventParam.getEventType());

        if (eventParam.getEventType().equals(EventType.MESSAGE)) {
            newStartedEvent.setReferenceId(eventParam.getMessageInfo().getId());
            newStartedEvent.setName("Telephone call"); //TODO With
        }

        if (eventParam.getEventType().equals(EventType.TASK)) {
            newStartedEvent.setReferenceId(eventParam.getTaskInfo().getId());
            newStartedEvent.setName(eventParam.getTaskInfo().getName());
        }



        if (eventParam.getUntil() == null && eventParam.getFrom() == null)
            throw new IllegalArgumentException("No period defined (until and from are null");

        EventInfo eventInfoBefore = model.findEventBefore(eventParam.getFromAsLocalDateTime());
        EventInfo eventInfoAfter = model.findEventAfter(eventParam.getFromAsLocalDateTime());


        if (eventInfoBefore == null && eventInfoAfter != null) {

            //If new event is before first event and no end time is defined then set start time of first event as end time of new event
            if (newStartedEvent.getStop() == null) {
                newStartedEvent.setStop(eventInfoAfter.getStart());
                output.add("Limit the new booking to " + dateUtils.getDateAsString(eventInfoAfter.getStart()));
            }
        }

        if (eventInfoAfter != null) {
            output.add("Found subsequent event " + eventInfoAfter.getName() + " (" + eventInfoAfter.getStart() + "-" + eventInfoAfter.getStop());

            //If new event ends after the next event starts, then adapt the next event to start when new event ends
            if (eventInfoAfter.getStart().isBefore(newStartedEvent.getStop())) {
                eventInfoAfter.setStart(newStartedEvent.getStop());
                output.add("New event ends after start of first event of the day, limit first event to " + dateUtils.getTimeAsString(newStartedEvent.getStop()));
            }
        }

        int numberOfHiddenElements = 0;
        for (EventInfo next: model.getEventInfosRealToday()) {
            if (next.getStart().isAfter(newStartedEvent.getStart())) {
                if (newStartedEvent.getStop() == null)
                    numberOfHiddenElements++;
                else if (next.getStop() != null && newStartedEvent.getStop().isAfter(next.getStop()))
                    numberOfHiddenElements++;
            }
        }

        if (numberOfHiddenElements > 0)
            throw new IllegalStateException(numberOfHiddenElements + " existing events would be hidden by new event");

        //If there is a event before then limit this to the start of the new one
        if (eventInfoBefore != null) {
            eventInfoBefore.setStop(eventParam.getFromAsLocalDateTime());
            output.add("Limit last event of the day to start of new event (" + dateUtils.getTimeAsString(eventInfoBefore.getStop()) + ")");
        }

        output.add("Create event " + newStartedEvent.getId() + " from " + dateUtils.getTimeAsString(newStartedEvent.getStart()) + " to " + dateUtils
            .getTimeAsString(newStartedEvent.getStop()));
        model.getEventInfosReal().add(newStartedEvent);

        Collections.sort(model.getEventInfosReal(), new Comparator<EventInfo>() {
            @Override
            public int compare(EventInfo o1, EventInfo o2) {
                LocalDateTime localDateTime1 = o1.getStart();
                if (localDateTime1 == null)
                    throw new IllegalStateException("No start time at event " + o1.getId());
                LocalDateTime localDateTime2 = o2.getStart();
                if (localDateTime2 == null)
                    throw new IllegalStateException("No start time at event " + o2.getId());
                return localDateTime1.compareTo(localDateTime2);
            }
        });

        return output;

    }

    public void startWorkOnTask (final TaskInfo topicInfo) {
        stopLastOpenEvent(null);
        Model model = getModelCache();
        EventInfo newStartedEvent = new EventInfo();
        newStartedEvent.setId(UUID.randomUUID().toString());
        newStartedEvent.setStart(LocalDateTime.now());
        newStartedEvent.setEventType(EventType.TASK);
        newStartedEvent.setName(topicInfo.getName());
        newStartedEvent.setReferenceId(topicInfo.getId());
        model.getEventInfosReal().add(newStartedEvent);
        modelCacheService.save(model, "Start work on topic");
    }

    public void startTelephoneCall () {
        stopLastOpenEvent(null);
        Model model = getModelCache();
        EventInfo eventInfo = new EventInfo();
        eventInfo.setId(UUID.randomUUID().toString());
        eventInfo.setStart(LocalDateTime.now());
        eventInfo.setEventType(EventType.MESSAGE);
        eventInfo.setName("Telephone call");

        model.getEventInfosReal().add(eventInfo);
        modelCacheService.save(model, "Start telephone call");
    }

    public void finishTelephoneCall (final MessageInfo messageInfo, UserInfo userInfo, final boolean restartPreviousWork) {
        EventInfo eventInfo = getModelCache().findLastOpenEventFromToday();
        if (eventInfo == null)
            throw new IllegalStateException("Message not found, but " + eventInfo + "-" + getModelCache().getEventInfosRealToday());

        if (! eventInfo.getEventType().equals(EventType.MESSAGE))
            throw new IllegalStateException("Last event is expected to be a phone call, but is " + eventInfo);
        else {
            eventInfo.setStop(LocalDateTime.now());
            String userinfo = userInfo != null ? userInfo.getName() + ", " + userInfo.getFirstname() : "unknown user";
            eventInfo.setName("Telephone call with " + userinfo);
        }

        eventInfo.setReferenceId(messageInfo.getId());

        Model model = getModelCache();

        if (restartPreviousWork)
          restartLastRealEvent(model, EventType.MESSAGE);

    }

    public void finishDay(LocalDateTime localDateTime) {
        stopLastOpenEvent(localDateTime);
        modelCacheService.save(getModelCache(), "Finish day");
    }

    public ModelCacheService getModelCacheService() {
        return modelCacheService;
    }

    public void setModelCacheService(ModelCacheService modelCacheService) {
        this.modelCacheService = modelCacheService;
    }

    public LocalDateTime finishEvent(EventInfo eventInfo) {
        LocalDateTime now = LocalDateTime.now();
        eventInfo.setStop(now);

        return now;
    }

    public void removeEvent(EventInfo eventInfo) {
        if (eventInfo == null)
            throw new IllegalArgumentException("Argument event must not be null");
        getModelCache().getEventInfosReal().remove(eventInfo);
        modelCacheService.save(getModelCache(), "Removed event " + eventInfo.getId());
    }
}
