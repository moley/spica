package org.spica.devclient.timetracker;

import org.spica.devclient.model.ModelCache;
import org.spica.devclient.model.ModelCacheService;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.EventType;
import org.spica.javaclient.model.TopicInfo;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.List;

public class TimetrackerService {

    public final static String DISPLAY_START_PAUSE = "Start pause";
    public final static String DISPLAY_FINISH_PAUSE = "Finish pause";

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
        pauseEvent.setStart(LocalDateTime.now());
        pauseEvent.setEventType(EventType.PAUSE);
        getModelCache().getEventInfosReal().add(pauseEvent);
    }

    public String togglePause () {
        if (isPause()) {
            stopPause();
            return DISPLAY_START_PAUSE;
        } else {
            startPause();
            return DISPLAY_FINISH_PAUSE;
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


        EventInfo pauseInfo = modelCache.getEventInfosRealToday().get(modelCache.getEventInfosReal().size() - 1);
        if (! pauseInfo.getEventType().equals(EventType.PAUSE))
            throw new IllegalStateException("Your last event is not a started pause. You cannot stop a pause");

        EventInfo lastEventInfo = modelCache.getEventInfosRealToday().get(modelCache.getEventInfosReal().size() - 2);
        EventInfo newStartedEvent = new EventInfo();
        newStartedEvent.setStart(LocalDateTime.now());
        newStartedEvent.setEventType(lastEventInfo.getEventType());
        newStartedEvent.setName(lastEventInfo.getName());
        newStartedEvent.setReferenceId(lastEventInfo.getReferenceId());
        modelCache.getEventInfosReal().add(newStartedEvent);
        modelCacheService.set(modelCache);
    }

    public void startWorkOnTopic (final TopicInfo topicInfo) {
        stopLastOpenEvent();
        ModelCache modelCache = getModelCache();
        EventInfo newStartedEvent = new EventInfo();
        newStartedEvent.setStart(LocalDateTime.now());
        newStartedEvent.setEventType(EventType.TOPIC);
        newStartedEvent.setName(topicInfo.getName());
        newStartedEvent.setReferenceId(topicInfo.getId());
        modelCache.getEventInfosReal().add(newStartedEvent);
        modelCacheService.set(modelCache);
    }

    public void stopWork() {
        stopLastOpenEvent();
        modelCacheService.set(getModelCache());
    }

    public ModelCacheService getModelCacheService() {
        return modelCacheService;
    }

    public void setModelCacheService(ModelCacheService modelCacheService) {
        this.modelCacheService = modelCacheService;
    }

}
