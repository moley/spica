package org.spica.server.user.api;

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
