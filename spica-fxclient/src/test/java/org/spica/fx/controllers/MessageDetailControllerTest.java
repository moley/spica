package org.spica.fx.controllers;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.spica.commons.services.mail.MailService;
import org.spica.fx.ApplicationContext;
import org.spica.fx.LoggingOutputMessages;
import org.spica.javaclient.StandaloneActionContext;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.MessageType;
import org.spica.javaclient.model.MessagecontainerInfo;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.UserInfo;

@Slf4j
public class MessageDetailControllerTest {

  @Test
  public void sendAndRenewMailContent () {

    LoggingOutputMessages loggingOutputMessages = new LoggingOutputMessages();

    MainController mockedMainController = Mockito.mock(MainController.class);
    MailService mockedMailService = Mockito.mock(MailService.class);

    Model model = new Model();
    model.setMe(new UserInfo().username("me").id("me"));
    MessageInfo messageInfo = model.createNewMessage(MessageType.MAIL);
    MessagecontainerInfo messagecontainerInfo = model.createNewMessageContainer();

    StandaloneActionContext actionContext = new StandaloneActionContext();
    actionContext.getServices().getModelCacheService().set(model);
    actionContext.getServices().setMailService(mockedMailService);
    ApplicationContext applicationContext = new ApplicationContext();
    applicationContext.setSelectedMessageContainer(messagecontainerInfo);
    applicationContext.setSelectedMessageInfo(messageInfo);
    applicationContext.setMessages(loggingOutputMessages);

    MessageDetailController messageDetailController = new MessageDetailController();
    messageDetailController.setApplicationContext(applicationContext);
    messageDetailController.setActionContext(actionContext);
    messageDetailController.setMainController(mockedMainController);

    messageDetailController.refreshData();

    messageDetailController.getViewModel().getTopicProperty().set("Topic");
    messageDetailController.getViewModel().getMailContentProperty().set("New content");
    messageDetailController.getViewModel().getMailToProperty().set("mailto@spica.org");
    messageDetailController.getViewModel().getMailCCProperty().set("mailcc@spica.org");
    messageDetailController.getViewModel().getMailBCCProperty().set("mailbcc@spica.org");

    messageDetailController.sendMessage();

    Assert.assertEquals (1, model.getMessagecontainerInfos().size());
    MessagecontainerInfo messagecontainerFromModel = model.getMessagecontainerInfos().get(0);
    Assert.assertEquals("Topic", messagecontainerFromModel.getTopic());
    Assert.assertEquals (1, messagecontainerFromModel.getMessage().size());
    MessageInfo sentMessage = messagecontainerFromModel.getMessage().get(0);
    Assert.assertEquals (1, sentMessage.getRecieversTo().size());
    Assert.assertEquals (1, sentMessage.getRecieversCC().size());
    Assert.assertEquals (1, sentMessage.getRecieversBCC().size());
    Assert.assertEquals ("mailto@spica.org", sentMessage.getRecieversTo().get(0));
    Assert.assertEquals ("mailcc@spica.org", sentMessage.getRecieversCC().get(0));
    Assert.assertEquals ("mailbcc@spica.org", sentMessage.getRecieversBCC().get(0));
    Assert.assertNotSame(messageDetailController.getViewModel().getCurrentMessage(), messagecontainerFromModel.getMessage().get(0));

    Assert.assertEquals (0, model.findUnreadMessages ().size());

    // TODO Check if history is adapted and editor is cleared
  }
}
