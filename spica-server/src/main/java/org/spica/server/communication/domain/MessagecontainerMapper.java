package org.spica.server.communication.domain;

import org.spica.server.communication.model.MessageInfo;
import org.spica.server.communication.model.MessagecontainerInfo;

public class MessagecontainerMapper {

  public MessagecontainerInfo toApi (final Messagecontainer messagecontainer) {
    MessagecontainerInfo messagecontainerInfo = new MessagecontainerInfo();
    for (Message nextMessage: messagecontainer.getMessages()) {
      messagecontainerInfo.addMessageItem(toApi(nextMessage));
    }
    return messagecontainerInfo;
  }

  public MessageInfo toApi (final Message message) {
    MessageInfo messageInfo = new MessageInfo();
    messageInfo.setCreatorId(message.getCreatorId().toString());
    messageInfo.setId(message.getId().toString());
    messageInfo.setMessage(message.getMessage());
    return messageInfo;
  }
}
