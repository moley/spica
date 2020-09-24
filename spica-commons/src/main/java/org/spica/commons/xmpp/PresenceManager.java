package org.spica.commons.xmpp;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.Jid;

public class PresenceManager {

  public static EntityFullJid getFullyQualifiedJID(XMPPConnection connection, BareJid jid) {
    final Roster roster = Roster.getInstanceFor(connection);

    if (roster.getEntry(jid) == null) {
      try {
        roster.createEntry(jid,  jid.getLocalpartOrNull().asUnescapedString(), new String [0]);
      } catch (SmackException.NotLoggedInException e) {
        throw new IllegalStateException(e);
      } catch (SmackException.NoResponseException e) {
        throw new IllegalStateException(e);
      } catch (XMPPException.XMPPErrorException e) {
        throw new IllegalStateException(e);
      } catch (SmackException.NotConnectedException e) {
        throw new IllegalStateException(e);
      } catch (InterruptedException e) {
        throw new IllegalStateException(e);
      }
    }
    Presence presence = roster.getPresence(jid);
    Jid result = presence.getFrom();
    EntityFullJid entityFullJid = result.asEntityFullJidIfPossible();
    return entityFullJid;
  }
}
