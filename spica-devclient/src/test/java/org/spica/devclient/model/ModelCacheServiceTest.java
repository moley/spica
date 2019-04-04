package org.spica.devclient.model;

import com.google.common.io.Files;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.TopicInfo;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;

public class ModelCacheServiceTest {

    @Test
    public void saveAndLoad () {

        File tmpDir = Files.createTempDir();
        File configFile = new File (tmpDir, "model.xml");

        TopicInfo topicInfo1 = new TopicInfo().name("name1").description("description1");
        TopicInfo topicInfo2 = new TopicInfo().name("name2").description("description2");

        EventInfo eventInfo1 = new EventInfo().start(LocalDateTime.now());

        ModelCache modelCache = new ModelCache();
        modelCache.setTopicInfos(Arrays.asList(topicInfo1, topicInfo2));
        modelCache.setEventInfosReal(Arrays.asList(eventInfo1));
        ModelCacheService modelCacheService = new ModelCacheService();
        modelCacheService.setConfigFile(configFile);
        modelCacheService.set(modelCache);

        modelCacheService.close();
        ModelCache loadedCache = modelCacheService.get();
        Assert.assertEquals ("Number of loaded topics invalid", 2, loadedCache.getTopicInfos().size());
        LocalDateTime startLoaded = loadedCache.getEventInfosReal().get(0).getStart();
        Assert.assertEquals ("Date could not be reloaded", eventInfo1.getStart(), startLoaded);
    }
}
