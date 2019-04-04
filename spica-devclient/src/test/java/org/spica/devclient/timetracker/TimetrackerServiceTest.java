package org.spica.devclient.timetracker;

import com.google.common.io.Files;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.spica.devclient.model.ModelCache;
import org.spica.devclient.model.ModelCacheService;
import org.spica.javaclient.model.EventType;
import org.spica.javaclient.model.TopicInfo;

import java.io.File;

public class TimetrackerServiceTest {

    private ModelCacheService modelCacheService = new ModelCacheService();

    private TimetrackerService timetrackerService;

    private File tmpDir = Files.createTempDir();

    @BeforeEach
    public void beforeEach() {
        modelCacheService.close();
        modelCacheService.setConfigFile(new File (tmpDir, "config.xml"));
        modelCacheService.set(new ModelCache());
        timetrackerService = new TimetrackerService();
        timetrackerService.setModelCacheService(modelCacheService);

    }

    @Test
    public void startPause() {
        timetrackerService.startPause();
        ModelCache modelCache = modelCacheService.get();
        Assert.assertEquals("Number of entries invalid", 1, modelCache.getEventInfosReal().size());
        Assert.assertEquals("Pause Type invalid", EventType.PAUSE, modelCache.getEventInfosReal().get(0).getEventType());
    }

    @Test()
    public void stopPauseAsFirst() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            modelCacheService.get();
            timetrackerService.stopPause();
        });
    }

    @Test
    public void stopPauseAfterNonClosedChat() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            TopicInfo topicInfo = new TopicInfo().name("Open topic");
            TopicInfo topicInfo2 = new TopicInfo().name("Open topic");
            ModelCache modelCache = modelCacheService.get();

            timetrackerService.startWorkOnTopic(topicInfo);
            timetrackerService.startWorkOnTopic(topicInfo2);
            System.out.println(modelCache.getEventInfosRealToday());
            timetrackerService.stopPause();
        });



    }

    @Test
    public void startNewTask() {
        TopicInfo topicInfo = new TopicInfo().name("Open topic");
        ModelCache modelCache = modelCacheService.get();
        Assert.assertEquals (0, modelCache.getEventInfosRealToday().size());
        timetrackerService.startWorkOnTopic(topicInfo);
        Assert.assertEquals (1, modelCache.getEventInfosRealToday().size());
    }

    @Test
    public void restartPausedTask() {
        TopicInfo topicInfo = new TopicInfo().name("Open topic");
        ModelCache modelCache = modelCacheService.get();
        timetrackerService.startWorkOnTopic(topicInfo);
        timetrackerService.startPause();
        timetrackerService.stopPause();
        Assert.assertEquals (3, modelCache.getEventInfosRealToday().size());
    }

    @Test
    public void finishDay() {

    }
}
