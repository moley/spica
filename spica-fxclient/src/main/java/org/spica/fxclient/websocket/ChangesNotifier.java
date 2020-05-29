package org.spica.fxclient.websocket;

import java.lang.reflect.Type;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class ChangesNotifier {

  private static final Logger LOGGER = LoggerFactory.getLogger(ChangesNotifier.class);


  public ChangesNotifier () {

    WebSocketClient client = new StandardWebSocketClient();



    WebSocketStompClient stompClient = new WebSocketStompClient(client);
    stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    StompSessionHandler sessionHandler = new StompSessionHandlerAdapter() {
      @Override public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        LOGGER.info("afterConnected ");
        session.subscribe("/topic/messages", this);
        session.send("/app/chat", "Hello");

      }

      @Override public void handleException(StompSession session, StompCommand command, StompHeaders headers,
          byte[] payload, Throwable exception) {
        LOGGER.info("handleException ", exception);

      }

      @Override public void handleTransportError(StompSession session, Throwable exception) {
        LOGGER.info("handleTransportError", exception);

      }

      @Override public Type getPayloadType(StompHeaders headers) {
        LOGGER.info("getPayloadType");
        return null;
      }

      @Override public void handleFrame(StompHeaders headers, Object payload) {
        Message msg = (Message) payload;
        LOGGER.info("Received : " + msg.toString());

      }
    };
    String url = "ws://localhost:8765/api/";
    stompClient.connect(url, sessionHandler);

    new Scanner(System.in).nextLine(); // Don't close immediately.
  }

  public static void main(String[] args) {
    ChangesNotifier changesNotifier = new ChangesNotifier();


  }
}
