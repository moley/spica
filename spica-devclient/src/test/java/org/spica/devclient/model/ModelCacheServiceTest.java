package org.spica.devclient.model;

import com.google.common.io.Files;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.spica.javaclient.model.TopicInfo;

import java.io.File;
import java.util.Arrays;

public class ModelCacheServiceTest {

    @Test
    public void saveAndLoad () {

        File tmpDir = Files.createTempDir();
        File configFile = new File (tmpDir, "model.xml");

        TopicInfo topicInfo1 = new TopicInfo().name("name1").description("description1");
        TopicInfo topicInfo2 = new TopicInfo().name("name2").description("description2");
        ModelCache modelCache = new ModelCache();
        modelCache.setTopicInfos(Arrays.asList(topicInfo1, topicInfo2));
        ModelCacheService modelCacheService = new ModelCacheService();
        modelCacheService.setConfigFile(configFile);
        modelCacheService.set(modelCache);

        modelCacheService.close();
        ModelCache loadedCache = modelCacheService.get();
        Assert.assertEquals ("Number of loaded topics invalid", 2, loadedCache.getTopicInfos().size());
    }
}
