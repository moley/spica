package org.spica.fx;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import org.spica.commons.UserPresence;
import org.spica.fx.clipboard.Clipboard;

public class ApplicationContext {

  private ScreenManager screenManager = new ScreenManager();

  private Clipboard clipboard = new Clipboard();

  public ScreenManager getScreenManager() {
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
}
