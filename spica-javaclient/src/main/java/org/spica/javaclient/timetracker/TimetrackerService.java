package org.spica.javaclient.timetracker;

import org.spica.javaclient.model.*;
import org.spica.javaclient.utils.DateUtil;

import java.time.LocalDateTime;
import java.util.*;

public class TimetrackerService {

    private DateUtil dateUtil = new DateUtil();

    private ModelCacheService modelCacheService;

    private ModelCache getModelCache () {
        return modelCacheService.get();
    }

    public boolean isPause () {
        ModelCache modelCache = getModelCache();
        EventInfo lastEventInfo = modelCache.findLastOpenEventFromToday();
        return lastEventInfo != null && lastEventInfo.getEventType().equals(EventType.PAUSE);
    }

    public void startPause () {
        stopLastOpenEvent();
        EventInfo pauseEvent = new EventInfo();
        pauseEvent.setId(UUID.randomUUID().toString());
        pauseEvent.setStart(LocalDateTime.now());
        pauseEvent.setEventType(EventType.PAUSE);
        getModelCache().getEventInfosReal().add(pauseEvent);
    }

    public String togglePause () {
        if (isPause()) {
            stopPause();
            return "Pause is stopped at " + dateUtil.getDateAndTimeAsString(LocalDateTime.now());
        } else {
            startPause();
            return "Pause is started at " + dateUtil.getDateAndTimeAsString(LocalDateTime.now());
        }
    }

    private void stopLastOpenEvent() {
        EventInfo eventInfo = getModelCache().findLastOpenEventFromToday();
        if (eventInfo != null) {
            eventInfo.setStop(LocalDateTime.now());
        }
    }

    public void stopPause () {
        stopLastOpenEvent();

        ModelCache modelCache = getModelCache();

        List<EventInfo> eventInfoListToday = modelCache.getEventInfosRealToday();
        if (eventInfoListToday.size() < 2 )
            throw new IllegalStateException("You cannot stop a pause with less than two events. Seems your day started with a pause, which is invalid");

        restartLastRealEvent(modelCache, EventType.PAUSE);
    }

    private void restartLastRealEvent (final ModelCache modelCache, final EventType eventType) {
        EventInfo pauseInfo = modelCache.getEventInfosRealToday().get(modelCache.getEventInfosRealToday().size() - 1);
        if (! pauseInfo.getEventType().equals(eventType))
            throw new IllegalStateException("Your last event is not a started " + eventType.toString() + ". You cannot stop a " + pauseInfo.getEventType().toString() + " at this point");

        if (modelCache.getEventInfosRealToday().size() > 1) {
            EventInfo lastEventInfo = modelCache.getEventInfosRealToday().get(modelCache.getEventInfosRealToday().size() - 2);
            EventInfo newStartedEvent = new EventInfo();
            newStartedEvent.setId(UUID.randomUUID().toString());
            newStartedEvent.setStart(LocalDateTime.now());
            newStartedEvent.setEventType(lastEventInfo.getEventType());
            newStartedEvent.setName(lastEventInfo.getName());
            newStartedEvent.setReferenceId(lastEventInfo.getReferenceId());
            modelCache.getEventInfosReal().add(newStartedEvent);
        }
        modelCacheService.set(modelCache);

    }

    public List<String> createEvent (TimetrackerCreationParam timetrackerCreationParam) {
        List<String> output = new ArrayList<String>();
        if (timetrackerCreationParam == null)
            throw new IllegalArgumentException("Param timetrackerCreationParam must not be null");

        timetrackerCreationParam.validate();

        ModelCache modelCache = getModelCache();
        EventInfo newStartedEvent = new EventInfo();
        newStartedEvent.setId(UUID.randomUUID().toString());
        newStartedEvent.setStart(timetrackerCreationParam.getFromAsLocalDateTime());
        newStartedEvent.setStop(timetrackerCreationParam.getUntilAsLocalDateTime());
        newStartedEvent.setEventType(timetrackerCreationParam.getEventType());

        if (timetrackerCreationParam.getEventType().equals(EventType.MESSAGE)) {
            newStartedEvent.setReferenceId(timetrackerCreationParam.getMessageInfo().getId());
        }

        if (timetrackerCreationParam.getEventType().equals(EventType.TOPIC)) {
            newStartedEvent.setReferenceId(timetrackerCreationParam.getTopicInfo().getId());
        }



        if (timetrackerCreationParam.getUntil() == null && timetrackerCreationParam.getFrom() == null)
            throw new IllegalArgumentException("No period defined (until and from are null");

        EventInfo eventInfoBefore = modelCache.findEventBefore(timetrackerCreationParam.getFromAsLocalDateTime());
        EventInfo eventInfoAfter = modelCache.findEventAfter(timetrackerCreationParam.getFromAsLocalDateTime());


        if (eventInfoBefore == null && eventInfoAfter != null) {

            //If new event is before first event and no end time is defined then set start time of first event as end time of new event
            if (newStartedEvent.getStop() == null) {
                newStartedEvent.setStop(eventInfoAfter.getStart());
                output.add("Limit the new booking to " + dateUtil.getDateAsString(eventInfoAfter.getStart()));
            }

            //If new event is before first event and there is a gap between end time of new event and start time of old first event
            if (newStartedEvent.getStop().isBefore(eventInfoAfter.getStart()))
                throw new IllegalStateException("Gap between stop of new booking and start of old first booking. Configure no until or an until which matches start of first booking");
        }

        if (eventInfoAfter != null) {

            //If new event ends after the next event starts, then adapt the next event to start when new event ends
            if (eventInfoAfter.getStart().isBefore(newStartedEvent.getStop())) {
                eventInfoAfter.setStart(newStartedEvent.getStop());
                output.add("New event ends after start of first event of the day, set start of first event to " + dateUtil.getTimeAsString(newStartedEvent.getStop()));
            }
        }

        int numberOfHiddenElements = 0;
        for (EventInfo next: modelCache.getEventInfosRealToday()) {
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
            eventInfoBefore.setStop(timetrackerCreationParam.getFromAsLocalDateTime());
            output.add("Limit last event of the day to start of new event (" + dateUtil.getTimeAsString(eventInfoBefore.getStop()) + ")");
        }

        output.add("Create event " + newStartedEvent.getId() + " from " + dateUtil.getTimeAsString(newStartedEvent.getStart()) + " to " + dateUtil.getTimeAsString(newStartedEvent.getStop()));
        modelCache.getEventInfosReal().add(newStartedEvent);

        Collections.sort(modelCache.getEventInfosReal(), new Comparator<EventInfo>() {
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

    public void startWorkOnTopic (final TopicInfo topicInfo) {
        stopLastOpenEvent();
        ModelCache modelCache = getModelCache();
        EventInfo newStartedEvent = new EventInfo();
        newStartedEvent.setId(UUID.randomUUID().toString());
        newStartedEvent.setStart(LocalDateTime.now());
        newStartedEvent.setEventType(EventType.TOPIC);
        newStartedEvent.setName(topicInfo.getName());
        newStartedEvent.setReferenceId(topicInfo.getId());
        modelCache.getEventInfosReal().add(newStartedEvent);
        modelCacheService.set(modelCache);
    }

    public void startTelephoneCall () {
        stopLastOpenEvent();
        ModelCache modelCache = getModelCache();
        EventInfo eventInfo = new EventInfo();
        eventInfo.setId(UUID.randomUUID().toString());
        eventInfo.setStart(LocalDateTime.now());
        eventInfo.setEventType(EventType.MESSAGE);
        eventInfo.setName("Telephone call");

        modelCache.getEventInfosReal().add(eventInfo);
        modelCacheService.set(modelCache);
    }

    public void finishTelephoneCall (final MessageInfo messageInfo, UserInfo userInfo) {
        EventInfo eventInfo = getModelCache().findLastOpenEventFromToday();
        if (eventInfo == null)
            throw new IllegalStateException("Message not found, but " + eventInfo + "-" + getModelCache().getEventInfosRealToday());

        if (! eventInfo.getEventType().equals(EventType.MESSAGE))
            throw new IllegalStateException("Last event is expected to be a phone call, but is " + eventInfo);
        else {
            eventInfo.setStop(LocalDateTime.now());
            eventInfo.setName("Telephone call with " + userInfo.getName() + ", " + userInfo.getFirstname());
        }

        eventInfo.setReferenceId(messageInfo.getId());

        ModelCache modelCache = getModelCache();

        restartLastRealEvent(modelCache, EventType.MESSAGE);


    }

    public void finishDay() {
        stopLastOpenEvent();
        modelCacheService.set(getModelCache());
    }

    public ModelCacheService getModelCacheService() {
        return modelCacheService;
    }

    public void setModelCacheService(ModelCacheService modelCacheService) {
        this.modelCacheService = modelCacheService;
    }

    public void finishEvent(EventInfo eventInfo) {
        eventInfo.setStop(LocalDateTime.now());
    }
}
