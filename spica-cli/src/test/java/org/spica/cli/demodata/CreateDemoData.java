package org.spica.cli.demodata;

import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.ModelCacheService;
import org.spica.javaclient.model.TopicInfo;
import org.spica.javaclient.model.UserInfo;

import java.io.File;
import java.util.Arrays;

public class CreateDemoData {

    private UserInfo userInfo1;

    private UserInfo userInfo2;

    private TopicInfo topicInfo1;

    public void createUsers (ModelCache modelCache) {

        userInfo1 = new UserInfo().id("1").name("Smith").firstname("John").email("john.smith@spica.org");
        userInfo2 = new UserInfo().id("2").name("Clark").firstname("Carla").email("carla.clark@spica.org");

        modelCache.setUserInfos(Arrays.asList(userInfo1, userInfo2));

    }

    public void createTopics (ModelCache modelCache) {
        topicInfo1 = new TopicInfo().id("1").name("Feature 1").description("Implementation").externalSystemID("JIRA").externalSystemKey("PRO-1");

        modelCache.setTopicInfos(Arrays.asList(topicInfo1));
    }

    public final static void main (final String [] args) {



        ModelCacheService modelCacheService = new ModelCacheService();
        modelCacheService.setConfigFile(new File("config.xml"));
        CreateDemoData createDemoData = new CreateDemoData();
        ModelCache modelCache = modelCacheService.get();
        createDemoData.createUsers(modelCache);

        createDemoData.createTopics(modelCache);

        modelCacheService.set(modelCache);


    }
}
