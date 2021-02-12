package org.spica.fx.controllers;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.spica.fx.ApplicationContext;
import org.spica.fx.LoggingOutputMessages;
import org.spica.javaclient.StandaloneActionContext;
import org.spica.javaclient.exceptions.NotFoundException;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.MessageType;
import org.spica.javaclient.model.MessagecontainerInfo;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.UserInfo;

@Slf4j
public class MessagesControllerTest {

  @Test
  public void createSecondMail () {
    //First mail was sent to firstname.lastname@spica.org
    //Second mail must be defaulted to this (and not me)
  }

  @Test
  public void answerExistingMail () throws NotFoundException {
    LoggingOutputMessages loggingOutputMessages = new LoggingOutputMessages();

    Model model = new Model();
    model.setMe(new UserInfo().username("me").id("me").email("me@spica.org"));
    model.getUserInfos().add(model.getMe());

    UserInfo creator = model.getUserNotNull("firstname.lastname@spica.org");

    MessageInfo messageInfo = new MessageInfo().message("Message1").creator(creator.getId());
    MessagecontainerInfo messagecontainerInfo = new MessagecontainerInfo().addMessageItem(messageInfo).topic("topic");
    model.getMessagecontainerInfos().add(messagecontainerInfo);

    StandaloneActionContext actionContext = new StandaloneActionContext();
    actionContext.getServices().getModelCacheService().set(model);

    ApplicationContext applicationContext = new ApplicationContext();
    applicationContext.setMessages(loggingOutputMessages);

    MainController mockedMainController = Mockito.mock(MainController.class);

    MessagesController messagesController = new MessagesController();
    messagesController.setActionContext(actionContext);
    messagesController.setMainController(mockedMainController);
    messagesController.setApplicationContext(applicationContext);
    messagesController.refreshData();
    Assert.assertEquals (1, messagesController.getViewModel().getMessageContainers().size());
    MessagecontainerInfo messagecontainerFromList = messagesController.getViewModel().getMessageContainers().get(0);
    messagesController.editExistingMessage(messagecontainerFromList);
    MessagecontainerInfo selectedMessagecontainer = messagesController.getApplicationContext().getSelectedMessageContainer();
    Assert.assertEquals(selectedMessagecontainer, messagecontainerFromList);
    MessageInfo selectedMessage = messagesController.getApplicationContext().getSelectedMessageInfo();
    Assert.assertEquals (1, selectedMessage.getRecieversTo().size());
    Assert.assertEquals (creator.getId(), selectedMessage.getRecieversTo().get(0));
    Assert.assertEquals (model.getMe().getId(), selectedMessage.getCreator());
  }

  @Test
  public void newMail () throws NotFoundException {

    LoggingOutputMessages loggingOutputMessages = new LoggingOutputMessages();

    Model model = new Model();
    model.setMe(new UserInfo().username("me").id("me").email("me@spica.org"));

    StandaloneActionContext actionContext = new StandaloneActionContext();
    actionContext.getServices().getModelCacheService().set(model);

    ApplicationContext applicationContext = new ApplicationContext();
    applicationContext.setMessages(loggingOutputMessages);

    MainController mockedMainController = Mockito.mock(MainController.class);


    MessagesController messagesController = new MessagesController();
    messagesController.setActionContext(actionContext);
    messagesController.setMainController(mockedMainController);
    messagesController.setApplicationContext(applicationContext);
    Assert.assertEquals (0, messagesController.getViewModel().getMessageContainers().size());
    messagesController.refreshData();
    Assert.assertEquals (0, messagesController.getViewModel().getMessageContainers().size());

    messagesController.getViewModel().getSearchProperty().set("mail firstname.lastname@spica.org Some other text");
    messagesController.editNewMessage();

    MessagecontainerInfo messagecontainerInfo = applicationContext.getSelectedMessageContainer();
    Assert.assertNull(messagecontainerInfo.getId());
    Assert.assertEquals ("Some other text", messagecontainerInfo.getTopic());
    log.info(messagecontainerInfo.toString());

    UserInfo recipient = model.findUserByMail("firstname.lastname@spica.org");


    MessageInfo messageInfo = applicationContext.getSelectedMessageInfo();
    Assert.assertNotNull(messageInfo.getId());
    Assert.assertEquals(MessageType.MAIL, messageInfo.getType());
    Assert.assertEquals (1, messageInfo.getRecieversTo().size());
    Assert.assertEquals (recipient.getId(), messageInfo.getRecieversTo().get(0));


    Assert.assertEquals (model.getMe().getId(), messageInfo.getCreator());
    Assert.assertTrue (messagesController.getViewModel().getSearchProperty().get().isEmpty());
    log.info(messageInfo.toString());
  }

  @Test
  public void newChat () throws NotFoundException {

    LoggingOutputMessages loggingOutputMessages = new LoggingOutputMessages();

    Model model = new Model();
    model.setMe(new UserInfo().username("me").id("me").email("me@spica.org"));
    model.getUserInfos().add(new UserInfo().username("someuser").email("someuser@spica.org"));

    StandaloneActionContext actionContext = new StandaloneActionContext();
    actionContext.getServices().getModelCacheService().set(model);

    ApplicationContext applicationContext = new ApplicationContext();
    applicationContext.setMessages(loggingOutputMessages);

    MainController mockedMainController = Mockito.mock(MainController.class);


    MessagesController messagesController = new MessagesController();
    messagesController.setActionContext(actionContext);
    messagesController.setMainController(mockedMainController);
    messagesController.setApplicationContext(applicationContext);
    Assert.assertEquals (0, messagesController.getViewModel().getMessageContainers().size());
    messagesController.refreshData();
    Assert.assertEquals (0, messagesController.getViewModel().getMessageContainers().size());

    messagesController.getViewModel().getSearchProperty().set("chat @someuser Some other text");
    messagesController.editNewMessage();

    MessagecontainerInfo messagecontainerInfo = applicationContext.getSelectedMessageContainer();
    Assert.assertNull(messagecontainerInfo.getId());
    Assert.assertEquals ("Some other text", messagecontainerInfo.getTopic());
    log.info(messagecontainerInfo.toString());

    UserInfo recipient = model.findUserByUsername("someuser");

    MessageInfo messageInfo = applicationContext.getSelectedMessageInfo();
    Assert.assertNotNull(messageInfo.getId());
    Assert.assertEquals(MessageType.CHAT, messageInfo.getType());
    Assert.assertEquals (1, messageInfo.getRecieversTo().size());
    Assert.assertEquals (recipient.getId(), messageInfo.getRecieversTo().get(0));


    Assert.assertEquals (model.getMe().getId(), messageInfo.getCreator());
    Assert.assertTrue (messagesController.getViewModel().getSearchProperty().get().isEmpty());
    log.info(messageInfo.toString());
  }

  @Test
  public void newChatInvalidUser () throws NotFoundException {

    LoggingOutputMessages loggingOutputMessages = new LoggingOutputMessages();

    Model model = new Model();
    model.setMe(new UserInfo().username("me").id("me").email("me@spica.org"));

    StandaloneActionContext actionContext = new StandaloneActionContext();
    actionContext.getServices().getModelCacheService().set(model);

    ApplicationContext applicationContext = new ApplicationContext();
    applicationContext.setMessages(loggingOutputMessages);

    MainController mockedMainController = Mockito.mock(MainController.class);


    MessagesController messagesController = new MessagesController();
    messagesController.setActionContext(actionContext);
    messagesController.setMainController(mockedMainController);
    messagesController.setApplicationContext(applicationContext);
    Assert.assertEquals (0, messagesController.getViewModel().getMessageContainers().size());
    messagesController.refreshData();
    Assert.assertEquals (0, messagesController.getViewModel().getMessageContainers().size());

    messagesController.getViewModel().getSearchProperty().set("chat @someuser Some other text");
    messagesController.editNewMessage();

    MessagecontainerInfo messagecontainerInfo = applicationContext.getSelectedMessageContainer();
    Assert.assertNull(messagecontainerInfo);
  }
}
