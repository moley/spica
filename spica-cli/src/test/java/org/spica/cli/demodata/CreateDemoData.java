package org.spica.cli.demodata;

import org.spica.javaclient.model.Model;
import org.spica.javaclient.services.ModelCacheService;
import org.spica.javaclient.model.TopicInfo;
import org.spica.javaclient.model.UserInfo;

import java.io.File;
import java.util.Arrays;

public class CreateDemoData {

    private UserInfo userInfo1;

    private UserInfo userInfo2;

    private TopicInfo topicInfo1;

    public void createUsers (Model model) {

        userInfo1 = new UserInfo().id("1").name("Smith").firstname("John").email("john.smith@spica.org");
        userInfo2 = new UserInfo().id("2").name("Clark").firstname("Carla").email("carla.clark@spica.org");

        model.setUserInfos(Arrays.asList(userInfo1, userInfo2));

    }

    public void createTopics (Model model) {
        topicInfo1 = new TopicInfo().id("1").name("Feature 1").description("Implementation").externalSystemID("JIRA").externalSystemKey("PRO-1");

        model.setTopicInfos(Arrays.asList(topicInfo1));
    }

    public final static void main (final String [] args) {



        ModelCacheService modelCacheService = new ModelCacheService();
        modelCacheService.setConfigFile(new File("config.xml"));
        CreateDemoData createDemoData = new CreateDemoData();
        Model model = modelCacheService.get();
        createDemoData.createUsers(model);

        createDemoData.createTopics(model);

        modelCacheService.set(model, "init");


    }
}
