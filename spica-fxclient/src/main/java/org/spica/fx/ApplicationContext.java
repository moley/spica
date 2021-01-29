package org.spica.fx;

import java.time.LocalDateTime;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import lombok.extern.slf4j.Slf4j;
import org.spica.commons.UserPresence;
import org.spica.fx.clipboard.Clipboard;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.MessagecontainerInfo;
import org.spica.javaclient.model.ProjectInfo;
import org.spica.javaclient.model.TaskInfo;

@Slf4j
public class ApplicationContext {

  private MessagecontainerInfo selectedMessageContainer;

  private MessageInfo selectedMessageInfo;

  private TaskInfo selectedTaskInfo;

  private ProjectInfo selectedProjectInfo;

  private ScreenManager screenManager;

  private Messages messages;

  private Clipboard clipboard = new Clipboard();

  public Messages getMessages ( ){
    if (messages == null)
      return new NotificationMessages();
    else
      return messages;
  }

  public void setMessages (final Messages notifications) {
    this.messages = notifications;
  }

  public ScreenManager getScreenManager() {
    if (screenManager == null)
      screenManager = new ScreenManager();
    return screenManager;
  }

  public Clipboard getClipboard() {
    return clipboard;
  }


  private UserPresence presence = UserPresence.ONLINE;

  private Property<String> presenceProperty = new SimpleStringProperty();

  public UserPresence getPresence() {
    return presence;
  }

  public void setPresence(UserPresence presence) {
    this.presence = presence;
  }

  public String getPresenceProperty() {
    return presenceProperty.getValue();
  }

  public Property<String> presencePropertyProperty() {
    return presenceProperty;
  }

  public MessagecontainerInfo getSelectedMessageContainer() {
    return selectedMessageContainer;
  }

  public MessageInfo getSelectedMessageInfo() {
    return selectedMessageInfo;
  }

  public void setSelectedMessageInfo(MessageInfo selectedMessageInfo) {
    log.info("set selected message " + selectedMessageInfo);
    this.selectedMessageInfo = selectedMessageInfo;
  }

  /**
   * select the message container
   * @param selectedMessageContainer message container
   * @return true: read time was added, caller should save the model
   */
  public boolean setSelectedMessageContainer(MessagecontainerInfo selectedMessageContainer) {
    boolean readtimeSet = false;
    if (selectedMessageContainer != null) {
      if (selectedMessageContainer.getMessage() != null) {
        for (MessageInfo nextMessage : selectedMessageContainer.getMessage()) {
          if (nextMessage.getReadtime() == null) {
            nextMessage.setReadtime(LocalDateTime.now());
            readtimeSet = true;
          }
        }
      }
    }
    log.info("set selected message container " + selectedMessageContainer);
    this.selectedMessageContainer = selectedMessageContainer;
    return readtimeSet;
  }

  public TaskInfo getSelectedTaskInfo() {
    return selectedTaskInfo;
  }

  public void setSelectedTaskInfo(TaskInfo selectedTaskInfo) {
    this.selectedTaskInfo = selectedTaskInfo;
  }

  public ProjectInfo getSelectedProjectInfo() {
    return selectedProjectInfo;
  }

  public void setSelectedProjectInfo(ProjectInfo selectedProjectInfo) {
    this.selectedProjectInfo = selectedProjectInfo;
  }
}
