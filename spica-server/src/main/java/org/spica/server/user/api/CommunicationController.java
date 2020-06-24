package org.spica.server.user.api;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CommunicationController {

  /**private static final Logger LOGGER = LoggerFactory.getLogger(CommunicationController.class);

  @Scheduled(fixedDelay=5000)
  @MessageMapping("/chat")
  @SendTo("/topic/messages")
  public Greeting greeting() throws Exception {
    LOGGER.info("Send greeting to topic");
    return new Greeting("Hello, it is " + DateTime.now().toString());
  }**/
}
