package org.spica.commons.xmpp;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;
import org.spica.commons.SpicaProperties;

public class XMPPAdapterTester {

  public static void main(String[] args) throws InterruptedException, XMPPException, SmackException, IOException {
    //SmackConfiguration.DEBUG = true;
    XMPPAdapter adapter = new XMPPAdapter();
    SpicaProperties spicaProperties = new SpicaProperties();
    String remoteUser = spicaProperties.getValue("spica.xmpp.remoteUser");
    adapter.login(spicaProperties, new IncomingChatMessageListener() {
      @Override public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
        System.out.println ("Incoming message from " + from.toString() + ":" + message.toString());
      }
    });
    System.out.println ("Login successfull");


    File file = new File("/etc/hosts");

    adapter.sendMessage(spicaProperties, remoteUser, "This is a message");
    System.out.println ("Sent message");

    adapter.sendMessage(spicaProperties, remoteUser, "And a message together with a file " + file.getAbsolutePath(), Arrays.asList(file));
    System.out.println ("Sent message with file");

    do {
      Thread.sleep(1000);

    } while (true);



  }
}
