package org.spica.commons.xmpp;

import java.io.IOException;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.spica.commons.SpicaProperties;

public class XMPPAdapter {

  public final static String PROPERTY_XMPP_URL = "spica.xmpp.url";
  public final static String PROPERTY_XMPP_USER = "spica.xmpp.user";
  public final static String PROPERTY_XMPP_PASSWORD = "spica.xmpp.password";


  private ChatManager chatManager;
  private String host;
  private String user;
  private String password;

  public void login (SpicaProperties spicaProperties, IncomingChatMessageListener incomingChatMessageListener)
      throws IOException, InterruptedException, XMPPException, SmackException {

    host = spicaProperties.getValueNotNull(PROPERTY_XMPP_URL);
    user = spicaProperties.getValueNotNull(PROPERTY_XMPP_USER);
    password = spicaProperties.getValueNotNull(PROPERTY_XMPP_PASSWORD);

    XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
        .setUsernameAndPassword(user, password)
        .setXmppDomain(host)
        .setHost(host)
        .build();

    AbstractXMPPConnection connection = new XMPPTCPConnection(config);
    connection.connect(); //Establishes a connection to the server
    connection.login(); //Logs in
    chatManager = ChatManager.getInstanceFor(connection);

    chatManager.addIncomingListener(incomingChatMessageListener);
  }

  public void sendMessage (SpicaProperties spicaProperties, final String userId, final String message)
      throws InterruptedException, XMPPException, SmackException, IOException {
    EntityBareJid jid = JidCreate.entityBareFrom(userId + "@" + host);
    Chat chat = chatManager.chatWith(jid);
    chat.send(message);

  }


}
