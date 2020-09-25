package org.spica.commons.xmpp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.SubscribeListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.bytestreams.socks5.Socks5Proxy;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.spica.commons.SpicaProperties;
import org.spica.commons.UserPresence;

import static java.lang.Thread.sleep;

@Slf4j public class XMPPAdapter {

  public final static String PROPERTY_XMPP_URL = "spica.xmpp.url";
  public final static String PROPERTY_XMPP_USER = "spica.xmpp.user";
  public final static String PROPERTY_XMPP_PASSWORD = "spica.xmpp.password";

  private ChatManager chatManager;
  private FileTransferManager fileTransferManager;
  private String host;
  private String user;
  private String password;

  private AbstractXMPPConnection connection;

  private Roster roster;

  public void login(SpicaProperties spicaProperties, IncomingChatMessageListener incomingChatMessageListener,
      FileTransferListener fileTransferListener)
      throws IOException, InterruptedException, XMPPException, SmackException {

    SmackConfiguration.DEBUG = true;
    Socks5Proxy.setLocalSocks5ProxyEnabled(false);
    SmackConfiguration.setUnknownIqRequestReplyMode(SmackConfiguration.UnknownIqRequestReplyMode.doNotReply);
    host = spicaProperties.getValueNotNull(PROPERTY_XMPP_URL);
    user = spicaProperties.getValueNotNull(PROPERTY_XMPP_USER);
    password = spicaProperties.getValueNotNull(PROPERTY_XMPP_PASSWORD);
    System.out.println (host);
    System.out.println (user);
    System.out.println (password);

    XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
    builder = builder.setUsernameAndPassword(user, password).setXmppDomain(host).setHost(host);




    connection = new XMPPTCPConnection(builder.build());
    connection.connect(); //Establishes a connection to the server
    connection.login(); //Logs in



    System.out.println ("Connected     : " + connection.isConnected());
    System.out.println ("Anonymous     : " + connection.isAnonymous());
    System.out.println ("Authenticated : " + connection.isAuthenticated());

    Roster roster = Roster.getInstanceFor(connection);
    roster.addSubscribeListener(new SubscribeListener() {
      @Override public SubscribeAnswer processSubscribe(Jid from, Presence subscribeRequest) {
        return SubscribeAnswer.Approve; //automatically approve subscriptions
      }
    });
    if (! roster.isLoaded()) {
      try {
        roster.reloadAndWait();
      } catch (SmackException.NotLoggedInException | SmackException.NotConnectedException | InterruptedException e) {
        throw new RuntimeException(e);
      }
    }


    Collection<RosterEntry> entries = roster.getEntries();
    for (RosterEntry entry : entries) {
      System.out.println ("  Roster: " + entry);
      System.out.println ("  Can see his presence  : " + entry.canSeeHisPresence());
      System.out.println ("  Can see my presence   : " + entry.canSeeMyPresence());
      System.out.println ("  Subscription pending  : " + entry.isSubscriptionPending());
      System.out.println ("  Approved              : " + entry.isApproved());
      System.out.println ("  Subscription          : " + entry.getType());
    }
    System.out.println ("Rosters: " + entries.size());

    chatManager = ChatManager.getInstanceFor(connection);

    fileTransferManager = FileTransferManager.getInstanceFor(connection);
    //FileTransferNegotiator.IBB_ONLY = true;

    fileTransferManager.addFileTransferListener(fileTransferListener);

    chatManager.addIncomingListener(incomingChatMessageListener);
  }

  public void sendMessage(SpicaProperties spicaProperties, final String userId, final String message)
      throws InterruptedException, IOException, SmackException, XMPPException {
    sendMessage(spicaProperties, userId, message, new ArrayList<>());
  }

  public void sendMessage(SpicaProperties spicaProperties, final String userId, final String message,
      final List<File> files) throws InterruptedException, XMPPException, SmackException, IOException {
    EntityBareJid jid = JidCreate.entityBareFrom(userId + "@" + host);
    Chat chat = chatManager.chatWith(jid);

    chat.send(message);

    sleep(1000);

    EntityFullJid entityFullJid = PresenceManager.getFullyQualifiedJID(connection, jid);

    if (!files.isEmpty()) {
      for (File next : files) {
        OutgoingFileTransfer outgoingFileTransfer = fileTransferManager.createOutgoingFileTransfer(entityFullJid);

        if (! next.exists())
          throw new IllegalStateException("Does not exist: " + next.getAbsolutePath());

        outgoingFileTransfer.sendFile(next, "Some test");


        while (!outgoingFileTransfer.isDone()) {
         Thread.sleep(100);
        }
        outgoingFileTransfer.cancel();


      }


    }

    log.info("Sent " + files.size() + " files");
  }

  public void setPresence(final UserPresence userPresence, final String status) {
    Presence presence = new Presence(Presence.Type.available);

    if (userPresence.equals(UserPresence.ONLINE)) {
      presence.setMode(Presence.Mode.chat);

    } else {
      presence.setMode(Presence.Mode.away);
    }

    presence.setFrom(connection.getUser());
    presence.setStatus(status);

    // Send the stanza (assume we have an XMPPConnection instance called "con").
    try {
      connection.sendStanza(presence);
    } catch (SmackException.NotConnectedException e) {
      log.error(e.getLocalizedMessage(), e);
    } catch (InterruptedException e) {
      log.error(e.getLocalizedMessage(), e);
    }

    log.info("Finished setting presence");

  }

  public void close() {
    connection.disconnect();

  }
}
