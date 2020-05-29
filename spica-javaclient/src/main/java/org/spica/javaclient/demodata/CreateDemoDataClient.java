package org.spica.javaclient.demodata;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.MessageType;
import org.spica.javaclient.model.MessagecontainerInfo;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.services.ModelCacheService;
import org.spica.javaclient.model.TopicInfo;
import org.spica.javaclient.model.UserInfo;

import java.io.File;
import java.util.Arrays;

public class CreateDemoDataClient {

    private UserInfo userInfo1;

    private UserInfo userInfo2;

    private UserInfo userInfo3;

    private UserInfo userInfo4;

    private TopicInfo topicInfo1;

    public void createMe (Model model) {
        model.setMe(model.getUserInfos().get(0));
    }

    public void createUsers (Model model) {

        userInfo1 = new UserInfo().id("1").username("js").password("js").name("Smith").firstname("John").email("john.smith@spica.org").avatar("/avatars/user1.png");
        userInfo2 = new UserInfo().id("2").username("cc").password("cc").name("Clark").firstname("Carla").email("carla.clark@spica.org").avatar("/avatars/user3.png");
        userInfo3 = new UserInfo().id("3").username("sh").password("sh").name("Holmes").firstname("Sherlock").email("sherlock.holmes@spica.org").avatar("/avatars/user2.png");
        userInfo4 = new UserInfo().id("4").username("mw").password("mw").name("Watson").firstname("Mister").email("mr.watson@spica.org").avatar("/avatars/user4.png");

        model.getUserInfos().add(userInfo1);
        model.getUserInfos().add(userInfo2);
        model.getUserInfos().add(userInfo3);
        model.getUserInfos().add(userInfo4);

    }

    public void createMessages (final Model model) {

        MessagecontainerInfo chat = new MessagecontainerInfo();
        chat.setTopic("How are you?");
        MessageInfo messageInfo1 = new MessageInfo().id("1").creator("1").creationtime(LocalDateTime.now()).message("What's up. I didn't see you for a long time.\nWhat do you do today in the evening").type(MessageType.CHAT);
        MessageInfo messageInfo2 = new MessageInfo().id("2").creator("2").creationtime(LocalDateTime.now()).message("Everything is fine").type(MessageType.CHAT);
        chat.addMessageItem(messageInfo1);
        chat.addMessageItem(messageInfo2);

        MessagecontainerInfo mail = new MessagecontainerInfo();
        mail.setTopic("Feature Request");
        MessageInfo messageInfo3 = new MessageInfo().id("3").creator("3").creationtime(LocalDateTime.now()).message("Please release our cool product").type(MessageType.MAIL);
        MessageInfo messageInfo4 = new MessageInfo().id("4").creator("4").creationtime(LocalDateTime.now()).message("I have already done in the morning").type(MessageType.MAIL);
        mail.addMessageItem(messageInfo3);
        mail.addMessageItem(messageInfo4);


        model.getMessagecontainerInfos().add(chat);
        model.getMessagecontainerInfos().add(mail);

    }

    public void createTopics (Model model) {
        topicInfo1 = new TopicInfo().id("1").name("Feature 1").description("Implementation").externalSystemID("JIRA").externalSystemKey("PRO-1");

        model.getTopicInfos().add(topicInfo1);
    }

    public final static void main (final String [] args) {




        File spicaFolder = new File (".spica");

        File configFile = new File(spicaFolder, "config.xml");
        if (configFile.exists())
            configFile.delete();

        ModelCacheService modelCacheService = new ModelCacheService();
        modelCacheService.setConfigFile(configFile);
        CreateDemoDataClient createDemoDataClient = new CreateDemoDataClient();
        Model model = modelCacheService.get();
        createDemoDataClient.createUsers(model);
        createDemoDataClient.createMe(model);
        createDemoDataClient.createMessages(model);
        createDemoDataClient.createTopics(model);

        modelCacheService.set(model, "init");


    }
}
