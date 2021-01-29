package org.spica.fx;

import org.controlsfx.control.Notifications;

public class NotificationMessages implements Messages {

  private Notifications notifications;

  public NotificationMessages () {
    notifications = Notifications.create();
  }

  @Override public Messages text(String s) {
    notifications.text(s);
    return this;
  }

  @Override public void showInformation() {
    notifications.showInformation();

  }

  @Override public void showError() {
    notifications.showError();
  }
}
